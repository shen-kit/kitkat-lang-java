package frontend.ast;

public enum NodeType {
	// statements -> return null
	PROGRAM,
	VAR_DECLARATION,
	// expressions -> return a value
	VAR_ASSIGNMENT,
	NULL,
	BOOLEAN,
	NUMBER,
	BINARY_EXPR,
	IDENTIFIER,
}
