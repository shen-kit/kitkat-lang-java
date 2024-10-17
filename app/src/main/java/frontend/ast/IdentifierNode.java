package frontend.ast;

public class IdentifierNode extends ExprNode {
	public String symbol;

	public IdentifierNode(String symbol) {
		this.type = NodeType.IDENTIFIER;
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}
}
