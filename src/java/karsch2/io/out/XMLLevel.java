package karsch2.io.out;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import karsch.level.Level.LevelStyle;
import karsch.level.LevelText;
import karsch2.core.ItemType;
import karsch2.io.IXML;
import karsch2.utils.XmlUtils;

import org.jdom.Element;

import com.jme3.asset.AssetKey;
import com.jme3.asset.CloneableSmartAsset;

public class XMLLevel implements IXML, CloneableSmartAsset {
  private AssetKey key;

  private final Map<ItemType, List<XMLItemRef>> itemRefs = new HashMap<ItemType, List<XMLItemRef>>();
  private Integer number;
  private Integer xSize, ySize;
  private Float levelToWorldScale = 5f;
  private LevelStyle style;
  private final List<HudText> hudTexts = new ArrayList<XMLLevel.HudText>();

  private final List<XMLDialog> dialogs = new ArrayList<XMLDialog>();

  public void setLevelText(final LevelText levelText) {
    style = levelText.getLevelStyle();
    hudTexts.add(new HudText(levelText.getText("start_txt"), 3000));
  }

  public void add(final XMLItemRef desc) {
    if (desc == null || desc.getType() == null) {
      throw new RuntimeException("No type set");
    }

    List<XMLItemRef> list = itemRefs.get(desc.getType());
    if (list == null) {
      list = new ArrayList<XMLItemRef>();
      itemRefs.put(desc.getType(), list);
    }

    list.add(desc);
  }

  public void setSize(final int xSize, final int ySize) {
    this.xSize = xSize;
    this.ySize = ySize;
  }

  public void setNumber(final int number) {
    this.number = number;
  }

  public Map<ItemType, List<XMLItemRef>> getItemRefs() {
    return itemRefs;
  }

  public Integer getNumber() {
    return number;
  }

  public Integer getxSize() {
    return xSize;
  }

  public Integer getySize() {
    return ySize;
  }

  public LevelStyle getStyle() {
    return style;
  }

  public List<HudText> getHudTexts() {
    return hudTexts;
  }

  public List<XMLDialog> getDialogs() {
    return dialogs;
  }

  public Float getLevelToWorldScale() {
    return levelToWorldScale;
  }

  private Element toXml(final ItemType type, final String nodeName) {
    final Element node = new Element(nodeName);

    final List<XMLItemRef> list = itemRefs.get(type);
    if (list != null) {
      Collections.sort(list, new Comparator<XMLItemRef>() {
        @Override
        public int compare(final XMLItemRef o1, final XMLItemRef o2) {
          return o1.getName().compareTo(o2.getName());
        };
      });

      for (final XMLItemRef desc : list) {
        node.addContent(desc.toXml());
      }
    }

    return node;
  }

  public void addDialog(final XMLDialog dialog) {
    if (!dialogs.contains(dialog)) {
      dialogs.add(dialog);
    }
  }

  @Override
  public Element toXml() {
    final Element root = new Element("level");
    root.setAttribute("number", number.toString());
    root.setAttribute("style", style.name());
    root.setAttribute("level_to_world_scale", levelToWorldScale.toString());

    if (xSize != null && ySize != null) {
      final Element dim = XmlUtils.addTag(root, "dimension");
      dim.setAttribute("size_x", xSize.toString());
      dim.setAttribute("size_y", ySize.toString());
    }

    if (!hudTexts.isEmpty()) {
      final Element iTxtElem = XmlUtils.addTag(root, "hud_text");
      for (final HudText t : hudTexts) {
        iTxtElem.addContent(t.toXml());
      }
    }

    final Element content = new Element("content");
    root.addContent(content);

    content.addContent(toXml(ItemType.LEVEL_STATIC, "static"));
    content.addContent(toXml(ItemType.LEVEL_DYNAMIC, "dynamic"));
    content.addContent(toXml(ItemType.ITEM, "item"));
    content.addContent(toXml(ItemType.CHARACTER, "character"));
    content.addContent(toXml(ItemType.MARKER, "marker"));

    if (!dialogs.isEmpty()) {
      final Element interactionElem = XmlUtils.addTag(root, "interaction");
      for (final XMLDialog dialog : dialogs) {
        if (dialog.getDialog() != null) {
          interactionElem.addContent(dialog.toXml());
        }
      }
    }

    return root;
  }

  @Override
  public void parseXml(final Element element) throws Exception {
    number = element.getAttribute("number").getIntValue();
    style = LevelStyle.valueOf(element.getAttributeValue("style"));
    levelToWorldScale = element.getAttribute("level_to_world_scale")
        .getFloatValue();

    Element e = element.getChild("dimension");
    if (e != null) {
      xSize = e.getAttribute("size_x").getIntValue();
      ySize = e.getAttribute("size_y").getIntValue();
    }

    e = element.getChild("hud_text");
    if (e != null) {
      final Iterator<?> it = e.getChildren().iterator();
      while (it.hasNext()) {
        final HudText t = new HudText();
        t.parseXml((Element) it.next());
        hudTexts.add(t);
      }
    }

    e = element.getChild("content");
    if (e != null) {
      final Iterator<?> it = e.getChildren().iterator();
      while (it.hasNext()) {
        final Element group = (Element) it.next();
        final Iterator<?> it2 = group.getChildren().iterator();
        while (it2.hasNext()) {
          final XMLItemRef itm = new XMLItemRef();
          itm.parseXml((Element) it2.next());
          add(itm);
        }
      }
    }

    e = element.getChild("interaction");
    if (e != null) {
      final Iterator<?> it = e.getChildren().iterator();
      while (it.hasNext()) {
        final Element iElem = (Element) it.next();
        final XMLDialog d = new XMLDialog();
        d.parseXml(iElem);
        dialogs.add(d);
      }
    }
  }

  @Override
  public void setKey(final AssetKey key) {
    this.key = key;
  }

  @Override
  public AssetKey getKey() {
    return key;
  }

  @Override
  public XMLLevel clone() {
    try {
      return (XMLLevel) super.clone();
    } catch (final CloneNotSupportedException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static class HudText implements IXML {
    private String text;
    private Integer delay;

    public HudText() {
    }

    public HudText(final String text, final Integer delay) {
      this.text = text;
      this.delay = delay;
    }

    public Integer getDelay() {
      return delay;
    }

    public String getText() {
      return text;
    }

    public void setText(final String text) {
      this.text = text;
    }

    public void setDelay(final Integer delay) {
      this.delay = delay;
    }

    @Override
    public void parseXml(final Element element) throws Exception {
      text = element.getText();
      delay = element.getAttribute("total_delay").getIntValue();
    }

    @Override
    public Element toXml() {
      final Element hudElem = XmlUtils.addTag(null, "text", text);
      hudElem.setAttribute("total_delay", delay.toString());
      return hudElem;
    }
  }
}
