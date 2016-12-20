package karsch.level;

import java.util.Iterator;

import karsch.KarschSimpleGame;
import karsch.characters.CharacterBase;
import karsch.characters.Karsch;
import karsch.debug.MapViewer;

import com.jme3.scene.Node;

@SuppressWarnings("serial")
public class Level extends Node {
  private final Node fieldNode = new Node("fieldnode");
  private final Node wallNode = new Node("wallnode");
  private final Node npcNode = new Node("npcnode");
  private LevelMap levelMap;
  private Karsch karsch;
  private MapViewer mapViewer;
  private LevelStyle levelStyle;

  private LevelText levelText;

  public enum LevelStyle {
    LS_FARM, LS_UNDERGRND, LS_FORREST, LS_HOUSE
  }

  public Level(final int number) {
    super("level" + number);

    attachChild(fieldNode);
    attachChild(wallNode);
    attachChild(npcNode);
  }

  public void setPause(final boolean pause) {
    if (npcNode != null && npcNode.getChildren() != null) {
      final Iterator<?> it = npcNode.getChildren().iterator();
      while (it.hasNext()) {
        final Object o = it.next();
        if (o instanceof CharacterBase) {
          final CharacterBase actChar = (CharacterBase) o;
          actChar.setPause(pause);
        }
      }
    }
  }

  public Node getFieldNode() {
    return fieldNode;
  }

  public Node getWallNode() {
    return wallNode;
  }

  public Node getNpcNode() {
    return npcNode;
  }

  public LevelMap getLevelMap() {
    return levelMap;
  }

  public Karsch getKarsch() {
    return karsch;
  }

  public LevelText getLevelText() {
    return levelText;
  }

  public LevelStyle getLevelStyle() {
    return levelStyle;
  }

  public void setLevelMap(final LevelMap levelMap) {
    this.levelMap = levelMap;
    if (KarschSimpleGame.DEBUG) {
      mapViewer = MapViewer.getInstance();
      mapViewer.setLevelMap(levelMap);
    }
  }

  public void setLevelStyle(final LevelStyle levelStyle) {
    this.levelStyle = levelStyle;
  }

  public void setLevelText(final LevelText levelText) {
    this.levelText = levelText;
  }

  public void setKarsch(final Karsch karsch) {
    this.karsch = karsch;
  }
}
