package karsch2.io.out;

import java.awt.Color;
import java.awt.Point;

import karsch.controller.NPCController.Direction;
import karsch2.core.ItemType;
import karsch2.io.IXML;
import karsch2.utils.XmlUtils;

import org.jdom.Element;

public final class XMLItemRef implements IXML {
  private String name;
  private ItemType type;

  private Integer levelX;
  private Integer levelY;
  private Integer xSize;
  private Integer ySize;
  private Direction direction;
  private Color deprecatedColor;
  private String connections;
  private Integer targetPositionX;
  private Integer targetPositionY;
  private Integer levelLink;
  private boolean startPosition;
  private Float skyRadius;

  public void setSize(final int xSize, final int ySize) {
    this.xSize = xSize;
    this.ySize = ySize;
  }

  public void setConnections(final String connections) {
    this.connections = connections;
  }

  public void setLevelX(final int levelX) {
    this.levelX = levelX;
  }

  public void setSkyRadius(final Float skyRadius) {
    this.skyRadius = skyRadius;
  }

  public void setLevelY(final int levelY) {
    this.levelY = levelY;
  }

  public void setLevelXY(final int levelX, final int levelY) {
    this.levelX = levelX;
    this.levelY = levelY;
  }

  public void setTargetPositon(final int targetX, final int targetY) {
    this.targetPositionX = targetX;
    this.targetPositionY = targetY;
  }

  public void setDeprecatedColor(final Color deprecatedColor) {
    this.deprecatedColor = deprecatedColor;
  }

  public void setDirection(final Direction direction) {
    this.direction = direction;
  }

  public void setLevelLink(final Integer levelLink) {
    this.levelLink = levelLink;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setType(final ItemType type) {
    this.type = type;
  }

  public ItemType getType() {
    return type;
  }

  public Integer getLevelX() {
    return levelX;
  }

  public Integer getLevelY() {
    return levelY;
  }

  public Integer getxSize() {
    return xSize;
  }

  public Integer getySize() {
    return ySize;
  }

  public Direction getDirection() {
    return direction != null ? direction : Direction.DIRECTION_NODIR;
  }

  public Color getDeprecatedColor() {
    return deprecatedColor;
  }

  public String getConnections() {
    return connections;
  }

  public Integer getTargetPositionX() {
    return targetPositionX;
  }

  public Integer getTargetPositionY() {
    return targetPositionY;
  }

  public Integer getLevelLink() {
    return levelLink;
  }

  public boolean isStartPosition() {
    return startPosition;
  }

  public Float getSkyRadius() {
    return skyRadius;
  }

  @Override
  public void parseXml(final Element element) throws Exception {
    name = element.getAttributeValue("name");

    connections = element.getAttributeValue("connection_property");

    type = ItemType.valueOf(element.getAttributeValue("item_type"));

    Element e = element.getChild("direction");
    if (e != null) {
      direction = Direction.valueOf(e.getText());
    }

    e = element.getChild("position");
    if (e != null) {
      levelX = e.getAttribute("level_x").getIntValue();
      levelY = e.getAttribute("level_y").getIntValue();
    }

    e = element.getChild("move_to_position");
    if (e != null) {
      targetPositionX = e.getAttribute("level_x").getIntValue();
      targetPositionY = e.getAttribute("level_y").getIntValue();
    }

    e = element.getChild("dimension");
    if (e != null) {
      xSize = e.getAttribute("size_x").getIntValue();
      ySize = e.getAttribute("size_y").getIntValue();
    }

    e = element.getChild("sky");
    if (e != null) {
      skyRadius = e.getAttribute("radius").getFloatValue();
    }

    e = element.getChild("level_link");
    if (e != null) {
      levelLink = e.getAttribute("level").getIntValue();
    }

    e = element.getChild("start_position");
    if (e != null) {
      startPosition = true;
    }
  }

  @Override
  public Element toXml() {
    final Element e = new Element("itemref");
    if (type != null) {
      e.setAttribute("item_type", type.name());
    }

    if (name != null) {
      e.setAttribute("name", name);
    }

    if (connections != null) {
      e.setAttribute("connection_property", connections);
    }

    if (direction != null) {
      XmlUtils.addTag(e, "direction", direction.name());
    }

    if (levelX != null && levelY != null) {
      final Element pos = XmlUtils.addTag(e, "position");
      pos.setAttribute("level_x", levelX.toString());
      pos.setAttribute("level_y", levelY.toString());
    }

    if (targetPositionX != null && targetPositionY != null) {
      final Element pos = XmlUtils.addTag(e, "move_to_position");
      pos.setAttribute("level_x", targetPositionX.toString());
      pos.setAttribute("level_y", targetPositionY.toString());
    }

    if (xSize != null && ySize != null) {
      final Element dim = XmlUtils.addTag(e, "dimension");
      dim.setAttribute("size_x", xSize.toString());
      dim.setAttribute("size_y", ySize.toString());
    }

    if (skyRadius != null) {
      final Element sky = XmlUtils.addTag(e, "sky");
      sky.setAttribute("radius", skyRadius.toString());
    }

    if (deprecatedColor != null) {
      final Element cElem = XmlUtils.addTag(e, "color",
          XmlUtils.toHTMLString(deprecatedColor, true));
      cElem.setAttribute("deprecated", "true");
    }

    if (levelLink != null) {
      final Element lnk = XmlUtils.addTag(e, "level_link");
      lnk.setAttribute("level", levelLink.toString());
    }

    if (startPosition) {
      XmlUtils.addTag(e, "start_position");
    }

    return e;
  }

  public Point getInteractionLocation() {
    if (direction != null) {
      final Point p = direction.toTranslation();
      p.x += levelX;
      p.y += levelY;
      return p;
    } else {
      return new Point(levelX, levelY);
    }
  }

  public void setStartPosition(final boolean b) {
    this.startPosition = b;
  }
}
