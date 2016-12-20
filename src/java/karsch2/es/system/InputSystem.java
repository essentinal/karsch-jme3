package karsch2.es.system;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import karsch2.es.component.ChangeLevelComponent;
import karsch2.es.component.LevelComponent;
import karsch2.es.component.MovementTargetComponent;
import karsch2.es.component.PlayerControlComponent;
import karsch2.es.component.PositionComponent;
import karsch2.utils.LevelUtil;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;

import es.core.entity.Entity;
import es.core.entity.EntityManager;
import es.core.system.ESystem;

@SuppressWarnings("unchecked")
public class InputSystem implements ESystem {
  private static final Logger LOGGER = Logger.getLogger(InputSystem.class
      .getName());

  private final InputManager inputManager;

  public InputSystem(final InputManager inputManager) {
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

  @Override
  public void process(final float tpf) {

  }

  private void addAction(final ActionAdapter action) {
    inputManager.addMapping(action.getMappingName(), action.getKeyTrigger());
    inputManager.addListener(action, action.getMappingName());
  }

  private void movePlayerCharacter(final Vector3f dir) {
    final List<Entity> entities = EntityManager.getInstance().getEntities(
        PlayerControlComponent.class);

    switch (entities.size()) {
    case 0:
      LOGGER.log(Level.WARNING, "There is no controllable entity");
      break;
    case 1:
      final Entity playerEntity = entities.get(0);
      final PositionComponent playerPos = playerEntity
          .getComponent(PositionComponent.class);

      final PlayerControlComponent playContr = playerEntity
          .getComponent(PlayerControlComponent.class);

      final MovementTargetComponent playerTargetComp = playerEntity
          .getComponent(MovementTargetComponent.class);

      if (playerTargetComp == null) {
        final float levelScale = LevelUtil.getLevelScale();

        final Vector3f snappedLocation = LevelUtil.snapWorldToLevelGrid(
            playerPos.getLocation().add(dir.mult(levelScale)), levelScale);

        playerEntity.addComponent(new MovementTargetComponent(playerPos
            .getLocation(), snappedLocation, playContr.getSpeed(), false));
      }

      break;
    default:
      LOGGER.log(Level.WARNING, "There are {0} controllable entities",
          entities.size());
      break;
    }

  }

  class KeyUpAction extends ActionAdapter {
    public KeyUpAction() {
      super("UP", KeyInput.KEY_UP);
    }

    @Override
    public void onAnalog(final String name, final float value, final float tpf) {
      movePlayerCharacter(Vector3f.UNIT_Z.negate());
    }
  }

  class KeyDownAction extends ActionAdapter {
    public KeyDownAction() {
      super("DOWN", KeyInput.KEY_DOWN);
    }

    @Override
    public void onAnalog(final String name, final float value, final float tpf) {
      movePlayerCharacter(Vector3f.UNIT_Z);
    }
  }

  class KeyLeftAction extends ActionAdapter {
    public KeyLeftAction() {
      super("LEFT", KeyInput.KEY_LEFT);
    }

    @Override
    public void onAnalog(final String name, final float value, final float tpf) {
      movePlayerCharacter(Vector3f.UNIT_X.negate());
    }
  }

  class KeyRightAction extends ActionAdapter {
    public KeyRightAction() {
      super("RIGHT", KeyInput.KEY_RIGHT);
    }

    @Override
    public void onAnalog(final String name, final float value, final float tpf) {
      movePlayerCharacter(Vector3f.UNIT_X);
    }
  }

  class KeyPauseAction extends ActionAdapter {
    public KeyPauseAction() {
      super("PAUSE", KeyInput.KEY_P);
    }

    @Override
    public void onAction(final String name, final boolean isPressed,
        final float tpf) {
      if (isPressed) {
        // TODO set paused
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
      if (isPressed) {
        // TODO show menu
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
      if (isPressed) {
        // TODO interact
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
        // TODO show help
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
      if (isPressed) {
        final List<Entity> entities = EntityManager.getInstance().getEntities(
            LevelComponent.class);
        if (!entities.isEmpty()) {
          entities.get(0).addComponent(new ChangeLevelComponent(true, false));
        } else {
          LOGGER.log(Level.WARNING, "Components are empty!");
        }
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
      if (isPressed) {
        final List<Entity> entities = EntityManager.getInstance().getEntities(
            LevelComponent.class);
        if (!entities.isEmpty()) {
          entities.get(0).addComponent(new ChangeLevelComponent(false, true));
        } else {
          LOGGER.log(Level.WARNING, "Components are empty!");
        }
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
