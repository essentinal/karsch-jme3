package karsch.level.tiles;

import karsch.resources.ModelCache;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class Tree extends Tile {
	public Tree(final int x, final int y, final float scale) {
		super("tree" + x + " " + y, x, y);
		model = ModelCache.loadBin("tree1.j3o");
		model.setLocalScale(.1f);

		final float randX = 3 * 0.5f - FastMath.rand.nextFloat();
		final float randY = 3 * 0.5f - FastMath.rand.nextFloat();
		setLocalTranslation(new Vector3f(x * 5 + 2f + randX, 4, y * 5 + 2f + randY));
		attachChild(model);
		setLocalScale(scale + (0.5f * FastMath.rand.nextFloat()));

		// MaterialState ms =
		// DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		// ms.setDiffuse(new ColorRGBA(.7f,.7f,.7f,1f));
		// ms.setAmbient(new ColorRGBA(.7f,.7f,.7f,1f));
		// ms.setEnabled(true);
		// setRenderState(ms);
		//
		// updateRenderState();

		// //////////
		// TODO workaround because not material set
		// final Material mat = new Material(Values.getInstance().getAssetManager(),
		// "Common/MatDefs/Light/Lighting.j3md");
		//
		// mat.setFloat("Shininess", 5.0f);
		// mat.setBoolean("UseMaterialColors", true);
		// mat.setColor("Ambient", new ColorRGBA(.7f, .7f, .7f, 1f));
		// mat.setColor("Diffuse", new ColorRGBA(.7f, .7f, .7f, 1f));
		// mat.setColor("Specular", ColorRGBA.White);
		// mat.setTexture("DiffuseMap",
		// TextureCache.getInstance().getTexture("TREE1.PNG"));
		//
		// model.setMaterial(mat);
		// ///////////

		lockAll();
	}
}
