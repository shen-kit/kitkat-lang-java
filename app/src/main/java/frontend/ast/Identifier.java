package frontend.ast;

public class Identifier extends Expr {
  String symbol;

  public Identifier(String symbol) {
    this.kind = NodeType.IDENTIFIER;
    this.symbol = symbol;
  }

  @Override
  public String toString() {
    return "identifier: " + this.symbol;
  }

  @Override
  public void print() {
    System.out.println("identifier: " + symbol);
  }
}
