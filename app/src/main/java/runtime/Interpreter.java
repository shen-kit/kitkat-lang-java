package runtime;

import frontend.ast.StatementNode;
import frontend.ast.StringNode;
import frontend.ast.VarDeclarationNode;
import frontend.ast.VarAssignmentNode;

import java.util.Map;
import java.util.HashMap;

import frontend.ast.*;

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
    }
    if (expr.operator.equals("+") && left.type == RuntimeType.STRING && right.type == RuntimeType.STRING) {
      String newValue = ((RtString) left).value + ((RtString) right).value;
      return new RtString(newValue);
    }
    if (expr.operator.equals("*") && left.type == RuntimeType.STRING && right.type == RuntimeType.NUMBER) {
      String newValue = "";
      for (int i = 0; i < ((RtNumber) right).val; i++) {
        newValue += ((RtString) left).value;
      }
      return new RtString(newValue);
    }
    return new RtNull();
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

  private RtVal evaluateMember(MemberNode node, Scope scope) {
    RtObject obj = (RtObject) evaluate(node.left, scope);
    IdentifierNode ident = (IdentifierNode) node.right;
    return scope.getVar(obj, ident.symbol);
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

  /**
   * @param node  the node being interpreted (assignee, value)
   * @param scope the scope we are evaluating in
   * @return the assigned value, allows chaining assignments (e.g. x = y = z)
   */
  private RtVal evaluateVarAssignment(VarAssignmentNode node, Scope scope) {
    if (node.assignee.getType() == NodeType.IDENTIFIER) { /* literal variable */
      String varname = ((IdentifierNode) node.assignee).symbol;
      RtVal val = evaluate(node.expr, scope);
      return scope.assignVar(varname, val);
    } else if (node.assignee.getType() == NodeType.MEMBER) {
      MemberNode mNode = (MemberNode) node.assignee;
      // object whose property to change (recursive)
      RtObject obj = (RtObject) evaluate(mNode.left, scope);
      IdentifierNode ident = (IdentifierNode) mNode.right;
      return scope.assignVar(obj, ident.symbol, evaluate(node.expr, scope));
    }
    throw new RuntimeException("Variable assignment has invalid assignee: " + node.assignee.toString());

  }

  private RtObject evaluateObject(ObjectNode node, Scope s) {
    Map<String, RtVal> m = new HashMap<>();
    for (PropertyNode p : node.properties) {
      m.put(p.key, evaluate(p.value, s));
    }
    return new RtObject(m);
  }

  private RtVal evaluatePrintStmt(PrintNode node, Scope s) {
    System.out.println(evaluate(node.toPrint, s).toString());
    return new RtNull();
  }

  public RtVal evaluate(StatementNode s, Scope scope) {
    switch (s.getType()) {
      case NodeType.PROGRAM:
        return evaluateProgram((Program) s, scope);
      case NodeType.VAR_DECLARATION:
        return evaluateVarDeclaration((VarDeclarationNode) s, scope);
      case NodeType.VAR_ASSIGNMENT:
        return evaluateVarAssignment((VarAssignmentNode) s, scope);
      case NodeType.PRINT:
        return evaluatePrintStmt((PrintNode) s, scope);
      case NodeType.OBJECT:
        return evaluateObject((ObjectNode) s, scope);
      case NodeType.BINARY_EXPR:
        return evaluateBinaryExpr((BinaryExpr) s, scope);
      case NodeType.NUMBER:
        return new RtNumber(((NumberNode) s).getValue());
      case NodeType.MEMBER:
        return evaluateMember((MemberNode) s, scope);
      case NodeType.STRING:
        return new RtString(((StringNode) s).value);
      case NodeType.IDENTIFIER:
        return evaluateIdentifier((IdentifierNode) s, scope);

      default:
        throw new RuntimeException("Cannot interpret statement type: " + s.getType());
    }
  }
}
