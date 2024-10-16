package runtime;

import frontend.ast.StatementNode;
import frontend.ast.VarDeclarationNode;
import frontend.ast.BinaryExpr;
import frontend.ast.NodeType;
import frontend.ast.NumberNode;
import frontend.ast.IdentifierNode;
import frontend.ast.Program;

/**
 * 
 */
public class Interpreter {

	public RtVal evaluateProgram(Program p, Scope scope) {
		RtVal lastProcessed = new RtNull();
		for (StatementNode s : p.body) {
			lastProcessed = evaluate(s, scope);
		}
		return lastProcessed;
	}

	public RtVal evaluateBinaryExpr(BinaryExpr expr, Scope scope) {
		RtVal left = evaluate(expr.left, scope);
		RtVal right = evaluate(expr.right, scope);
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

	private RtVal evaluateIdentifier(IdentifierNode identifier, Scope scope) {
		return scope.getVar(identifier.symbol);
	}

	/**
	 * @param node  the node being interpreted (contains varname, value, isConst)
	 * @param scope the scope we are evaluating in
	 * @return null
	 */
	private RtVal evaluateVarDeclaration(VarDeclarationNode node, Scope scope) {
		scope.declareVar(node.varname, evaluate(node.value, scope), node.isConst);
		return new RtNull();
	}

	public RtVal evaluate(StatementNode s, Scope scope) {
		switch (s.getType()) {
			case NodeType.PROGRAM:
				return evaluateProgram((Program) s, scope);
			case NodeType.VAR_DECLARATION:
				return evaluateVarDeclaration((VarDeclarationNode) s, scope);
			case NodeType.BINARY_EXPR:
				return evaluateBinaryExpr((BinaryExpr) s, scope);
			case NodeType.NUMBER:
				return new RtNumber(((NumberNode) s).getValue());
			case NodeType.IDENTIFIER:
				return evaluateIdentifier((IdentifierNode) s, scope);

			default:
				throw new RuntimeException("Cannot interpret statement type: " + s.getType());
		}
	}
}
