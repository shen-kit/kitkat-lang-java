package runtime;

public class RtNumber extends RtVal {
	int val;

	public RtNumber(int val) {
		this.type = RuntimeType.NUMBER;
		this.val = val;
	}

	@Override
	public String toString() {
		return String.valueOf(val);
	}
}
