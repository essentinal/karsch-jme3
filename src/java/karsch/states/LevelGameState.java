package karsch.states;

import jme3test.app.state.RootNodeState;
import karsch.KarschSimpleGame;
import karsch.Values;
import karsch.characters.Karsch;
import karsch.controller.FlyCamController;
import karsch.controller.TimedControl;
import karsch.debug.MapViewer;
import karsch.input.KarschInputHandler;
import karsch.items.SwitchRiddle;
import karsch.level.Level;
import karsch.level.Level.LevelStyle;
import karsch.level.LevelFactory;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.LightNode;
import com.jme3.scene.Spatial;

// TODO audiosystem is not available in JME3
// TODO scene monitor is not available
public class LevelGameState extends RootNodeState {
  private Application app;

  private final String name;

  private final Values values;
  private Karsch karsch;
  // private LightNode lightNode;
  private Level level;

  private SwitchRiddle riddle;

  private final int levelNumber;
  // private SceneMonitor monitor;

  private Vector3f levelEntrance, levelExit;

  // settings
  private final Vector3f camPos = new Vector3f(0, 20, 15);

  private boolean pause = true;

  private Camera cam, flycam;

  private ViewPort viewPort;
  private ViewPort flyVp;

  public LevelGameState(final int levelNumber) {

    this.name = "level" + levelNumber;

    this.levelNumber = levelNumber;

    values = Values.getInstance();

  }

  @Override
  public void stateAttached(final AppStateManager stateManager) {
    super.stateAttached(stateManager);

    this.app = stateManager.getApplication();
    this.viewPort = app.getViewPort();
    viewPort.clearScenes();
    viewPort.attachScene(getRootNode());
    this.cam = viewPort.getCamera();

    getRootNode().setCullHint(Spatial.CullHint.Dynamic);

    final int camWidth = app.getCamera().getWidth();
    final int camHeight = app.getCamera().getHeight();
    final float aspect = camWidth / (float) camHeight;

    System.out.println("aspect: " + aspect);

    cam.setFrustumPerspective(65f, aspect, 0.5f, 1000f);

    flycam = new Camera(camWidth, camHeight);
    flycam.setFrustumPerspective(65f, aspect, 0.5f, 1000f);
    flyVp = new ViewPort("Flycam viewport", flycam);

    values.setLevelGameState(levelNumber, this);

    loadLevel();

    app.getContext().setTitle("Karsch Level " + levelNumber);

    app.getRenderManager().setCamera(cam, false);
    if (level != null && level.getLevelMap() != null && KarschSimpleGame.DEBUG) {
      MapViewer.getInstance().setLevelMap(level.getLevelMap());
      MapViewer.getInstance().update();
    }

    setPause(false);

    // System.out.println("levelgamestate attached");
  }

  @Override
  public void update(final float tpf) {
    super.update(tpf);

    if (!pause) {
      cam.setLocation(karsch.getLocalTranslation().add(camPos));
      cam.lookAt(karsch.getLocalTranslation(), Vector3f.UNIT_Y);

      // AudioSystem.getSystem().update();
    }

    if (KarschSimpleGame.DEBUG) {

      MapViewer.getInstance().update();
    }

    // System.out.println("levelgamestate update");
  }

  private void loadLevel() {
    level = LevelFactory.getInstance().createLevel(this, levelNumber);
    setLevel(level);
  }

