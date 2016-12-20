package karsch2.io.out;

import java.io.File;
import java.io.IOException;

import com.jme3.export.binary.BinaryExporter;
import com.jme3.scene.Spatial;

public class J3OHelper {
  public static void save(final Spatial s, final String name) {
    final BinaryExporter ex = new BinaryExporter();

    final File f = new File("Assets/model/bin/" + name + ".j3o");
    f.getParentFile().mkdirs();

    try {
      ex.save(s, f);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
