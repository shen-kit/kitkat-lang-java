package frontend.ast;

public class BinaryExpr extends Expr {
	public Expr left;
	public Expr right;
	public String operator;

	public BinaryExpr(Expr left, Expr right, String operator) {
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
