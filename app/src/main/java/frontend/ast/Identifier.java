package frontend.ast;

public class Identifier extends Expr {
	public String symbol;

	public Identifier(String symbol) {
		this.type = NodeType.IDENTIFIER;
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return "identifier: " + this.symbol;
	}
}
