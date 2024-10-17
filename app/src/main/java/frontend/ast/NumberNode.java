package frontend.ast;

public class NumberNode extends ExprNode {
	int value;

	public NumberNode(int value) {
		this.type = NodeType.NUMBER;
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
}
