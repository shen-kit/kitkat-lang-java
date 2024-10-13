package frontend.ast;

public class BinaryExpr extends Expr {
  Expr left;
  Expr right;
  String operator;

  public BinaryExpr(Expr left, Expr right, String operator) {
    this.kind = NodeType.BINARY_EXPR;
    this.left = left;
    this.right = right;
    this.operator = operator;
  }

  @Override
  public String toString() {
    return String.format("%s %s %s", left.toString(), operator, right.toString());
  }

  @Override
  public void print() {
    System.out.println("binary expr: ");
  }
}
