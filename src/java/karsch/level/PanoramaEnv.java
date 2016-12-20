package karsch.level;

import karsch.Values;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

// TODO check if this works
// TODO maybe replace with skyfactory
public class PanoramaEnv extends Node {
	public PanoramaEnv(final int x, final int y, final float radius,
			final int samples) {
		super("environment");

		final Sphere sp = new Sphere(samples, samples, radius);
		attachChild(new Geometry("Sky", sp));

		// final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
		// .createTextureState();
		//
		// ts.setTexture(TextureManager.loadTexture(ResourceLocatorTool
		// .locateResource(ResourceLocatorTool.TYPE_TEXTURE, "ENV_FOREST.PNG"),
		// Texture.MinificationFilter.BilinearNearestMipMap,
		// Texture.MagnificationFilter.Bilinear), 0);
		//
		// ts.setEnabled(true);
		// setRenderState(ts);
		//
		// final MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
		// .createMaterialState();
		// ms.setDiffuse(ColorRGBA.White);
		// ms.setAmbient(ColorRGBA.White);
		// ms.setEmissive(ColorRGBA.White);
		// ms.setSpecular(ColorRGBA.White);
		// ms.setEnabled(true);
		//
		// setRenderState(ms);
		//
		// final CullState cs = DisplaySystem.getDisplaySystem().getRenderer()
		// .createCullState();
		// cs.setCullFace(CullState.Face.Front);
		// cs.setEnabled(true);
		// setRenderState(cs);
		//
		// updateRenderState();

		final Material mat_tt = new Material(
				Values.getInstance().getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat_tt.setTexture("ColorMap", Values.getInstance().getAssetManager()
				.loadTexture("ENV_FOREST.PNG"));

		setCullHint(Spatial.CullHint.Never);

		setMaterial(mat_tt);

		setModelBound(new BoundingBox());
		updateModelBound();
		getLocalRotation()
				.fromAngleAxis(FastMath.DEG_TO_RAD * -90, Vector3f.UNIT_X);
		getLocalScale().z = .5f;

		setLocalTranslation(x * 5, -10f, y * 5);

		// lockBounds();
		// lockShadows();
		// lockBranch();
	}
}
