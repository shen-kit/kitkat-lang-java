package frontend.ast;

import java.util.List;

public class Program extends Stmt {
	public List<Stmt> body;

	public Program(List<Stmt> body) {
		this.type = NodeType.PROGRAM;
		this.body = body;
	}

	@Override
	public String toString() {
		String res = "";
		for (Stmt s : body) {
			res += s.toString() + "\n";
		}
		return res;
	}
}
