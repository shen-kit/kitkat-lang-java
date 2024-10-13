package frontend.ast;

public class NumberLiteral extends Expr {
  int value;

  public NumberLiteral(int value) {
    this.kind = NodeType.NUMBER;
    this.value = value;
  }

  @Override
  public void print() {
    System.out.println("number: " + String.valueOf(this.value));
  }
}