  public void setLevel(final Level level) {
    this.level = level;

    getRootNode().attachChild(level);

    karsch = level.getKarsch();
    getRootNode().attachChild(karsch);

    KarschInputHandler.getInstance().init(app.getInputManager());
    KarschInputHandler.getInstance().setKarsch(karsch);

    cam.setLocation(karsch.getLocalTranslation().add(camPos));
    cam.lookAt(karsch.getLocalTranslation(), Vector3f.UNIT_Y);

    // getRootNode().updateGeometricState();

    // if (KarschSimpleGame.DEBUG) {
    // monitor = values.getMonitor();
    // monitor.registerNode(rootNode);
    // monitor.showViewer(true);
    // }

    setPause(false);
    update(0.01f);
    setPause(true);

    if ((level.getLevelStyle() == LevelStyle.LS_UNDERGRND)
        || (levelEntrance == null || levelExit == null)) {
      app.getRenderManager().setCamera(cam, false);
      final TimeController tc = new TimeController(3);
      getRootNode().addControl(tc);
      setupLight(0.3f);
    } else {
      final Vector3f tmp = levelEntrance.add(levelExit).divide(1.5f);
      tmp.y = 60;

      final Vector3f[] cameraPoints = new Vector3f[] { levelExit, tmp,
          karsch.getLocalTranslation().add(camPos) };

      // Create a path for the camera.
      final MotionPath path = new MotionPath();
      path.setCycle(false);
      for (final Vector3f point : cameraPoints) {
        path.addWayPoint(point);
      }

      path.setCurveTension(0.83f);

      // Create a curve controller to move the CameraNode along the path
      final FlyCamController cc = new FlyCamController(app, path, flycam, cam,
          getRootNode(), karsch);
      cc.start();
      setupLight(1f);
    }
    app.getTimer().reset();
  }

  public void startLevel() {
    setPause(false);
    // TODO reimplement this
    // Values
    // .getInstance()
    // .getHudState()
    // .displayTextTime(
    // Values.getInstance().getLevelGameState().getLevel().getLevelText()
    // .getText("start_txt"), 3000);
  }

  private void setupLight(final float factor) {
    final PointLight l = new PointLight();
    l.setColor(ColorRGBA.White);
    l.setRadius(25f * factor);
    l.setPosition(new Vector3f(0, 5, 0));

    final LightNode lightNode = new LightNode("lightnode", l);
    lightNode.setLocalTranslation(new Vector3f(0, 1, 5));
    lightNode.setLocalTranslation(karsch.getLocalTranslation());

    karsch.attachChild(lightNode);

    final PointLight l2 = new PointLight();
    l2.setRadius(20f * factor);
    l2.setPosition(new Vector3f(0, 80, 100));
    final LightNode ln2 = new LightNode("lightnode2", l2);
    getRootNode().attachChild(ln2);
    ln2.setLocalTranslation(new Vector3f(100, 8, 100));

    getRootNode().addLight(l);
    getRootNode().addLight(l2);

    final AmbientLight al = new AmbientLight();
    al.setColor(ColorRGBA.White.mult(0.5f));
    getRootNode().addLight(al);

    final DirectionalLight dl = new DirectionalLight();
    dl.setDirection(new Vector3f(5, 15, 5).negate());
    getRootNode().addLight(dl);

  }

  public void setPause(final boolean pause) {
    if (this.pause == pause)
      return;
    this.pause = pause;

    level.setPause(pause);
    karsch.setPause(pause);
    values.setPause(pause);
  }

  public void setPause(final boolean pause, final boolean showGui) {
    // TODO
    // if (pause) {
    // values.getHudState().displayText("Paused");
    // } else {
    // values.getHudState().disableProgressbar();
    // }
    setPause(pause);
  }

  public void setLevelEntrance(final Vector3f levelEntrance) {
    this.levelEntrance = levelEntrance;
  }

  public void setLevelExit(final Vector3f levelExit) {
    this.levelExit = levelExit;
  }

  public Karsch getKarsch() {
    return karsch;
  }

  public Level getLevel() {
    return level;
  }

  public Camera getCam() {
    return cam;
  }

  @SuppressWarnings("serial")
  private class TimeController extends TimedControl {
    float time = 0, targetTime;

    public TimeController(final float maxTime) {
      setMaxTime(maxTime);
      // TODO this should be called by a listener
      // values.getHudPass().setEnabled(false);
    }

    @Override
    public void controlUpdate(final float tpf) {
      if ((time += tpf) > getMaxTime()) {
        setEnabled(false);

        // TODO this should be called by a listener
        // values.getHudPass().setEnabled(true);
        startLevel();
        getRootNode().removeControl(this);

        try {
          finalize();
        } catch (final Throwable e) {
          e.printStackTrace();
        }
      }
    }
  }

  public SwitchRiddle getRiddle() {
    if (riddle == null)
      riddle = new SwitchRiddle();
    return riddle;
  }

}
