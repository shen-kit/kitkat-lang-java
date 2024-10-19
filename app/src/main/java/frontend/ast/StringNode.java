package frontend.ast;

public class StringNode extends ExprNode {

  public String value;

  public StringNode(String value) {
    this.value = value;
    this.type = NodeType.STRING;
  }

  @Override
  public String toString() {
    return '"' + value + '"';
  }

}
