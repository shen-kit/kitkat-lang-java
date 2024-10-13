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
import frontend.ast.BooleanNode;
import frontend.ast.ExprNode;
import frontend.ast.IdentifierNode;
import frontend.ast.NumberNode;
import frontend.ast.Program;
import frontend.ast.StatementNode;

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

	/**
	 * Order of Precedence:
	 *
	 * 1. Base expressions (literals, parentheses, keywords)
	 * 2. Multiplication
	 * 3. Addition
	 */

	private StatementNode parseStatement() {
		return parseExpr();
	}

	private ExprNode parseExpr() {
		return parseAddExpr();
	}

	/**
	 * Parses an expression of the form:
	 * <EXPR> + <EXPR>, or
	 * <EXPR> - <EXPR>
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
	 * <EXPR> * <EXPR>, or
	 * <EXPR> / <EXPR>, or
	 * <EXPR> % <EXPR>
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
			case TokenType.BOOLEAN:
				return new BooleanNode(t.value.equals("true"));
			case TokenType.OPEN_PAREN:
				ExprNode e = parseExpr();
				expect(TokenType.CLOSE_PAREN, "must close brackets after opening");
				return e;
			default:
				throw new RuntimeException("Could not parse token: " + t.type + ", value = " + t.value);
		}
	}
}
