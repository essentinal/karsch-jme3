package karsch.characters;

import karsch.Values;
import karsch.interfaces.Collectable;
import karsch.interfaces.KarschPassable;
import karsch.level.Freefield;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;
import karsch.sound.SoundManager;

import com.jme3.bounding.BoundingBox;
import com.jme3.scene.Node;

// TODO get the right animation control
// TODO check how particle system works in JME3
public class Pig extends CharacterBase implements KarschPassable, Collectable {
	// private final ParticleEffect particleEffect;

	public Pig(final int x, final int y, final LevelMap levelMap) {
		super(x, y, levelMap, "babypig");
		model = ModelCache.getInstance().get("babypig.3ds");
		// TODO
		// trans = (SpatialTransformer) model.getController(0);
		// trans.setRepeatType(Controller.RT_WRAP);
		// trans.setSpeed(0.5f + FastMath.rand.nextFloat());

		// model.setLocalScale(.004f);
		model.setModelBound(new BoundingBox());
		model.setLocalTranslation(0, .5f, 0);
		model.updateModelBound();

		attachChild(model);
		attachChild(new Shadow(1.5f, 1.5f));

		// TODO
		// particleEffect = new ParticleEffect(5, "HEART.PNG");

		addSound("pig", 1, 3, 5);
	}

	// @Override
	@Override
	public void collect(final Node source) {
		// TODO
		// particleEffect.setActive(true);
		// if (track != null){
		// track.stop();
		// }
		SoundManager.getInstance().playSoundOnce("pig_juhu.ogg");
		final Values values = Values.getInstance();
		values.getLevelGameState().getLevel().getNpcNode().detachChild(this);
		values.getLevelGameState().getKarsch().getController()
				.setActField(new Freefield());
		if (audioController != null) {
			removeControl(audioController);
		}
		int babies = values.getBabies();
		values.setBabies(++babies);

		// TODO
		// values.getHudState().setBabies(babies);
		// values.getHudState().displayTextTime(
		// "You rescued a baby, congratulations!", 2000);
		System.out.println("You rescued a baby, congratulations!");

		final Karsch karsch = values.getLevelGameState().getKarsch();
		karsch.incBabiesFound();
	}

	// @Override
	@Override
	public boolean canPass() {
		return true;
	}
}
