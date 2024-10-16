package frontend;

public enum TokenType {
	// literals
	NUMBER,
	IDENTIFIER,
	SEMICOLON,
	// keywords
	VAR_DECLARATION,
	// operators / groupings
	BINARY_OPERATOR,
	EQUALS,
	OPEN_PAREN,
	CLOSE_PAREN,
	// eof
	EOF,
};
