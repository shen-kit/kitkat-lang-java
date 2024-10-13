package frontend.ast;

public class BooleanLiteral extends Expr {

  public boolean val;

  public BooleanLiteral(boolean val) {
    this.type = NodeType.BOOLEAN;
    this.val = val;
  }

  @Override
  public String toString() {
    return "boolean: " + (val ? "true" : "false");
  }
}
