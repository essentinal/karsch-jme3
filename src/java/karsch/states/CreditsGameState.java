package karsch.states;


public class CreditsGameState
// extends CameraGameStateDefaultCamera
{
	// private final DisplaySystem display;
	// private final Values values;
	//
	// private LightNode lightNode;
	//
	// private SceneMonitor monitor;
	//
	// private boolean pause = false;
	// private final Camera cam;
	// private Level level;
	//
	// public CreditsGameState() {
	// super("credits");
	//
	// KeyBindingManager.getKeyBindingManager().add("ESCAPE",
	// KeyInput.KEY_ESCAPE);
	//
	// values = Values.getInstance();
	// display = DisplaySystem.getDisplaySystem();
	//
	// display.getRenderer().getQueue().setTwoPassTransparency(false);
	// final CullState cs = display.getRenderer().createCullState();
	// cs.setCullFace(CullState.Face.Back);
	// cs.setEnabled(true);
	//
	// rootNode.setRenderState(cs);
	// rootNode.setCullHint(Spatial.CullHint.Dynamic);
	//
	// cam = display.getRenderer().createCamera(display.getWidth(),
	// display.getHeight());
	// cam.setFrustumPerspective(65, 1.3f, 0.5f, 1000);
	// cam.update();
	//
	// // flycam = display.getRenderer().createCamera(display.getWidth(),
	// // display.getHeight());
	// // flycam.setFrustumPerspective(65, 1.3f, 0.5f, 1000);
	// // flycam.update();
	//
	// init();
	// }
	//
	// @Override
	// protected void onActivate() {
	// DisplaySystem.getDisplaySystem().setTitle("Karsch Credits");
	// display.getRenderer().setCamera(cam);
	//
	// setPause(false);
	// }
	//
	// @Override
	// protected void onDeactivate() {
	// super.onDeactivate();
	// }
	//
	// @Override
	// protected void stateUpdate(final float tpf) {
	// super.stateUpdate(tpf);
	//
	// if (KeyBindingManager.getKeyBindingManager().isValidCommand("ESCAPE")) {
	// LevelManager.getInstance().unloadCredits();
	// SoundManager.getInstance().playMusic();
	// }
	//
	// if (KarschSimpleGame.DEBUG) {
	// if (monitor != null)
	// monitor.updateViewer(tpf);
	//
	// MapViewer.getInstance().update();
	// }
	// }
	//
	// public void init() {
	//
	// SoundManager.getInstance().playCreditMusic();
	// rootNode.addController(new TimeController(107));
	//
	// level = LevelFactory.getInstance().createLevel(null, 8);
	// rootNode.attachChild(level);
	//
	// rootNode.updateGeometricState(0, true);
	// rootNode.updateRenderState();
	//
	// if (KarschSimpleGame.DEBUG) {
	// monitor = values.getMonitor();
	// monitor.registerNode(rootNode);
	// monitor.showViewer(true);
	// }
	//
	// display.getRenderer().setCamera(cam);
	//
	// final CameraNode camNode = new CameraNode("cam", cam);
	// rootNode.attachChild(new Marquee());
	// camNode.lookAt(new Vector3f(0, -0.0f, -1), Vector3f.UNIT_Y);
	// camNode.setLocalTranslation(60, 5, 100);
	// rootNode.attachChild(camNode);
	//
	// // TimeController tc = new TimeController(3);
	// // rootNode.addController(tc);
	// setupLight(0.3f);
	//
	// Timer.getTimer().reset();
	//
	// }
	//
	// private void setupLight(final float factor) {
	// final PointLight l = new PointLight();
	// l.setDiffuse(ColorRGBA.White);
	// l.setShadowCaster(true);
	// l.setEnabled(true);
	// l.setConstant(1 / factor);
	// l.setLocation(new Vector3f(0, 5, 0));
	//
	// lightNode = new LightNode("lightnode");
	// lightNode.setLight(l);
	// lightNode.setLocalTranslation(new Vector3f(0, 1, 5));
	//
	// final PointLight l2 = new PointLight();
	// l2.setEnabled(true);
	// l2.setAttenuate(true);
	// l2.setConstant(1.5f / factor);
	// l2.setLocation(new Vector3f(0, 80, 100));
	//
	// final LightState lightState = DisplaySystem.getDisplaySystem()
	// .getRenderer().createLightState();
	// lightState.attach(l);
	// lightState.attach(l2);
	// rootNode.clearRenderState(RenderState.RS_LIGHT);
	// rootNode.setRenderState(lightState);
	// rootNode.updateRenderState();
	// }
	//
	// public void setPause(final boolean pause) {
	// this.pause = pause;
	//
	// values.setPause(pause);
	// level.setPause(pause);
	// }
	//
	// @SuppressWarnings("serial")
	// private class TimeController extends Controller {
	// float time = 0, targetTime;
	// Controller controller;
	//
	// public TimeController(final float maxTime) {
	// this.controller = this;
	// setMaxTime(maxTime);
	// setActive(true);
	// }
	//
	// @Override
	// public void update(final float tpf) {
	// if ((time += tpf) > getMaxTime()) {
	// LevelManager.getInstance().unloadCredits();
	// SoundManager.getInstance().playMusic();
	// }
	// }
	// }
}
