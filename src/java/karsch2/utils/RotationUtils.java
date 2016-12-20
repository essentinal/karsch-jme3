package karsch2.utils;

import com.jme3.math.FastMath;

public class RotationUtils {
  public static float diffAngleDeg(final float rotation1, final float rotation2) {
    float diffangle = rotation1 - rotation2 + 180f;
    diffangle = diffangle / 360f;
    diffangle = ((diffangle - FastMath.floor(diffangle)) * 360f) - 180f;
    return diffangle;
  }
}
