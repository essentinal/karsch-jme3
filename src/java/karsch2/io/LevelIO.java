package karsch2.io;

import karsch2.io.in.LevelKey;
import karsch2.io.out.XMLLevel;

import com.jme3.asset.AssetManager;

public final class LevelIO {
  private LevelIO() {

  }

  public static XMLLevel loadLevel(final AssetManager assetManager,
      final String fileName) {
    return assetManager.loadAsset(new LevelKey(fileName));
  }

}
