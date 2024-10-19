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
		return props.toString();
	}

}
