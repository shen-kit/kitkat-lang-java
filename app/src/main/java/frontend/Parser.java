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
import frontend.ast.BooleanLiteral;
import frontend.ast.Expr;
import frontend.ast.Identifier;
import frontend.ast.NumberLiteral;
import frontend.ast.Program;
import frontend.ast.Stmt;

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

	private Stmt parseStatement() {
		return parseExpr();
	}

	private Expr parseExpr() {
		return parseAddExpr();
	}

	/**
	 * Parses an expression of the form:
	 * <EXPR> + <EXPR>, or
	 * <EXPR> - <EXPR>
	 * 
	 * @return Expression
	 */
	private Expr parseAddExpr() {
		Expr left = parseMultiplyExpr();
		while (at().value.equals("+") || at().value.equals("-")) {
			String op = eat().value;
			Expr right = parseMultiplyExpr();
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
	private Expr parseMultiplyExpr() {
		Expr left = parseBaseExpr();
		while (at().value.equals("*") || at().value.equals("/") || at().value.equals("%")) {
			String op = eat().value;
			Expr right = parseBaseExpr();
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
	private Expr parseBaseExpr() {
		Token t = eat();
		switch (t.type) {
			case TokenType.IDENTIFIER:
				return new Identifier(t.value);
			case TokenType.NUMBER:
				return new NumberLiteral(Integer.parseInt(t.value));
			case TokenType.BOOLEAN:
				return new BooleanLiteral(t.value.equals("true"));
			case TokenType.OPEN_PAREN:
				Expr e = parseExpr();
				expect(TokenType.CLOSE_PAREN, "must close brackets after opening");
				return e;
			default:
				throw new RuntimeException("Could not parse token: " + t.type + ", value = " + t.value);
		}
	}
}
