package runtime;

import frontend.ast.Stmt;
import frontend.ast.BinaryExpr;
import frontend.ast.NodeType;
import frontend.ast.NumberLiteral;
import frontend.ast.Program;

public class Interpreter {

	public RtVal evaluateProgram(Program p) {
		RtVal lastProcessed = new RtNull();
		for (Stmt s : p.body) {
			lastProcessed = evaluate(s);
		}
		return lastProcessed;
	}

	public RtVal evaluateBinaryExpr(BinaryExpr expr) {
		RtVal left = evaluate(expr.left);
		RtVal right = evaluate(expr.right);
		if (left.type == RuntimeType.NUMBER && right.type == RuntimeType.NUMBER) {
			return evaluateBinaryNumberExpr(((RtNumber) left).val, ((RtNumber) right).val, expr.operator);
		} else {
			return new RtNull();
		}
	}

	private RtVal evaluateBinaryNumberExpr(int l, int r, String op) {
		if (op.equals("+"))
			return new RtNumber(l + r);
		else if (op.equals("-"))
			return new RtNumber(l - r);
		else if (op.equals("*"))
			return new RtNumber(l * r);
		else if (op.equals("/"))
			return new RtNumber(l / r);
		else if (op.equals("%"))
			return new RtNumber(l % r);
		else {
			throw new RuntimeException("Evaluating BinaryNumberExpression, unrecognised operator: " + op);
		}
	}

	public RtVal evaluate(Stmt s) {
		switch (s.getType()) {
			case NodeType.PROGRAM:
				return evaluateProgram((Program) s);
			case NodeType.BINARY_EXPR:
				return evaluateBinaryExpr((BinaryExpr) s);
			case NodeType.NUMBER:
				return new RtNumber(((NumberLiteral) s).getValue());
			case NodeType.NULL:
				return new RtNull();

			default:
				throw new RuntimeException("Cannot interpret statement type: " + s.getType());
		}
	}

}
