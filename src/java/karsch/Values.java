package karsch;

import java.util.HashMap;

import karsch.level.LevelMap;
import karsch.states.CreditsGameState;
import karsch.states.HUDGameState;
import karsch.states.LevelGameState;
import karsch.states.MenuGameState;

import com.jme3.asset.AssetManager;
import com.jme3.post.FilterPostProcessor;
import com.jme3.system.AppSettings;

public class Values {
	private static Values instance;

	private HUDGameState hudGameState;
	private LevelGameState levelGameState;
	private FilterPostProcessor filterPostProcessor;
	// private LoadGameState loadingGS;
	// private LevelPass levelPass;
	// private HudPass hudPass;
	private AppSettings settings;
	private final HashMap<Integer, FilterPostProcessor> pmGameStates;
	private final HashMap<Integer, LevelGameState> levels;
	private boolean pause = false;
	private int babies = 0;
	private int keys = 0;
	private MenuGameState menu;
	private LevelMap levelMap;
	private final float speed = 2;
	private CreditsGameState credits;
	private AssetManager assetManager;

	private Values() {
		pmGameStates = new HashMap<Integer, FilterPostProcessor>(3);
		levels = new HashMap<Integer, LevelGameState>(3);
	}

	public static Values getInstance() {
		if (instance == null)
			instance = new Values();
		return instance;
	}

	public void setAssetManager(final AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	// public SceneMonitor getMonitor(){
	// return SceneMonitor.getMonitor();
	// }

	public HUDGameState getHudState() {
		return hudGameState;
	}

	public void setHudState(final HUDGameState hudState) {
		this.hudGameState = hudState;
	}

	public LevelGameState getLevelGameState() {
		return levelGameState;
	}

	public LevelGameState getLevelGameState(final int number) {
		return levels.get(number);
	}

	public FilterPostProcessor getPManagerGameState(final int number) {
		return pmGameStates.get(number);
	}

	public void setLevelGameState(final int number,
			final LevelGameState levelGameState) {
		this.levelGameState = levelGameState;
		if (!levels.containsKey(number)) {
			levels.put(number, levelGameState);
		}
	}

	public FilterPostProcessor getPManagerGameState() {
		return filterPostProcessor;
	}

	public void setPManagerGameState(final int number,
			final FilterPostProcessor managerGameState) {
		filterPostProcessor = managerGameState;
		if (!pmGameStates.containsKey(number)) {
			pmGameStates.put(number, managerGameState);
		}
		System.out.println("stored levels " + pmGameStates.size());
	}

	// public LevelPass getLevelPass() {
	// return levelPass;
	// }
	//
	// public void setLevelPass(final LevelPass levelPass) {
	// this.levelPass = levelPass;
	// }
	//
	// public HudPass getHudPass() {
	// return hudPass;
	// }
	//
	// public void setHudPass(final HudPass hudPass) {
	// this.hudPass = hudPass;
	// }

	public HashMap<Integer, FilterPostProcessor> getPmGameStates() {
		return pmGameStates;
	}

	public HashMap<Integer, LevelGameState> getLevels() {
		return levels;
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(final boolean pause) {
		this.pause = pause;
	}

	public int getBabies() {
		return babies;
	}

	public void setBabies(final int babies) {
		this.babies = babies;
	}

	public AppSettings getSettings() {
		return settings;
	}

	public void setSettings(final AppSettings settings) {
		this.settings = settings;
	}

	public MenuGameState getMenu() {
		return menu;
	}

	public void setMenu(final MenuGameState menu) {
		this.menu = menu;
	}

	public LevelMap getLevelMap() {
		return levelMap;
	}

	public void setLevelMap(final LevelMap levelMap) {
		this.levelMap = levelMap;
	}

	public int getKeys() {
		return keys;
	}

	public void setKeys(final int keys) {
		this.keys = keys;
	}

	// public LoadGameState getLoadingGS() {
	// return loadingGS;
	// }
	//
	// public void setLoadingGS(final LoadGameState loadingGS) {
	// this.loadingGS = loadingGS;
	// }

	public float getSpeed() {
		return speed;
	}

	public CreditsGameState getCredits() {
		return credits;
	}

	public void setCredits(final CreditsGameState credits) {
		this.credits = credits;
	}
}
