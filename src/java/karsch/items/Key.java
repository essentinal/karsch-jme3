package karsch.items;

import karsch.Values;
import karsch.characters.CharacterBase;
import karsch.characters.Shadow;
import karsch.controller.TimedControl;
import karsch.interfaces.Collectable;
import karsch.interfaces.KarschPassable;
import karsch.level.Freefield;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;
import karsch.sound.SoundManager;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

//TODO check JME3 implementation of particle effects
@SuppressWarnings("serial")
public class Key extends CharacterBase implements Collectable, KarschPassable {
	// private final ParticleEffect particleEffect;
	private final KeyController keyRot;

	public Key(final int x, final int y, final LevelMap levelMap) {
		super(x, y, levelMap, "key");
		model = ModelCache.getInstance().get("key.3ds");

		model.setLocalScale(.020f);
		model.setModelBound(new BoundingBox());
		model.setLocalTranslation(0, 1.5f, 0);
		model.updateModelBound();

		attachChild(model);
		attachChild(new Shadow(1.0f, 1.0f));

		// particleEffect = new ParticleEffect(5, "STAR1.PNG");

		setLocalTranslation(new Vector3f(x * 5 + 2.5f, 0, y * 5 + 2.5f));

		keyRot = new KeyController(this);
		keyRot.setMaxTime(30);
		keyRot.setSpeed(.1f);
		keyRot.setEnabled(true);
		addControl(keyRot);
	}

	// @Override
	@Override
	public void collect(final Node source) {
		SoundManager.getInstance().playSoundOnce("twinkle.ogg");

		final Values values = Values.getInstance();
		values.getLevelGameState().getKarsch().getController()
				.setActField(new Freefield());
		// TODO
		// values.getHudState().displayTextTime(
		// "You found a key, maybe you can open a door with it", 1000);
		// particleEffect.setActive(true);

		values.getLevelGameState().getLevel().getNpcNode().detachChild(this);

		final int keys = values.getKeys();
		values.setKeys(keys + 1);
		// TODO
		// values.getHudState().setKeys(keys + 1);

		try {
			finalize();
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}

	public class KeyController extends TimedControl {
		private final Key key;
		private float time;

		public KeyController(final Key key) {
			this.key = key;
			// setRepeatType(RT_WRAP);
		}

		@Override
		public void controlUpdate(final float tpf) {
			if ((time += tpf * getSpeed()) <= getMaxTime()) {
				final float rot = FastMath.interpolateLinear(time / getMaxTime(), 0,
						360);
				key.getLocalRotation().fromAngleAxis(rot, Vector3f.UNIT_Y);

			} else {
				time = getMinTime();
			}
		}
	}

	// @Override
	@Override
	public boolean canPass() {
		return true;
	}
}
