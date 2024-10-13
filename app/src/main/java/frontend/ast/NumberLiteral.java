package frontend.ast;

public class NumberLiteral extends Expr {
	int value;

	public NumberLiteral(int value) {
		this.type = NodeType.NUMBER;
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return String.format("number: %d", this.value);
	}
}
