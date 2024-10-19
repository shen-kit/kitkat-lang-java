package runtime;

public class RtNull extends RtVal {

	public RtNull() {
		this.type = RuntimeType.NULL;
	}

	@Override
	public String toString() {
		return "null";
	}

}
