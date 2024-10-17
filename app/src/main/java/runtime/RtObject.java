package runtime;

import java.util.Map;

public class RtObject extends RtVal {

	public Map<String, RtVal> props;

	public RtObject(Map<String, RtVal> props) {
		this.type = RuntimeType.OBJECT;
		this.props = props;
	}

	@Override
	public String toString() {
		String res = "{\n";
		for (Map.Entry<String, RtVal> e : props.entrySet()) {
			res += String.format("%s: %s\n", e.getKey(), e.getValue());
		}
		return res + '}';
	}

}
