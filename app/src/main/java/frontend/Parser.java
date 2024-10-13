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
import frontend.ast.Expr;
import frontend.ast.Identifier;
import frontend.ast.NullLiteral;
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

	private Token expect(TokenType expected, String errorMsg) {
		Token t = this.tokens.poll();
		if (t == null || t.type != expected) {
			throw new RuntimeException(
					String.format("%s\nExpected: %s\nReceived: %s\n\n", errorMsg, expected, t.type));
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
		return parseAddExpr();
	}

	private Expr parseAddExpr() {
		Expr left = parseMultiplyExpr();
		while (at().value.equals("+") || at().value.equals("-")) {
			String op = eat().value;
			Expr right = parseMultiplyExpr();
			left = new BinaryExpr(left, right, op);
		}
		return left;
	}

	private Expr parseMultiplyExpr() {
		Expr left = parseBaseExpr();
		while (at().value.equals("*") || at().value.equals("/") || at().value.equals("%")) {
			String op = eat().value;
			Expr right = parseBaseExpr();
			left = new BinaryExpr(left, right, op);
		}
		return left;
	}

	private Expr parseBaseExpr() {
		Token t = eat();
		switch (t.type) {
			case TokenType.IDENTIFIER:
				return new Identifier(t.value);
			case TokenType.NULL:
				return new NullLiteral();
			case TokenType.NUMBER:
				return new NumberLiteral(Integer.parseInt(t.value));
			case TokenType.OPEN_PAREN:
				Expr e = parseExpr();
				expect(TokenType.CLOSE_PAREN, "must close brackets after opening");
				return e;
			default:
				throw new RuntimeException("Could not parse token: " + t.type + ", value = " + t.value);
		}
	}
}
