package frontend;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import static java.util.Map.entry;

class Lexer {

  private static final Map<String, TokenType> reservedKeywords = Map.ofEntries(
      entry("let", TokenType.LET),
      entry("const", TokenType.CONST));

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
        case '(' -> tokens.add(token(c, TokenType.OPEN_PAREN));
        case ')' -> tokens.add(token(c, TokenType.CLOSE_PAREN));
        case '=' -> tokens.add(token(c, TokenType.EQUALS));
        case '+', '-', '*', '/', '%' -> tokens.add(token(c, TokenType.BINARY_OPERATOR));
        default -> {
          /* multi-character tokens */

          if (Character.isDigit(c)) { // number
            String num = "" + c;
            while (!q.isEmpty() && Character.isDigit(q.peek())) {
              num += q.poll();
            }
            tokens.add(token(num, TokenType.NUMBER));
          } else if (Character.isLetter(c)) { // identifier
            String word = "" + c;
            while (!q.isEmpty() && Character.isLetter(q.peek())) {
              word += q.poll();
            }

            if (reservedKeywords.containsKey(word)) {
              tokens.add(token(word, reservedKeywords.get(word)));
            } else {
              tokens.add(token(word, TokenType.IDENTIFIER));
            }
          } else if (isSkippable(c)) { // skippable
          } else {
            System.out.println("unrecognised character found: " + c);
          }
        }
      }
    }

    tokens.add(token("EOF", TokenType.EOF));
    return tokens;
  }
}
