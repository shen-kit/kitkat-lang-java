package frontend.ast;

public abstract class StatementNode {
	protected NodeType type;

	public NodeType getType() {
		return this.type;
	}

	@Override
	public abstract String toString();
}
