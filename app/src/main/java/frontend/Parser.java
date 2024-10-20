package frontend;

/**
 * Parser.java
 * 
 * The parser's job is to take a list of tokens, and convert it into an
 * abstract syntax tree (AST) for interpretation/compilation
 *
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import frontend.ast.BinaryExpr;
import frontend.ast.ComparatorNode;
import frontend.ast.ExprNode;
import frontend.ast.IdentifierNode;
import frontend.ast.MemberNode;
import frontend.ast.NumberNode;
import frontend.ast.ObjectNode;
import frontend.ast.PrintNode;
import frontend.ast.Program;
import frontend.ast.PropertyNode;
import frontend.ast.StatementNode;
import frontend.ast.StringNode;
import frontend.ast.VarAssignmentNode;
import frontend.ast.VarDeclarationNode;

/**
 * 
 */
public class Parser {
  private Deque<Token> tokens = new ArrayDeque<>();

  /**
   * Entry point for this class, creates the full AST
   * 
   * @return Program (list of Statements)
   */
  public Program createAST(String sourceCode) {
    this.tokens = Lexer.tokenise(sourceCode);
    // tokens.stream().forEach((Token t) -> System.out.println(t.value));
    Program p = new Program(new ArrayList<>());

    while (this.tokens.peek().type != TokenType.EOF) {
      p.body.add(parseStatement());
    }

    return p;
  }

  /**
   * @return the value of the next token, does not modify tokens list
   */
  private Token at() {
    return this.tokens.peek();
  }

  /**
   * @return value of the next token, and removes it from this.tokens
   */
  private Token eat() {
    return this.tokens.poll();
  }

  /**
   * Same as eat(), but throws an exception if the token type is not what was
   * expected
   * 
   * @return value of the next token
   */
  private Token expect(TokenType expected, String errorMsg) {
    Token t = this.tokens.poll();
    if (t == null || t.type != expected) {
      throw new RuntimeException(
          String.format("%s\nExpected: %s\nReceived: %s\n\n", errorMsg, expected, t.type));
    }
    return t;
  }

  /**
   * Parse sequential tokens that form a statement
   * 
   * @return StatementNode for the statement
   */
  private StatementNode parseStatement() {
    StatementNode stmt = switch (at().type) {
      case TokenType.VAR_DECLARATION -> parseVarDeclaration();
      case TokenType.PRINT -> parsePrintStmt();
      default -> parseExpr();
    };
    expect(TokenType.SEMICOLON, "Semicolon expected after expression.");
    return stmt;
  }

  /**
   * Parses a variable declaration of the form:
   * ( let | const ) ( varname ) = ( expr );
   * 
   * @return VarDeclarationNode if successful
   */
  private StatementNode parseVarDeclaration() {
    boolean isConst = eat().value.equals("const");
    String varname = eat().value;
    ExprNode expr = new IdentifierNode("null");
    // value required if const
    if (isConst || at().type == TokenType.EQUALS) {
      expect(TokenType.EQUALS, "'=' expected following variable name in variable declaration.");
      expr = parseExpr();
    }
    return new VarDeclarationNode(varname, expr, isConst);
  }

  /**
   * Parses a variable declaration of the form:
   * ( let | const ) ( varname ) = ( expr );
   * 
   * @return VarDeclarationNode if successful
   */
  private StatementNode parsePrintStmt() {
    eat(); // eat PRINT token
    expect(TokenType.OPEN_PAREN, "print must be followed by an open parenthesis");
    ExprNode expr = parseExpr();
    expect(TokenType.CLOSE_PAREN, "open bracket not closed");
    return new PrintNode(expr);
  }

  /**
   * Parse an expression into an expression tree
   *
   * Order of Precedence:
   * 1. Base expressions (literals, parentheses, keywords)
   * 2. Members (object.member || object[memberExpr])
   * 3. Multiplication
   * 4. Addition
   * 5. Comparators
   * 6. Objects
   * 7. Variable assignment
   */
  private ExprNode parseExpr() {
    return parseAssignmentExpr();
  }

  /**
   * Parses a variable assignment of the form:
   * ( assignee ) = ( expr )
   * 
   * @return Expression
   */
  private ExprNode parseAssignmentExpr() {
    ExprNode left = parseObject();
    if (at().type == TokenType.EQUALS) {
      eat(); // advance past equals token
      return new VarAssignmentNode(left, parseExpr());
    }
    return left;
  }

  /**
   * Parses an object of the form:
   * { ( key ): ( value), ... }
   */
  private ExprNode parseObject() {
    if (at().type != TokenType.OPEN_BRACE) {
      return parseComparatorExpr();
    }

    this.eat(); // consume {
    List<PropertyNode> properties = new ArrayList<>();

    while (at().type != TokenType.EOF && at().type != TokenType.CLOSE_BRACE) {

      String key = expect(TokenType.IDENTIFIER, "identifier expected").value;
      expect(TokenType.COLON, "Colon (:) missing after key in object definition");

      ExprNode value = parseObject();
      expect(TokenType.COMMA, "Comma missing after property assignment");
      properties.add(new PropertyNode(key, value));
    }
    expect(TokenType.CLOSE_BRACE, "Object not closed");
    return new ObjectNode(properties);
  }

  private ExprNode parseComparatorExpr() {
    ExprNode left = parseAddExpr();
    while (at().type == TokenType.COMPARATOR) {
      String comparator = eat().value;
      ExprNode right = parseAddExpr();
      left = new ComparatorNode(left, right, comparator);
    }
    return left;
  }

  /**
   * Parses an expression of the form:
   * ( expr ) ( + | - ) ( expr )
   * 
   * @return Expression
   */
  private ExprNode parseAddExpr() {
    ExprNode left = parseMultiplyExpr();
    while (at().value.equals("+") || at().value.equals("-")) {
      String op = eat().value;
      ExprNode right = parseMultiplyExpr();
      left = new BinaryExpr(left, right, op);
    }
    return left;
  }

  /**
   * Parses an expression of the form:
   * ( expr ) ( * | / | % ) ( expr )
   * 
   * @return Expression
   */
  private ExprNode parseMultiplyExpr() {
    ExprNode left = parseMemberExpr();
    while (at().value.equals("*") || at().value.equals("/") || at().value.equals("%")) {
      String op = eat().value;
      ExprNode right = parseMemberExpr();
      left = new BinaryExpr(left, right, op);
    }
    return left;
  }

  private ExprNode parseMemberExpr() {
    ExprNode left = parseBaseExpr();
    while (at().type == TokenType.DOT) {
      eat(); // consume the dot
      Token t = expect(TokenType.IDENTIFIER, "Identifier expected following dot operator");
      IdentifierNode right = new IdentifierNode(t.value);
      left = new MemberNode(left, (IdentifierNode) right);
    }
    return left;
  }

  /**
   * Parses a base expression (cannot be broken down further):
   * identifiers, numbers, string literals, booleans, brackets
   * 
   * @return Expression
   */
  private ExprNode parseBaseExpr() {
    Token t = eat();
    switch (t.type) {
      case TokenType.IDENTIFIER -> {
        return new IdentifierNode(t.value);
      }
      case TokenType.NUMBER -> {
        return new NumberNode(Integer.parseInt(t.value));
      }
      case TokenType.STRING -> {
        return new StringNode(t.value);
      }
      case TokenType.OPEN_PAREN -> {
        ExprNode e = parseExpr();
        expect(TokenType.CLOSE_PAREN, "must close brackets after opening");
        return e;
      }
      default -> throw new RuntimeException("Could not parse token: " + t.type + ", value = " + t.value);
    }
  }
}
