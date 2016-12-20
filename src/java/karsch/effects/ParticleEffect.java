package karsch.effects;


// TODO check how particles are done in JME3
@SuppressWarnings("serial")
public class ParticleEffect
// extends ParticleMesh
{
	// private final ParticleController controller;
	// private final ParticleMesh particles;
	// private final TimeController timeController;
	//
	// public ParticleEffect(final float time, final String image, final int
	// number,
	// final Vector3f position, final float degrees) {
	// super("particles", number, ParticleType.Quad);
	// particles = this;
	// controller = new ParticleController(this);
	// controller.setActive(false);
	// controller.setSpeed(0.1f);
	// controller.setControlFlow(true);
	// addController(controller);
	//
	// final DisplaySystem display = DisplaySystem.getDisplaySystem();
	//
	// final TextureState ts = display.getRenderer().createTextureState();
	//
	// ts.setTexture(TextureCache.getInstance().getTexture(image));
	// ts.setEnabled(true);
	//
	// final BlendState as1 = display.getRenderer().createBlendState();
	// as1.setBlendEnabled(true);
	// as1.setConstantColor(new ColorRGBA(.5f, .5f, .5f, .5f));
	// as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
	// as1.setDestinationFunction(BlendState.DestinationFunction.One);
	//
	// as1.setTestEnabled(true);
	// as1.setTestFunction(BlendState.TestFunction.GreaterThan);
	// as1.setEnabled(true);
	// as1.setBlendEnabled(true);
	//
	// setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
	// setMaximumAngle(FastMath.DEG_TO_RAD * degrees);
	//
	// setMinimumLifeTime(10.0f);
	// setMaximumLifeTime(50.0f);
	// setStartSize(.5f);
	// setEndSize(2f);
	//
	// setStartColor(new ColorRGBA(1.0f, 0.612f, 0.121f, 0.9f));
	// setEndColor(new ColorRGBA(1.0f, 0.612f, 0.121f, 0.0f));
	//
	// setInitialVelocity(0.10f);
	//
	// setLightCombineMode(LightCombineMode.Off);
	// setTextureCombineMode(TextureCombineMode.Replace);
	// setParticlesInWorldCoords(true);
	//
	// final ZBufferState zstate = display.getRenderer().createZBufferState();
	// zstate.setEnabled(true);
	// zstate.setWritable(false);
	//
	// setRenderState(ts);
	// setRenderState(as1);
	// setRenderState(zstate);
	// updateRenderState();
	//
	// setModelBound(new BoundingBox());
	// updateModelBound();
	//
	// setLocalTranslation(position);
	//
	// timeController = new TimeController(time);
	//
	// addController(timeController);
	// }
	//
	// public ParticleEffect(final float time, final String image,
	// final Vector3f position) {
	// this(time, image, 150, position, 45);
	// }
	//
	// public ParticleEffect(final float time, final String image,
	// final float degrees) {
	// this(time, image, 150, new Vector3f(0, 4, 0), degrees);
	// }
	//
	// public ParticleEffect(final float time, final String image) {
	// this(time, image, 150, new Vector3f(0, 4, 0), 45);
	// }
	//
	// public void setActive(final boolean active) {
	// if (active) {
	// Values.getInstance().getLevelGameState().getKarsch()
	// .attachChild(particles);
	// }
	// controller.setActive(active);
	// timeController.setActive(active);
	// }
	//
	// public class TimeController extends Controller {
	// private float time = 0;
	//
	// public TimeController(final float maxTime) {
	// setRepeatType(RT_CLAMP);
	// setMinTime(time);
	// setMaxTime(maxTime);
	// setActive(false);
	// }
	//
	// @Override
	// public void update(final float tpf) {
	// time += tpf * getSpeed();
	//
	// if (time >= getMaxTime() / 2) {
	// particles.setReleaseRate(0);
	// }
	// if (time >= getMaxTime()) {
	// Values.getInstance().getLevelGameState().getKarsch()
	// .detachChild(particles);
	//
	// try {
	// finalize();
	// } catch (final Throwable e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
}
