package frontend.ast;

import java.util.List;

public class Program extends StatementNode {
	public List<StatementNode> body;

	public Program(List<StatementNode> body) {
		this.type = NodeType.PROGRAM;
		this.body = body;
	}

	@Override
	public String toString() {
		String res = "";
		for (StatementNode s : body) {
			res += s.toString() + "\n";
		}
		return res;
	}
}
