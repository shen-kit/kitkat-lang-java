package frontend.ast;

public class VarDeclarationNode extends StatementNode {

	public String varname;
	public boolean isConst;
	public ExprNode value;

	public VarDeclarationNode(String varname, ExprNode value, boolean isConst) {
		this.type = NodeType.VAR_DECLARATION;
		this.varname = varname;
		this.value = value;
		this.isConst = isConst;
	}

	@Override
	public String toString() {
		return "VarDeclarationNode";
	}

}
