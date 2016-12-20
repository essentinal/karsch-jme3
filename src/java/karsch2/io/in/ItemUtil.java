package karsch2.io.in;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import karsch2.core.Item;
import karsch2.utils.XmlUtils;

import org.jdom.Element;

public class ItemUtil {
  private static final Logger LOGGER = Logger.getLogger(ItemUtil.class
      .getName());

  private static Map<String, Item> cache;

  static {
    cache = new ConcurrentHashMap<String, Item>();
    Element e;
    try {
      e = XmlUtils
          .getSaxBuilder()
          .build(
              ItemUtil.class.getClassLoader().getResourceAsStream(
                  "items/items.xml")).getRootElement();

      final Iterator<?> it = e.getChildren("item").iterator();
      while (it.hasNext()) {
        final Element itemElem = (Element) it.next();
        loadItem(itemElem.getAttributeValue("filename"));
      }

    } catch (final Exception e1) {
      e1.printStackTrace();
    }
  }

  public static Item getItem(final String name) {
    return cache.get(name);
  }

  private static void loadItem(final String filename) {

    try {
      final Element e = XmlUtils.getSaxBuilder()
          .build(ItemUtil.class.getClassLoader().getResourceAsStream(filename))
          .getRootElement();
      final Item item = new Item();
      item.parseXml(e);

      cache.put(item.getName(), item);
    } catch (final Exception e) {
      e.printStackTrace();
      LOGGER.log(Level.WARNING, "Error loading item file {0}", filename);
    }
  }
}
