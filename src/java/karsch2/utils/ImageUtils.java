package karsch2.utils;

import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public final class ImageUtils {

  public static ConvolveOp createGaussianBlurFilter(final int hSize,
      final int vSize) {

    final int size = hSize * vSize;

    final float[] data = new float[size];

    for (int i = 0; i < data.length; i++) {
      data[i] = 1f / 9f;
    }

    final Kernel kernel = new Kernel(hSize, vSize, data);

    return new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
  }

}
