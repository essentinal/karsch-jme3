package karsch.controller;

import karsch.characters.CharacterBase;
import karsch.interfaces.Collectable;
import karsch.level.LevelMap;
import karsch.level.tiles.Fence;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

// TODO add animation control
@SuppressWarnings("serial")
public class Cow2Controller extends TimedControl {
	private final CharacterBase character;
	private float time = 0; // this is not TIME, is range to move
	private float rpf = 0; // range per frame
	private int direction = DIRECTION_NODIR;
	protected LevelMap levelMap;
	protected int x, y;
	private Object actField;

	private int lastDirection;

	private float angle = 0, lastAngle = 0;

	// protected SpatialTransformer trans;
	protected Object trans;

	public static final int DIRECTION_NODIR = -1;
	public static final int DIRECTION_RIGHT = 0;
	public static final int DIRECTION_LEFT = 1;

	protected boolean run = false;

	public Cow2Controller(final CharacterBase character, final Object trans,
			final int x, final int y) {
		this.character = character;
		this.levelMap = character.getLevelMap();
		this.trans = trans;

		this.x = x;
		this.y = y;

		setMinTime(0);
		setMaxTime(5);
		setEnabled(false);
		actField = levelMap.getLevelMap()[x][y];
	}

	private void checkActField() {
		if (actField instanceof Collectable) {
			((Collectable) actField).collect(character);
		}
	}

	@Override
	public void controlUpdate(final float tpf) {

		if (time == getMinTime()) {
			moveAround();
			checkRotation();
		}
		if (run) {
			rpf = 5f * tpf / getSpeed();
			if (time + rpf <= getMaxTime()) {
				rotate();
			} else {
				rpf = getMaxTime() - time;
			}

			time += rpf;

			move();

			if (time >= getMaxTime()) {
				time = getMinTime();
				checkActField();
				lastDirection = direction;
			}
		}
	}

	private void startRun() {
		if (!run) {
			time = getMinTime();
			setRun(true);
		}
	}

	public void setRun(final boolean run) {
		this.run = run;

		if (run) {
			if (direction > DIRECTION_NODIR) {
				// trans.setEnabled(true);
			}
		} else {
			// trans.setEnabled(false);
		}
	}

	private void checkRotation() {
		if (lastDirection == DIRECTION_RIGHT)
			lastAngle = 90;
		else if (lastDirection == DIRECTION_LEFT)
			lastAngle = 270;

		if (direction == DIRECTION_RIGHT)
			angle = 90;
		else if (direction == DIRECTION_LEFT)
			angle = 270;
	}

	private void rotate() {
		if (time >= getMaxTime() / 2)
			return;

		final float percent = time * 2 / (getMaxTime());
		final float newAngle = FastMath.DEG_TO_RAD
				* FastMath.interpolateLinear(percent, lastAngle, angle);

		character.getLocalRotation().fromAngleNormalAxis(newAngle, Vector3f.UNIT_Y);
	}

	private void move() {
		if (direction == DIRECTION_RIGHT) {
			character.getLocalTranslation().x += rpf;
		} else if (direction == DIRECTION_LEFT) {
			character.getLocalTranslation().x -= rpf;
		}
	}

	private void moveAround() {
		if (direction == DIRECTION_NODIR) {
			if ((levelMap.getLevelMap().length > 0)
					&& ((levelMap.getLevelMap()[x - 1][y] instanceof Fence))) {
				direction = DIRECTION_RIGHT;
			} else {
				direction = DIRECTION_LEFT;
			}
		}
		if (direction == DIRECTION_RIGHT) {
			if ((levelMap.getLevelMap().length > x + 1)
					&& (!(levelMap.getLevelMap()[x + 1][y] instanceof Fence))) {
				levelMap.getLevelMap()[x][y] = actField;
				x++;
				actField = levelMap.getLevelMap()[x][y];
				levelMap.getLevelMap()[x][y] = character;

				// trans.setActive(true);
				direction = DIRECTION_RIGHT;

				startRun();
			} else {
				levelMap.getLevelMap()[x][y] = actField;
				x--;
				actField = levelMap.getLevelMap()[x][y];
				levelMap.getLevelMap()[x][y] = character;

				// trans.setActive(true);
				direction = DIRECTION_LEFT;

				startRun();
			}
		} else if (direction == DIRECTION_LEFT) {
			if ((levelMap.getLevelMap().length > x - 1)
					&& (!(levelMap.getLevelMap()[x - 1][y] instanceof Fence))) {
				levelMap.getLevelMap()[x][y] = actField;
				x--;
				actField = levelMap.getLevelMap()[x][y];
				levelMap.getLevelMap()[x][y] = character;

				// trans.setActive(true);
				direction = DIRECTION_LEFT;

				startRun();
			} else {
				levelMap.getLevelMap()[x][y] = actField;
				x++;
				actField = levelMap.getLevelMap()[x][y];
				levelMap.getLevelMap()[x][y] = character;

				// trans.setActive(true);
				direction = DIRECTION_RIGHT;

				startRun();
			}
		}
	}
}