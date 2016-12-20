package karsch2.io.in;

import java.io.IOException;
import java.io.InputStream;

import karsch2.io.out.XMLLevel;
import karsch2.utils.XmlUtils;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;

public class LevelLoader implements AssetLoader {

  @Override
  public Object load(final AssetInfo assetInfo) throws IOException {
    final SAXBuilder builder = XmlUtils.getSaxBuilder();

    final InputStream is = assetInfo.openStream();

    XMLLevel level = null;

    try {
      final Element elem = builder.build(is).getRootElement();
      level = new XMLLevel();
      level.parseXml(elem);
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      is.close();
    }

    return level;
  }
}