package frontend.ast;

public abstract class Stmt {
	protected NodeType type;

	public NodeType getType() {
		return this.type;
	}

	@Override
	public abstract String toString();
}
