package karsch.effects;

import java.awt.Font;

import karsch.Values;
import karsch.controller.TimedControl;
import karsch.resources.TextureCache;
import karsch.utils.TextQuadUtils;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;

@SuppressWarnings("serial")
public class SpeechBalloon extends Node {
	// private MaterialState ms;
	// private ZBufferState zbs;

	private final Material mat;

	private final BalloonController balloonController;
	private final Quad balloonQuad;
	private final Geometry balloonGeo;
	private Geometry speechTextGeo;

	private final TextQuadUtils quadUtils;

	private boolean visible = false;

	// private BlendState bs2;
	public SpeechBalloon() {
		super("speechballoon");
		balloonQuad = new Quad(6f, 3f);
		balloonGeo = new Geometry("balloon", balloonQuad);

		mat = new Material(Values.getInstance().getAssetManager(),
				"Common/MatDefs/Light/Lighting.j3md");

		mat.setTexture("DiffuseMap",
				TextureCache.getInstance().getTexture("SPEECHBALLOON.PNG"));

		mat.setFloat("Shininess", 0.0f);
		mat.setBoolean("UseMaterialColors", true);
		mat.setBoolean("UseAlpha", true);
		mat.setColor("Ambient", ColorRGBA.White);
		mat.setColor("Diffuse", new ColorRGBA(1f, 1f, 1f, 1f));
		mat.setColor("Specular", ColorRGBA.White);
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		mat.getAdditionalRenderState().setDepthTest(false);

		// final Texture t = TextureCache.getInstance()
		// .getTexture("SPEECHBALLOON.PNG");

		// mat = new Material(Values.getInstance().getAssetManager(),
		// "Common/MatDefs/Misc/Unshaded.j3md");
		// mat.setTexture("ColorMap", t);
		// mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

		balloonGeo.setQueueBucket(Bucket.Transparent);

		balloonGeo.setMaterial(mat);

		// final BlendState bs = DisplaySystem.getDisplaySystem().getRenderer()
		// .createBlendState();
		// bs.setBlendEnabled(true);
		// bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		// bs.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		// bs.setTestEnabled(true);
		// bs.setTestFunction(BlendState.TestFunction.GreaterThan);
		// bs.setReference(0.1f);
		//
		// bs.setEnabled(true);
		//
		// final ZBufferState zstate =
		// DisplaySystem.getDisplaySystem().getRenderer()
		// .createZBufferState();
		// zstate.setEnabled(true);
		// zstate.setWritable(true);
		// zstate.setFunction(TestFunction.Always);
		//
		// ms =
		// DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		// ms.setDiffuse(new ColorRGBA(1, 1, 1, 0));
		// ms.setAmbient(ColorRGBA.White);
		// balloonQuad.setRenderState(ms);
		// balloonQuad.setRenderState(zstate);

		// balloonQuad.setRenderState(bs);

		balloonGeo.setModelBound(new BoundingBox());
		balloonGeo.updateModelBound();
		attachChild(balloonGeo);

		balloonController = new BalloonController(this);
		balloonGeo.addControl(balloonController);

		// zbs =
		// DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		// zbs.setFunction(ZBufferState.TestFunction.Always);

		quadUtils = new TextQuadUtils(" test bla bla blubb trira ");
		// quadUtils.setForeground(Color.BLACK);
		quadUtils.setFont(new Font("Helvetica", java.awt.Font.PLAIN, 12));
		quadUtils.setFontResolution(12);
		quadUtils.setMaxLineWidth(300);

		speechTextGeo = quadUtils.getQuad(1f);

		// speechTextQuad.setRenderState(zbs);
		// bs2 = (BlendState) speechTextQuad.getRenderState(RenderState.RS_BLEND);
		// speechTextGeo.updateRenderState();

		speechTextGeo.setCullHint(CullHint.Always);

		attachChild(speechTextGeo);

		setLocalTranslation(0f, 5f, 0f);

		addControl(new BillboardControl());
	}

	public void showText(final String text) {
		if (!balloonController.isEnabled()) {
			balloonController.setEnabled(true);
		}

		if (text == null) {
			if (visible) {
				balloonController.fadeOut();
				visible = false;
			}
		} else {
			detachChild(speechTextGeo);
			quadUtils.setText(text);
			speechTextGeo = quadUtils.getQuad(1f);
			// speechTextQuad.setRenderState(bs2);
			// speechTextQuad.setRenderState(zbs);
			// speechTextGeo.updateLogicalState(0f);

			// TODO donno if this works
			// balloonQuad.resize(speechTextQuad.getWidth() + 0.5f,
			// speechTextQuad.getHeight() + 0.5f);

			balloonQuad.updateGeometry(speechTextGeo.getWorldScale().x + 0.5f,
					speechTextGeo.getWorldScale().y + 0.5f);

			speechTextGeo.updateModelBound();

			attachChild(speechTextGeo);
			updateGeometricState();
			balloonController.fadeIn();
			visible = true;
		}
	}

	public class BalloonController extends TimedControl {
		private float time = 0;
		private boolean fadeIn = false, fadeOut = false;

		public BalloonController(final SpeechBalloon balloon) {
			setMinTime(0);
			setMaxTime(1);
			setSpeed(2f);
			setEnabled(true);
		}

		public void fadeIn() {
			fadeOut = false;
			fadeIn = true;
			time = 0;
			setEnabled(true);
		}

		public void fadeOut() {
			fadeIn = false;
			fadeOut = true;
			time = 0;
		}

		@Override
		public void controlUpdate(final float tpf) {
			time += tpf * getSpeed();
			if (time <= getMaxTime()) {
				if (fadeIn) {
					final float alpha = FastMath.interpolateLinear(time / getMaxTime(),
							0f, 1f);
					mat.setColor("Diffuse", new ColorRGBA(1f, 1f, 1f, alpha));
					speechTextGeo.setCullHint(CullHint.Never);
				}

				if (fadeOut) {
					final float alpha = FastMath.interpolateLinear(time / getMaxTime(),
							1f, 0f);
					mat.setColor("Diffuse", new ColorRGBA(1f, 1f, 1f, alpha));
					speechTextGeo.setCullHint(CullHint.Always);
				}

			} else {
				if (fadeOut) {
					setEnabled(false);
				}
				fadeOut = false;
				fadeIn = false;
			}
		}
	}
}
