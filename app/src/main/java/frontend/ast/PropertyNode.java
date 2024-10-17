package frontend.ast;

public class PropertyNode extends ExprNode {

	public String key;
	public ExprNode value;

	public PropertyNode(String key, ExprNode value) {
		this.type = NodeType.PROPERTY;
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", key, value);
	}

}
