package frontend;

public enum TokenType {
	// literals
	NUMBER,
	IDENTIFIER,
	// keywords
	LET,
	CONST,
	// operators / groupings
	BINARY_OPERATOR,
	EQUALS,
	OPEN_PAREN,
	CLOSE_PAREN,
	// eof
	EOF,
};
