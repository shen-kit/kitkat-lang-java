package frontend.ast;

import java.util.List;

public class Program extends Stmt {
	public List<Stmt> body;

	public Program(List<Stmt> body) {
		this.kind = NodeType.PROGRAM;
		this.body = body;
	}

	@Override
	public void print() {
		System.out.println("Program:");
		for (Stmt s : body) {
			s.print();
		}
	}
}
