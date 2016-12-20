package karsch.level;

import java.util.concurrent.Callable;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.input.KarschInputHandler;
import karsch.level.tiles.LevelEntrance;
import karsch.states.HUDGameState;
import karsch.states.LevelGameState;

import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;

public class LevelManager {
	private static LevelManager instance = new LevelManager();

	// TODO
	// protected LoadGameState loading;

	private AppStateManager stateManager;

	private final Values values = Values.getInstance();

	// actual level number, the game starts with 1
	private int levelNumber = 1;

	private LevelManager() {

	}

	public void setAppStateManager(final AppStateManager stateManager) {
		this.stateManager = stateManager;
	}

	public static LevelManager getInstance() {
		return instance;
	}

	public void loadMenu() {
		// TODO
		// if (values.getMenu() == null) {
		// final MenuGameState menu = new MenuGameState();
		// GameStateManager.getInstance().attachChild(menu);
		// menu.setActive(true);
		// values.setMenu(menu);
		// } else {
		// if (Values.getInstance().getPManagerGameState() != null) {
		// Values.getInstance().getPManagerGameState().setActive(false);
		// }
		// GameStateManager.getInstance().activateChildNamed("menu");
		// }
	}

	public void loadCredits() {
		// TODO
		// final HUDGameState hud = values.getHudState();
		// if (hud != null) {
		// hud.setActive(false);
		// final PassManagerGameState actGameState = values.getPManagerGameState();
		// if (actGameState != null) {
		// actGameState.setActive(false);
		// GameStateManager.getInstance().detachChild(actGameState);
		// }
		// values.setLevelGameState(levelNumber, null);
		// values.getPmGameStates().remove(levelNumber);
		// values.getLevels().remove(levelNumber);
		// }
		//
		// loading = new LoadGameState(ResourceLocatorTool.locateResource(
		// ResourceLocatorTool.TYPE_TEXTURE, "LOADINGSCREEN.PNG"));
		// loading.setActive(true);
		// loading.setProgress(0, "Please wait, credits are loading..."); // center
		// the
		// // progress
		// // bar.
		// GameStateManager.getInstance().attachChild(loading);
		// GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE)
		// .setExecuteAll(true);
		// Values.getInstance().setLoadingGS(loading);
		//
		// loading.setProgress(.1f);
		//
		// new Thread() {
		// @Override
		// public void run() {
		// this.setPriority(Thread.NORM_PRIORITY + 1);
		//
		// GameTaskQueueManager.getManager().update(new Callable<Object>() {
		// @Override
		// public Object call() throws Exception {
		//
		// final CreditsGameState credits = new CreditsGameState();
		// GameStateManager.getInstance().attachChild(credits);
		// credits.setActive(true);
		// values.setCredits(credits);
		//
		// loading.setProgress(1f, "Loading done");
		// return null;
		// }
		// });
		// }
		// }.start();
	}

	public void loadFirstLevel() {
		// game state for passmanager
		// final PassManagerGameState pManagerGS = new PassManagerGameState(
		// levelNumber);
		//
		// values.setPManagerGameState(levelNumber, pManagerGS);

		// passes for level and hud
		// final LevelPass levelPass = new LevelPass();
		// values.setLevelPass(levelPass);
		//
		// final HudPass hudPass = new HudPass();
		// values.setHudPass(hudPass);

		// game state for the hud
		final HUDGameState hudGameState = new HUDGameState();
		values.setHudState(hudGameState);
		// hudPass.setState(hudGameState);

		// game state for the level
		final LevelGameState levelGameState = new LevelGameState(levelNumber);

		levelGameState.setEnabled(true);
		stateManager.attach(levelGameState);

		// levelPass.setState(levelGameState);

		// add the passes to the passmanager state
		// pManagerGS.getManager().add(levelPass);
		// pManagerGS.getManager().add(hudPass);
		//
		// GameStateManager.getInstance().attachChild(pManagerGS);
		//
		// pManagerGS.setActive(true);

		printInfo();
	}

