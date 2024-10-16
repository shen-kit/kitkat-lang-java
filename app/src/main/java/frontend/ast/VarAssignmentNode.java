package frontend.ast;

public class VarAssignmentNode extends ExprNode {

	public ExprNode assignee;
	public ExprNode expr;

	public VarAssignmentNode(ExprNode assignee, ExprNode expr) {
		this.type = NodeType.VAR_ASSIGNMENT;
		this.assignee = assignee;
		this.expr = expr;
	}

	@Override
	public String toString() {
		return "AssignmentExpr";
	}
}
