package frontend.ast;

public abstract class Stmt {
  NodeType kind;

  public abstract void print();
}
