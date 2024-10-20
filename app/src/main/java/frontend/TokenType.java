package frontend;

public enum TokenType {
  // literals
  NUMBER,
  IDENTIFIER,
  SEMICOLON, COLON,
  COMMA, DOT,
  STRING,
  // keywords
  VAR_DECLARATION,
  PRINT,
  // operators / groupings
  EQUALS,
  BINARY_OPERATOR,
  COMPARATOR, // > / < / == / != / >= / <=
  OPEN_PAREN, CLOSE_PAREN,
  OPEN_BRACKET, CLOSE_BRACKET,
  OPEN_BRACE, CLOSE_BRACE,
  // eof
  EOF,
};
