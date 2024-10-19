package frontend;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import static java.util.Map.entry;

class Lexer {

  private static final Map<String, TokenType> reservedKeywords = Map.ofEntries(
      entry("let", TokenType.VAR_DECLARATION),
      entry("const", TokenType.VAR_DECLARATION),
      entry("print", TokenType.PRINT));

  /**
   * @return can c be ignored?
   */
  private static boolean isSkippable(char c) {
    return c == ' ' || c == '\n' || c == '\t';
  }

  /**
   * Create a Token object
   */
  private static Token token(String value, TokenType type) {
    return new Token(value, type);
  }

  /**
   * method overload: create a Token(String, TokenType) from a character
   */
  private static Token token(char value, TokenType type) {
    return new Token("" + value, type);
  }

  private static boolean isValidIdentifierChar(char c) {
    return Character.isLetter(c) || c == '_';
  }

  static Deque<Token> tokenise(String sourceCode) {
    Deque<Token> tokens = new ArrayDeque<>();
    char[] src = sourceCode.toCharArray();
    Deque<Character> q = new ArrayDeque<>();
    for (char c : src)
      q.add(c);

    char c;
    while (!q.isEmpty()) {
      c = q.poll();
      switch (c) {
        // separators
        case ';' -> tokens.add(token(c, TokenType.SEMICOLON));
        case ':' -> tokens.add(token(c, TokenType.COLON));
        case ',' -> tokens.add(token(c, TokenType.COMMA));
        case '.' -> tokens.add(token(c, TokenType.DOT));
        // operators / groupings
        case '(' -> tokens.add(token(c, TokenType.OPEN_PAREN));
        case ')' -> tokens.add(token(c, TokenType.CLOSE_PAREN));
        case '[' -> tokens.add(token(c, TokenType.OPEN_BRACKET));
        case ']' -> tokens.add(token(c, TokenType.CLOSE_BRACKET));
        case '{' -> tokens.add(token(c, TokenType.OPEN_BRACE));
        case '}' -> tokens.add(token(c, TokenType.CLOSE_BRACE));
        case '=' -> tokens.add(token(c, TokenType.EQUALS));
        case '+', '-', '*', '/', '%' -> tokens.add(token(c, TokenType.BINARY_OPERATOR));
        // comments
        case '#' -> {
          while (!q.isEmpty() && q.peek() != '\n')
            q.poll();
        }
        default -> {

          /* multi-character tokens */

          // string literal
          if (c == '"') {
            String s = "";
            while (!q.isEmpty() && q.peek() != '"') {
              s += q.poll();
            }
            if (q.isEmpty())
              throw new RuntimeException("Open quote never closed.");
            q.poll(); // eat closing quotation mark
            tokens.add(token(s, TokenType.STRING));
          }

          // number
          else if (Character.isDigit(c)) {
            String num = "" + c;
            while (!q.isEmpty() && Character.isDigit(q.peek())) {
              num += q.poll();
            }
            tokens.add(token(num, TokenType.NUMBER));
          }

          // identifier
          else if (isValidIdentifierChar(c)) {
            String word = "" + c;
            while (!q.isEmpty() && isValidIdentifierChar(q.peek())) {
              word += q.poll();
            }

            if (reservedKeywords.containsKey(word)) {
              tokens.add(token(word, reservedKeywords.get(word)));
            } else {
              tokens.add(token(word, TokenType.IDENTIFIER));
            }
          }

          else if (!isSkippable(c)) {
            System.out.println("unrecognised character found: " + c);
          }
        }
      }
    }

    tokens.add(token("EOF", TokenType.EOF));
    return tokens;
  }
}
