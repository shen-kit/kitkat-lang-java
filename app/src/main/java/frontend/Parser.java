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

import frontend.ast.Expr;
import frontend.ast.Identifier;
import frontend.ast.NumberLiteral;
import frontend.ast.Program;
import frontend.ast.Stmt;

public class Parser {
  private Deque<Token> tokens = new ArrayDeque<>();

  private Token at() {
    return this.tokens.peek();
  }

  private Token eat() {
    return this.tokens.poll();
  }

  private Token expect(Token expected, String errorMsg) {
    Token t = this.tokens.poll();
    if (t == null || t != expected) {
      throw new RuntimeException(
          String.format("%s\nExpected: %s\nReceived: %s\n\n", errorMsg, expected, t));
    }
    return t;
  }

  public Program createAST(String sourceCode) {
    this.tokens = Lexer.tokenise(sourceCode);
    Program p = new Program(new ArrayList<>());

    while (this.tokens.peek().type != TokenType.EOF) {
      p.body.add(parseStatement());
    }

    return p;
  }

  /**
   * Order of Precedence:
   *
   * 1. Base expressions (literals, parentheses, keywords)
   * 2. Multiplication
   * 3. Addition
   */

  private Stmt parseStatement() {
    return parseExpr();
  }

  private Expr parseExpr() {
    return parseBaseExpr();
  }

  private Expr parseBaseExpr() {
    Token t = this.eat();
    switch (t.type) {
      case TokenType.IDENTIFIER:
        return new Identifier(t.value);
      case TokenType.NUMBER:
        return new NumberLiteral(Integer.parseInt(t.value));
      case TokenType.EOF:

      default:
        throw new RuntimeException("Could not parse token: " + t.type + ", value = " + t.value);
    }
  }
}
