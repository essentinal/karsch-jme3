package karsch.characters;

import karsch.Values;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

// TODO check dest and source function
public class Shadow extends Geometry {
	public Shadow(final float sizeX, final float sizeY) {
		super("shadow", new Box(new Vector3f(0f, .2f, 0f), sizeX, .0f, sizeY));

		final AssetManager assetManager = Values.getInstance().getAssetManager();
		final Material mat_tt = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat_tt.setTexture("ColorMap", assetManager.loadTexture("SHADOW_ROUND.PNG"));
		mat_tt.getAdditionalRenderState().setBlendMode(BlendMode.Alpha); // activate
																																			// transparency
		setMaterial(mat_tt);
		setQueueBucket(Bucket.Transparent);

		// create new Blendstate
		// final BlendState as = DisplaySystem.getDisplaySystem().getRenderer()
		// .createBlendState();
		// as.setBlendEnabled(true);
		// as.setSourceFunction(BlendState.SourceFunction.ConstantAlpha);
		// as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		// as.setTestEnabled(true);
		// as.setTestFunction(BlendState.TestFunction.GreaterThan);
		//
		// as.setEnabled(true);
		//
		// final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
		// .createTextureState();
		// ts.setTexture(TextureManager.loadTexture(ResourceLocatorTool
		// .locateResource(ResourceLocatorTool.TYPE_TEXTURE, "SHADOW_ROUND.PNG"),
		// Texture.MinificationFilter.BilinearNearestMipMap,
		// Texture.MagnificationFilter.Bilinear));
		// ts.setEnabled(true);
		//
		// setRenderState(as);
		// setRenderState(ts);
		// setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		//
		// lockBounds();
		// lockShadows();
	}
}
