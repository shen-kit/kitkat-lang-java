package frontend.ast;

public class MemberNode extends ExprNode {
  public ExprNode parent;
  public ExprNode key;

  public MemberNode(ExprNode parent, ExprNode key) {
    this.type = NodeType.MEMBER;
    this.parent = parent;
    this.key = key;
  }

  @Override
  public String toString() {
    return String.format("%s.%s", parent, key);
  }
}