	public void loadLevel(final int offset, final LevelEntrance entrance) {
		if ((levelNumber + offset) < 1)
			return;

		final LevelGameState actLevel = values.getLevelGameState();
		final Karsch karsch = actLevel.getKarsch();
		karsch.getController().dontMove();
		actLevel.setPause(true);
		actLevel.setEnabled(false);

		if (entrance != null) {
			karsch.setX(entrance.getX());
			karsch.setY(entrance.getY());
			karsch.setLocalTranslation(new Vector3f(karsch.getX() * 5f + 2.5f, 0,
					karsch.getY() * 5f + 2.5f));
		}

		levelNumber += offset;

		// enqueue this later
		new Thread() {
			@Override
			public void run() {

				stateManager.getApplication().enqueue(new Callable<Object>() {
					@Override
					public Object call() throws Exception {

						// TODO
						// if (values.getPManagerGameState(levelNumber) == null) {
						//
						// loading = new LoadGameState(ResourceLocatorTool.locateResource(
						// ResourceLocatorTool.TYPE_TEXTURE, "LOADINGSCREEN.PNG"));
						// loading.setActive(true);
						// loading.setProgress(0, "Please wait, loading..."); // center the
						// progress
						// // bar.
						// GameStateManager.getInstance().attachChild(loading);
						// GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE)
						// .setExecuteAll(true);
						// Values.getInstance().setLoadingGS(loading);
						//
						// loading.setProgress(.1f);
						// }
						// values.getPManagerGameState().setActive(false);

						final boolean levelExists = (values
								.getPManagerGameState(levelNumber) != null);
						System.out.println("LEVEL EXISTS");

						// game state for passmanager
						// TODO
						// PassManagerGameState pManagerGS;
						// if (levelExists) {
						// pManagerGS = values.getPManagerGameState(levelNumber);
						// } else {
						// pManagerGS = new PassManagerGameState(levelNumber);
						// }
						//
						// if (!levelExists)
						// GameStateManager.getInstance().attachChild(pManagerGS);
						//
						// pManagerGS.setActive(true);
						//
						// values.setPManagerGameState(levelNumber, pManagerGS);
						//
						// // GameStateManager.getInstance().detachAllChildren();
						//
						// // passes for level and hud
						// final LevelPass levelPass = new LevelPass();
						// values.setLevelPass(levelPass);
						// final HudPass hudPass = new HudPass();
						// values.setHudPass(hudPass);
						//
						// // game state for the hud
						// hudPass.setState(values.getHudState());

						// game state for the level
						LevelGameState nextLevel;
						if (levelExists) {
							nextLevel = values.getLevelGameState(levelNumber);
						} else {
							nextLevel = new LevelGameState(levelNumber);
							stateManager.attach(nextLevel);
						}
						values.setLevelGameState(levelNumber, nextLevel);

						// TODO
						// levelPass.setState(nextLevel);

						// if (levelExists)

						nextLevel.setEnabled(true);

						// add the passes to the passmanager state
						// if (!levelExists) {
						// pManagerGS.getManager().add(levelPass);
						// pManagerGS.getManager().add(hudPass);
						// }
						// pManagerGS.setActive(true);

						final Karsch karsch = nextLevel.getKarsch();

						// if (levelOffset < 0)
						// karsch.setLocalTranslation(nextLevel.getLevelExit());
						// else
						// karsch.setLocalTranslation(nextLevel.getLevelEntrance());

						KarschInputHandler.getInstance().setKarsch(karsch);
						printInfo();
						// if (!levelExists)
						// loading.setProgress(1f, "Loading done");
						return null;
					}
				});
			}
		}.start();
	}

	public void restartLevel() {
		// TODO
		// final PassManagerGameState actGameState = values.getPManagerGameState();
		// actGameState.setActive(false);
		// GameStateManager.getInstance().detachChild(actGameState);
		values.getPmGameStates().remove(levelNumber);
		values.getLevels().remove(levelNumber);
		loadLevel(0, null);
	}

	public void unloadCredits() {
		// TODO
		// final FilterPostProcessor actGameState = values.getPManagerGameState();
		// if (actGameState != null) {
		// actGameState.setActive(false);
		// GameStateManager.getInstance().detachChild(actGameState);
		// }
		// values.getPmGameStates().remove(levelNumber);
		// values.getLevels().remove(levelNumber);
		//
		// levelNumber = 0;
		// loadMenu();
		//
		// final CreditsGameState credits = values.getCredits();
		// if (credits != null) {
		// GameStateManager.getInstance().detachChild(credits);
		// values.setCredits(null);
		// }
	}

	public int getLevelNumber() {
		return levelNumber;
	}

	private void printInfo() {

		// if (values.getHudPass() != null) {
		// System.out.print("HUD Pass ");
		// System.out.println("enabled: " + values.getHudPass().isEnabled());
		// }
		// if (values.getHudState() != null) {
		// System.out.print("HUD State ");
		// System.out.println("active: " + values.getHudState().isActive());
		// }
		// if (values.getLevelPass() != null) {
		// System.out.print("Level Pass ");
		// System.out.println("enabled: " + values.getLevelPass().isEnabled());
		// }
		// if (values.getLevelGameState() != null) {
		// System.out.print("Level State " + values.getLevelGameState().getName());
		// System.out.println(" active: " + values.getLevelGameState().isActive());
		// }
		//
		// final GameStateManager gsManager = GameStateManager.getInstance();
		// System.out.println("\n" + gsManager.getQuantity()
		// + " levels in GameStateManager");
		// for (final GameState gs : values.getPmGameStates().values()) {
		// System.out.println(gs.getName() + " active: " + gs.isActive());
		// }
		//
		// for (final GameState gs : values.getLevels().values()) {
		// System.out.println(gs.getName() + " active: " + gs.isActive());
		// }
	}
}
