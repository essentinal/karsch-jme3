package karsch.utils;

import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class MaterialUtil {
	public static void changeMaterial(final Spatial s) {
		if (s instanceof Geometry) {
			final Material m = ((Geometry) s).getMaterial();
			for (final MatParam mp : m.getParams()) {
				try {
					System.out.println(mp);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}

		} else if (s instanceof Node) {
			for (final Spatial child : ((Node) s).getChildren()) {
				changeMaterial(child);
			}
		}
	}
}
