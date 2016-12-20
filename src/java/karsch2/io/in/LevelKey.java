package karsch2.io.in;

import karsch2.io.out.XMLLevel;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetProcessor;
import com.jme3.asset.CloneableAssetProcessor;
import com.jme3.asset.cache.AssetCache;
import com.jme3.asset.cache.WeakRefCloneAssetCache;

public class LevelKey extends AssetKey<XMLLevel> {

  public LevelKey(final String name) {
    super(name);
  }

  public LevelKey() {
    super();
  }

  @Override
  public Class<? extends AssetCache> getCacheType() {
    return WeakRefCloneAssetCache.class;
  }

  @Override
  public Class<? extends AssetProcessor> getProcessorType() {
    return CloneableAssetProcessor.class;
  }

}
