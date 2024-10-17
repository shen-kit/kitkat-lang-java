package frontend;

public enum TokenType {
	// literals
	NUMBER,
	IDENTIFIER,
	SEMICOLON,
	COLON,
	COMMA,
	// keywords
	VAR_DECLARATION,
	PRINT,
	// operators / groupings
	BINARY_OPERATOR,
	EQUALS,
	OPEN_PAREN,
	CLOSE_PAREN,
	OPEN_BRACE,
	CLOSE_BRACE,
	// eof
	EOF,
};
