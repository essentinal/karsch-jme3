package karsch.characters;

import karsch.Values;
import karsch.controller.Cow2Controller;
import karsch.interfaces.Collectable;
import karsch.interfaces.KarschPassable;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

// TODO get the right animation control
public class Cow2 extends CharacterBase implements KarschPassable, Collectable {
	private final Cow2Controller cow2controller;

	public Cow2(final int x, final int y, final LevelMap levelMap) {
		super(x, y, levelMap, "cow2");
		model = ModelCache.getInstance().get("cow1.3ds");
		// trans = (SpatialTransformer) model.getController(0);
		// trans.setActive(false);
		// trans.setRepeatType(Controller.RT_WRAP);
		// trans.setMaxTime(19);
		model.setLocalScale(.02f);
		model.setLocalTranslation(new Vector3f(0f, 2.5f, 0));
		model.updateModelBound();

		setLocalTranslation(new Vector3f(x * 5 + 2.5f, 0, y * 5 + 2.5f));
		attachChild(model);

		attachChild(new Shadow(1.5f, 2.5f));

		setModelBound(new BoundingBox());
		updateModelBound();

		cow2controller = new Cow2Controller(this, null, x, y);
		float speed = (FastMath.rand.nextFloat()) * 3;
		if (speed < 0.5f) {
			speed = 0.5f;
		}

		cow2controller.setSpeed(speed / Values.getInstance().getSpeed());

		// trans.setSpeed(-12 * speed * Values.getInstance().getSpeed() + 46);
		addControl(cow2controller);
		addSound("cow", 1, 3, 2);
	}

	// @Override
	@Override
	public void collect(final Node source) {
		Values.getInstance().getLevelGameState().getKarsch().collect(this);
	}

	@Override
	public void setPause(final boolean pause) {
		if (cow2controller != null)
			cow2controller.setEnabled(!pause);
		// if (trans != null && pause)
		// trans.setActive(false);
		// if (track != null) {
		// if (pause) {
		// track.stop();
		// } else {
		// track.play();
		// }
		// }
		if (audioController != null)
			audioController.setEnabled(!pause);
	}

	// @Override
	@Override
	public boolean canPass() {
		return true;
	}
}
