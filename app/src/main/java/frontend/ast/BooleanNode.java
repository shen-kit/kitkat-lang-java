package frontend.ast;

public class BooleanNode extends ExprNode {

	public boolean val;

	public BooleanNode(boolean val) {
		this.type = NodeType.BOOLEAN;
		this.val = val;
	}

	@Override
	public String toString() {
		return "boolean: " + (val ? "true" : "false");
	}
}
