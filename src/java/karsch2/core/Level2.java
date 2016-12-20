package karsch2.core;

import com.jme3.asset.AssetKey;
import com.jme3.asset.CloneableSmartAsset;

public class Level2 implements CloneableSmartAsset {
  private AssetKey key;

  @Override
  public void setKey(final AssetKey key) {
    this.key = key;
  }

  @Override
  public AssetKey getKey() {
    return key;
  }

  @Override
  public Level2 clone() {
    try {
      return (Level2) super.clone();
    } catch (final CloneNotSupportedException e) {
      e.printStackTrace();
    }

    return null;
  }

}
