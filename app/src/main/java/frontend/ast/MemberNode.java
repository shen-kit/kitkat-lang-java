package frontend.ast;

public class MemberNode extends ExprNode {
	public ExprNode left;
	public IdentifierNode right;

	public MemberNode(ExprNode left, IdentifierNode right) {
		this.type = NodeType.MEMBER;
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return String.format("%s.%s", left, right);
	}
}
