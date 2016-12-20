package karsch.input;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.level.LevelManager;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;

public class KarschInputHandler {
	private Karsch karsch;
	private InputManager inputManager;
	static KarschInputHandler instance;

	private KarschInputHandler() {
	}

	public static KarschInputHandler getInstance() {
		if (instance == null)
			instance = new KarschInputHandler();
		return instance;
	}

	public void init(final InputManager inputManager) {
		this.inputManager = inputManager;

		addAction(new KeyUpAction());
		addAction(new KeyDownAction());
		addAction(new KeyLeftAction());
		addAction(new KeyRightAction());

		addAction(new KeyPauseAction());
		addAction(new KeyInteractionAction());
		addAction(new KeyMenuAction());
		addAction(new KeyHelpAction());

		// DEBUG ACTIONS
		addAction(new KeyNextAction());
		addAction(new KeyPreviousAction());
	}

	private void addAction(final ActionAdapter action) {
		inputManager.addMapping(action.getMappingName(), action.getKeyTrigger());
		inputManager.addListener(action, action.getMappingName());
	}

	public void setKarsch(final Karsch karsch) {
		this.karsch = karsch;
	}

	class KeyUpAction extends ActionAdapter {
		public KeyUpAction() {
			super("UP", KeyInput.KEY_UP);
		}

		@Override
		public void onAnalog(final String name, final float value, final float tpf) {
			if (karsch != null && !Values.getInstance().isPause()) {
				karsch.moveUp();
			}
		}
	}

	class KeyDownAction extends ActionAdapter {
		public KeyDownAction() {
			super("DOWN", KeyInput.KEY_DOWN);
		}

		@Override
		public void onAnalog(final String name, final float value, final float tpf) {
			if (karsch != null && !Values.getInstance().isPause()) {
				karsch.moveDown();
			}
		}
	}

	class KeyLeftAction extends ActionAdapter {
		public KeyLeftAction() {
			super("LEFT", KeyInput.KEY_LEFT);
		}

		@Override
		public void onAnalog(final String name, final float value, final float tpf) {
			if (karsch != null && !Values.getInstance().isPause()) {
				karsch.moveLeft();
			}
		}
	}

	class KeyRightAction extends ActionAdapter {
		public KeyRightAction() {
			super("RIGHT", KeyInput.KEY_RIGHT);
		}

		@Override
		public void onAnalog(final String name, final float value, final float tpf) {
			if (karsch != null && !Values.getInstance().isPause()) {
				karsch.moveRight();
			}
		}
	}

	class KeyPauseAction extends ActionAdapter {
		public KeyPauseAction() {
			super("PAUSE", KeyInput.KEY_P);
		}

		@Override
		public void onAction(final String name, final boolean isPressed,
				final float tpf) {
			if (karsch != null && !isPressed) {
				final boolean pause = !Values.getInstance().isPause();
				Values.getInstance().getLevelGameState().setPause(pause, true);
			}
		}
	}

	class KeyMenuAction extends ActionAdapter {
		public KeyMenuAction() {
			super("MENU", KeyInput.KEY_ESCAPE);
		}

		@Override
		public void onAction(final String name, final boolean isPressed,
				final float tpf) {
			if (!isPressed) {
				LevelManager.getInstance().loadMenu();
			}
		}
	}

	class KeyInteractionAction extends ActionAdapter {
		public KeyInteractionAction() {
			super("INTERACT", KeyInput.KEY_SPACE);
		}

		@Override
		public void onAction(final String name, final boolean isPressed,
				final float tpf) {
			if (karsch != null && isPressed) {
				karsch.checkInteractions();
			}
		}

	}

	class KeyHelpAction extends ActionAdapter {
		public KeyHelpAction() {
			super("HELP", KeyInput.KEY_F1);
		}

		@Override
		public void onAction(final String name, final boolean isPressed,
				final float tpf) {
			if (isPressed) {
				// TODO
				// Values.getInstance().getHudState().displayHelp();
			}
		}
	}

	// //////////
	// DEBUG ACTIONS

	class KeyNextAction extends ActionAdapter {
		public KeyNextAction() {
			super("NEXT_LVL", KeyInput.KEY_F6);
		}

		@Override
		public void onAction(final String name, final boolean isPressed,
				final float tpf) {
			if (!isPressed) {
				LevelManager.getInstance().loadLevel(1, null);
			}
		}
	}

	class KeyPreviousAction extends ActionAdapter {
		public KeyPreviousAction() {
			super("PREV_LVL", KeyInput.KEY_F5);
		}

		@Override
		public void onAction(final String name, final boolean isPressed,
				final float tpf) {
			if (!isPressed) {
				LevelManager.getInstance().loadLevel(-1, null);
			}
		}
	}

	// //////////

	private static abstract class ActionAdapter implements ActionListener,
			AnalogListener {
		private final String mappingName;
		private final KeyTrigger keyTrigger;

		public ActionAdapter(final String mappingName, final int keyCode) {
			this.mappingName = mappingName;
			this.keyTrigger = new KeyTrigger(keyCode);
		}

		public KeyTrigger getKeyTrigger() {
			return keyTrigger;
		}

		public String getMappingName() {
			return mappingName;
		}

		@Override
		public int hashCode() {
			return (mappingName + "" + keyTrigger.getKeyCode()).hashCode();
		}

		@Override
		public void onAction(final String name, final boolean isPressed,
				final float tpf) {

		}

		@Override
		public void onAnalog(final String name, final float value, final float tpf) {

		}
	}
}
