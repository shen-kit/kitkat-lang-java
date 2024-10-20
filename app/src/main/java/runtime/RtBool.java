package runtime;

public class RtBool extends RtVal {

	public boolean val;

	public RtBool(boolean val) {
		this.type = RuntimeType.BOOLEAN;
		this.val = val;
	}

	@Override
	public String toString() {
		return val ? "true" : "false";
	}
}
