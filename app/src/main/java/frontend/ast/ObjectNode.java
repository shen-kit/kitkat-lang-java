package frontend.ast;

import java.util.List;

public class ObjectNode extends ExprNode {

	public List<PropertyNode> properties;

	public ObjectNode(List<PropertyNode> properties) {
		this.type = NodeType.OBJECT;
		this.properties = properties;
	}

	@Override
	public String toString() {
		String res = "{\n";
		for (PropertyNode p : properties) {
			res += p.toString() + '\n';
		}
		return res + '}';
	}

}
