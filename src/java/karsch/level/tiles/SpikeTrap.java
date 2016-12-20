package karsch.level.tiles;

import karsch.Values;
import karsch.characters.CharacterBase;
import karsch.controller.TimedControl;
import karsch.interfaces.Collectable;
import karsch.interfaces.KarschPassable;
import karsch.resources.ModelCache;

import com.jme3.bounding.BoundingBox;
import com.jme3.scene.Node;

// TODO add animation control
public class SpikeTrap extends CharacterBase implements KarschPassable,
		Collectable {
	private final SpikeTrap spikeTrap;
	private final SpikeTrapController spikeTrapController;

	public SpikeTrap(final int x, final int y) {
		super("spiketrap" + x + "" + y, x, y);
		spikeTrap = this;

		model = ModelCache.getInstance().get("spiketrap.3ds");
		model.setLocalScale(.025f);

		// TODO
		// trans = (SpatialTransformer) model.getController(0);
		// trans.setActive(true);
		// trans.setRepeatType(Controller.RT_WRAP);
		// trans.setMaxTime(35f);
		// trans.setSpeed(10f + FastMath.rand.nextInt(10));

		attachChild(model);
		setModelBound(new BoundingBox());
		updateModelBound();
		setLocalTranslation(x * 5f + 2.5f, 0, y * 5 + 2.5f);
		spikeTrapController = new SpikeTrapController();

		addControl(spikeTrapController);
	}

	// @Override
	@Override
	public void collect(final Node source) {
		// TODO
		// if (trans.getCurTime() > 15) {
		Values.getInstance().getLevelGameState().getKarsch().collect(this);
		// }
	}

	@Override
	public void setPause(final boolean pause) {
		super.setPause(pause);
		if (spikeTrapController != null)
			spikeTrapController.setEnabled(!pause);
	}

	class SpikeTrapController extends TimedControl {
		public SpikeTrapController() {
			// setRepeatType(RT_WRAP);
			setEnabled(true);
		}

		@Override
		public void controlUpdate(final float tpf) {

			if (isVisible()) {
				// TODO
				// if (trans.getCurTime() > 15f && trans.getCurTime() < 15.3f) {
				// SoundManager.getInstance().playSoundOnce("spike_out.ogg");
				// } else if (trans.getCurTime() > 30f && trans.getCurTime() < 30.3f) {
				// SoundManager.getInstance().playSoundOnce("spike_in.ogg");
				// }
			}

			final Object karschActfield = Values.getInstance().getLevelGameState()
					.getKarsch().getController().getActField();
			if (// trans.getCurTime() > 15 &&
			karschActfield instanceof SpikeTrap && karschActfield == spikeTrap) {

				Values.getInstance().getLevelGameState().getKarsch().collect(spikeTrap);
			}
		}
	}

	// @Override
	@Override
	public boolean canPass() {
		return true;
	}
}