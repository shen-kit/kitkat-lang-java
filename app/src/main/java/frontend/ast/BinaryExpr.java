package frontend.ast;

public class BinaryExpr extends ExprNode {
	public ExprNode left;
	public ExprNode right;
	public String operator;

	public BinaryExpr(ExprNode left, ExprNode right, String operator) {
		this.type = NodeType.BINARY_EXPR;
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	@Override
	public String toString() {
		return String.format("binary expr (%s): \n%s\n%s", operator, left.toString(), right.toString());
	}
}
