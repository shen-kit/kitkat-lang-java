package frontend.ast;

public class ComparatorNode extends ExprNode {
  public ExprNode left;
  public ExprNode right;
  public String operator;

  public ComparatorNode(ExprNode left, ExprNode right, String operator) {
    this.type = NodeType.COMPARATOR_EXPR;
    this.left = left;
    this.right = right;
    this.operator = operator;
  }

  @Override
  public String toString() {
    return String.format("comparison (%s): \n%s\n%s", operator, left.toString(), right.toString());
  }
}
