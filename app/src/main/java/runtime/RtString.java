package runtime;

public class RtString extends RtVal {

  public String value;

  public RtString(String value) {
    this.type = RuntimeType.STRING;
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
