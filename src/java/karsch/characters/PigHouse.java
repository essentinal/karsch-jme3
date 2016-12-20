package karsch.characters;

import karsch.Values;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

// TODO get the right animation control
public class PigHouse extends CharacterBase {

	public PigHouse(final int x, final int y, final LevelMap levelMap) {
		super(x, y, levelMap, "babypig");
		model = ModelCache.getInstance().get("babypig.3ds");
		// trans = (SpatialTransformer) model.getController(0);
		// trans.setRepeatType(Controller.RT_WRAP);
		// trans.setSpeed(0.5f + FastMath.rand.nextFloat());

		model.setLocalScale(.004f);
		model.setModelBound(new BoundingBox());
		model.setLocalTranslation(0, .5f, 0);
		model.updateModelBound();

		// //////////
		// TODO workaround because not material set
		final Material mat = new Material(Values.getInstance().getAssetManager(),
				"Common/MatDefs/Light/Lighting.j3md");

		mat.setFloat("Shininess", 0.0f);
		mat.setBoolean("UseMaterialColors", true);
		mat.setColor("Ambient", new ColorRGBA(.7f, .7f, .7f, 1f));
		mat.setColor("Diffuse", new ColorRGBA(.7f, .7f, .7f, 1f));
		mat.setColor("Specular", ColorRGBA.Black);

		model.setMaterial(mat);
		// ///////////

		attachChild(model);
		attachChild(new Shadow(1.5f, 1.5f));
	}

}
