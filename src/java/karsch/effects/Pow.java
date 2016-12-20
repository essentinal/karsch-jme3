package karsch.effects;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.controller.TimedControl;
import karsch.level.LevelManager;
import karsch.resources.ModelCache;
import karsch.resources.TextureCache;
import karsch.sound.SoundManager;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

//TODO source and dest function
@SuppressWarnings("serial")
public class Pow extends Node {
  private final Material mat;
  private final PowController powController;
  private final Quad quad;
  private final Geometry geo;
  private final int type;
  private final Spatial ghost;

  public static final int TYPE_COWPOW = 1;
  public static final int TYPE_SPIKEOUCH = 2;

  public Pow(final Karsch karsch, final int type) {
    super("pow");
    this.type = type;
    quad = new Quad(9, 9);
    geo = new Geometry("pow1", quad);

    mat = new Material(Values.getInstance().getAssetManager(),
        "Common/MatDefs/Light/Lighting.j3md");

    mat.setFloat("Shininess", 0.0f);
    mat.setBoolean("UseMaterialColors", true);
    mat.setBoolean("UseAlpha", true);
    mat.setColor("Ambient", ColorRGBA.Black);
    mat.setColor("Diffuse", ColorRGBA.White);
    mat.setColor("Specular", ColorRGBA.Black);
    mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

    Texture t = null;

    if (type == TYPE_COWPOW) {
      t = TextureCache.getInstance().getTexture("POW1.PNG");
    } else if (type == TYPE_SPIKEOUCH) {
      t = TextureCache.getInstance().getTexture("OUCH1.PNG");
    }

    if (t != null) {
      mat.setTexture("DiffuseMap", t);
    }
    mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha); // activate
    geo.setMaterial(mat);

    // quad.setRenderState(ts);
    //
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
    // ms.setDiffuse(ColorRGBA.White);
    // ms.setAmbient(ColorRGBA.White);
    // quad.setRenderState(ms);
    // quad.setRenderState(zstate);
    //
    // quad.setRenderState(bs);

    attachChild(geo);

    // updateGeometricState(0, false);
    // updateRenderState();

    powController = new PowController();
    geo.addControl(powController);

    // setAlignment(SCREEN_ALIGNED);

    addControl(new BillboardControl());

    setLocalTranslation(0, 3, 0);

    karsch.attachChild(this);

    ghost = ModelCache.getInstance().get("karschghost.3ds");
    ghost.setLocalScale(.008f);
    ghost.setModelBound(new BoundingBox());
    ghost.setLocalTranslation(0, 0, 0);
    ghost.updateModelBound();
    ghost.lookAt(new Vector3f(0, 1, 0), Vector3f.UNIT_Z);
    karsch.attachChild(ghost);
    // karsch.updateRenderState();
  }

  public void setActive() {
    powController.setEnabled(true);

    // TODO deactivation of the gui should be triggered by a listener
    // Values.getInstance().getHudPass().setEnabled(false);

    if (type == TYPE_COWPOW) {
      SoundManager.getInstance().playSoundOnce("cow1.ogg");
      SoundManager.getInstance().playSoundOnce("scream1.ogg");
    } else if (type == TYPE_SPIKEOUCH) {
      SoundManager.getInstance().playSoundOnce("scream2.ogg");
    }
  }

  public class PowController extends TimedControl {
    private float time = 0;

    public PowController() {
      setMinTime(0);
      setMaxTime(5);
      setSpeed(2f);
      setEnabled(false);
    }

    @Override
    public void controlUpdate(final float tpf) {

      time += tpf * getSpeed();
      if (time <= getMaxTime()) {
        if (time < getMaxTime() / 5) {
          final float alpha = 4 * FastMath.sin(time * 5 / getMaxTime());
          mat.setColor("Diffuse", new ColorRGBA(1f, 1f, 1f, alpha));
          setLocalScale(FastMath.sin(time * 10 / getMaxTime()));
        } else {
          Values.getInstance().getLevelGameState().setPause(true);
        }

        ghost.getLocalTranslation().y += tpf * 10;
      } else {
        setEnabled(false);
        LevelManager.getInstance().restartLevel();
      }
    }
  }
}
