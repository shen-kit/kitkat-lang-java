package frontend.ast;

public class NullLiteral extends Expr {

	public NullLiteral() {
		this.type = NodeType.NULL;
	}

	@Override
	public String toString() {
		return "null";
	}
}
