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

import frontend.ast.BinaryExpr;
import frontend.ast.ExprNode;
import frontend.ast.IdentifierNode;
import frontend.ast.NodeType;
import frontend.ast.NumberNode;
import frontend.ast.Program;
import frontend.ast.StatementNode;
import frontend.ast.VarDeclarationNode;
import frontend.ast.VarAssignmentNode;

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

	private StatementNode parseStatement() {
		switch (at().type) {
			case TokenType.VAR_DECLARATION:
				return parseVarDeclaration();
			default:
				StatementNode expr = parseExpr();
				expect(TokenType.SEMICOLON, "Semicolon expected after expression.");
				return expr;
		}
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
		// var declaration must be terminated with a semicolon
		expect(TokenType.SEMICOLON, "Semicolon expected after expression.");
		return new VarDeclarationNode(varname, expr, isConst);
	}

	/**
	 * Parse an expression into an expression tree
	 *
	 * Order of Precedence:
	 * 1. Base expressions (literals, parentheses, keywords)
	 * 2. Multiplication
	 * 3. Addition
	 * 4. Variable assignment
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
		ExprNode left = parseAddExpr();
		if (at().type == TokenType.EQUALS) {
			eat(); // advance past equals token
			return new VarAssignmentNode(left, parseExpr());
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
		ExprNode left = parseBaseExpr();
		while (at().value.equals("*") || at().value.equals("/") || at().value.equals("%")) {
			String op = eat().value;
			ExprNode right = parseBaseExpr();
			left = new BinaryExpr(left, right, op);
		}
		return left;
	}

	/**
	 * Parses a base expression (cannot be broken down further):
	 * identifiers, numbers, booleans, brackets
	 * 
	 * @return Expression
	 */
	private ExprNode parseBaseExpr() {
		Token t = eat();
		switch (t.type) {
			case TokenType.IDENTIFIER:
				return new IdentifierNode(t.value);
			case TokenType.NUMBER:
				return new NumberNode(Integer.parseInt(t.value));
			case TokenType.OPEN_PAREN:
				ExprNode e = parseExpr();
				expect(TokenType.CLOSE_PAREN, "must close brackets after opening");
				return e;
			default:
				throw new RuntimeException("Could not parse token: " + t.type + ", value = " + t.value);
		}
	}
}
