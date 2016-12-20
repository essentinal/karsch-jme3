package karsch2.core;

public class AnimMapping {
  private final AnimationType type;
  private final String name;
  private final float speed;

  public AnimMapping(final AnimationType type, final String name,
      final float speed) {
    this.type = type;
    this.name = name;
    this.speed = speed;
  }

  public String getName() {
    return name;
  }

  public float getSpeed() {
    return speed;
  }

  public AnimationType getType() {
    return type;
  }

}
