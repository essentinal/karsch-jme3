package karsch.effects;

import karsch.Values;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

// TODO source and dest function
@SuppressWarnings("serial")
public class FogOfWar extends Node {
	public FogOfWar() {
		final Box box = new Box(new Vector3f(0, 7, 0), 70, 0, 70);
		final Geometry geo = new Geometry("fogofwar", box);

		final Material mat_tt = new Material(
				Values.getInstance().getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat_tt.setTexture("ColorMap", Values.getInstance().getAssetManager()
				.loadTexture("FOGOFWAR.PNG"));
		mat_tt.getAdditionalRenderState().setBlendMode(BlendMode.Alpha); // activate
		geo.setMaterial(mat_tt);

		// final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
		// .createTextureState();
		//
		// ts.setTexture(TextureCache.getInstance().getTexture("FOGOFWAR.PNG"), 0);
		// quad.setRenderState(ts);
		//
		// final BlendState bs = DisplaySystem.getDisplaySystem().getRenderer()
		// .createBlendState();
		// bs.setBlendEnabled(true);
		// bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		// bs.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		// bs.setTestEnabled(true);
		// bs.setTestFunction(BlendState.TestFunction.GreaterThan);
		// // bs.setReference(0.1f);
		//
		// bs.setEnabled(true);
		//
		// quad.setRenderState(bs);
		// quad.updateRenderState();

		attachChild(geo);
	}
}
