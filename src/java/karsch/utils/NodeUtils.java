package karsch.utils;

import com.jme3.animation.AnimControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public final class NodeUtils {
  public static AnimControl findAnimControl(final Spatial parent) {
    final AnimControl animControl = parent.getControl(AnimControl.class);
    if (animControl != null) {
      return animControl;
    }

    if (parent instanceof Node) {
      for (final Spatial s : ((Node) parent).getChildren()) {
        final AnimControl animControl2 = findAnimControl(s);
        if (animControl2 != null) {
          return animControl2;
        }
      }
    }

    return null;
  }
}
