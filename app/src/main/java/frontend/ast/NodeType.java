package frontend.ast;

public enum NodeType {
  // statements -> return null
  PROGRAM,
  VAR_DECLARATION,
  PRINT,
  // expressions -> return a value
  VAR_ASSIGNMENT,
  OBJECT,
  PROPERTY,
  NULL,
  BOOLEAN,
  NUMBER,
  BINARY_EXPR,
  IDENTIFIER, MEMBER, STRING,
}
