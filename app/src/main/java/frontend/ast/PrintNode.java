package frontend.ast;

public class PrintNode extends StatementNode {

	public ExprNode toPrint;

	public PrintNode(ExprNode toPrint) {
		this.type = NodeType.PRINT;
		this.toPrint = toPrint;
	}

	@Override
	public String toString() {
		return "PrintNode: " + toPrint.toString();
	}

}
