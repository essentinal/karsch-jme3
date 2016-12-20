package karsch2.io.out;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import karsch.Values;
import karsch.characters.CharacterBase;
import karsch.characters.Cow;
import karsch.characters.Cow2;
import karsch.characters.CreditSheep;
import karsch.characters.Gunther;
import karsch.characters.Karsch;
import karsch.characters.MrsKarsch;
import karsch.characters.Pig;
import karsch.characters.PigHouse;
import karsch.characters.Sheep;
import karsch.effects.FogOfWar;
import karsch.items.Key;
import karsch.level.Blocker;
import karsch.level.Level.LevelStyle;
import karsch.level.LevelFactory;
import karsch.level.LevelMap;
import karsch.level.LevelText;
import karsch.level.tiles.Fence;
import karsch.level.tiles.FenceGate;
import karsch.level.tiles.Floor;
import karsch.level.tiles.Hay;
import karsch.level.tiles.House;
import karsch.level.tiles.HouseInside;
import karsch.level.tiles.LevelEntrance;
import karsch.level.tiles.Lever;
import karsch.level.tiles.RollingStone;
import karsch.level.tiles.SpikeTrap;
import karsch.level.tiles.Stone;
import karsch.level.tiles.Tile;
import karsch.level.tiles.Tree;
import karsch.level.tiles.Wall;
import karsch2.core.ItemType;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.jme3.math.Vector3f;

public class ConvertLevelImageToXML {
  private static final String assetPath = "karsch/resource/levels/";
  private static final String prefix = "src/java/";

  public static void convertAll() {
    new File(assetPath).mkdirs();

    final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
    for (int i = 1; i < 9; i++) {
      final XMLLevel export = convertLevel(i);

      final File levelFile = new File(prefix + assetPath + "level" + i
          + ".level.xml");

      OutputStream out = null;
      try {
        out = new FileOutputStream(levelFile);
        outputter.output(export.toXml(), out);
      } catch (final Exception e) {
        e.printStackTrace();
        System.exit(1);
      } finally {
        try {
          out.close();
        } catch (final Exception e) {

        }
      }

    }
  }

  public static XMLLevel convertLevel(final int number) {

    BufferedImage levelImage = null;
    final LevelText levelText = new LevelText(number);
    final String imageFile = "level" + number + ".png";

    try {
      levelImage = ImageIO.read(ConvertLevelImageToXML.class.getClassLoader()
          .getResourceAsStream(assetPath + imageFile));
    } catch (final IOException e) {
      e.printStackTrace();
      return null;
    }

    final int xSize = levelImage.getWidth();
    final int ySize = levelImage.getHeight();

    final LevelStyle levelStyle = levelText.getLevelStyle();

    final LevelMap levelMap = new LevelMap(xSize, ySize, levelStyle, number);

    final XMLLevel export = new XMLLevel();
    export.setNumber(number);
    export.setLevelText(levelText);
    export.setSize(xSize, ySize);

    if (levelStyle != LevelStyle.LS_UNDERGRND
        && levelStyle != LevelStyle.LS_HOUSE) {
      // level.attachChild(new PanoramaEnv(xSize / 2, ySize / 2, 200, 12));

      final XMLItemRef desc = new XMLItemRef();
      desc.setName("sky");
      desc.setType(ItemType.LEVEL_STATIC);
      desc.setLevelXY(xSize / 2, ySize / 2);
      desc.setSkyRadius(200f);
      export.add(desc);
    }

    if (levelStyle != LevelStyle.LS_HOUSE) {
      // level.attachChild(
      final Floor f = new Floor(xSize * 2, ySize * 2, levelStyle);
      // );

      final XMLItemRef desc = new XMLItemRef();
      desc.setName(f.getItemName());
      desc.setType(ItemType.LEVEL_STATIC);
      desc.setLevelXY(xSize / 2, ySize / 2);
      desc.setSize(xSize, ySize);
      export.add(desc);
    }

    for (int x = 0; x < xSize; x++) {
      for (int y = 0; y < ySize; y++) {
        CharacterBase actChar = null;
        final int pixel = levelImage.getRGB(x, y);

        Tile tmp = null;

        XMLItemRef desc = new XMLItemRef();
        desc.setDeprecatedColor(new Color(pixel));
        desc.setLevelXY(x, y);

        if (pixel == LevelFactory.COLOR_WALL) {
          desc.setType(ItemType.LEVEL_STATIC);
          if (levelStyle == LevelStyle.LS_UNDERGRND) {
            tmp = new Wall(x, y);
            desc.setName("wall");
          } else if (levelStyle == LevelStyle.LS_FARM) {
            tmp = new Stone(x, y);
            desc.setName("stone1");
          } else if (levelStyle == LevelStyle.LS_HOUSE) {
            tmp = new Wall(x, y, true);
            desc.setName("blackwall");
          }
          // level.getWallNode().attachChild(tmp);

          levelMap.getLevelMap()[x][y] = tmp;
        } else if (pixel == LevelFactory.COLOR_TREE) {
          desc.setType(ItemType.LEVEL_STATIC);
          if (levelStyle == LevelStyle.LS_HOUSE) {
            tmp = new Hay(x, y);
            desc.setName(((Hay) tmp).getItemName());
            // level.getFieldNode().attachChild(tmp);
            levelMap.getLevelMap()[x][y] = tmp;
          } else {
            tmp = new Tree(x, y, 1);
            desc.setName("tree1");
            // level.getFieldNode().attachChild(tmp);
            levelMap.getLevelMap()[x][y] = tmp;
          }
        } else if (pixel == LevelFactory.COLOR_HOUSE) {

          tmp = new House(x, y, levelImage);
          if (((House) tmp).getNewHouse()) {
            // level.getWallNode().attachChild(tmp);
            desc.setType(ItemType.LEVEL_STATIC);
            desc.setName("house1");

            final int hXSize = ((House) tmp).getXSize();
            final int hYSize = ((House) tmp).getYSize();

            desc.setLevelXY(x + (hXSize / 2), y + (hYSize / 2));
            desc.setSize(hXSize, hYSize);
            desc.setDirection(((House) tmp).getDirection());
          }
          levelMap.getLevelMap()[x][y] = tmp;
        } else if (pixel == LevelFactory.COLOR_FENCE) {
          tmp = new Fence(x, y, levelImage);
          // level.getWallNode().attachChild(tmp);
          levelMap.getLevelMap()[x][y] = tmp;
          desc.setType(ItemType.LEVEL_STATIC);
          desc.setName(((Fence) tmp).getItemName());
          desc.setDirection(((Fence) tmp).getDirection());
          // desc.setConnections(((Fence) tmp).getConnectionString());
        } else if (pixel == LevelFactory.COLOR_FENCEGATE) {
          tmp = new FenceGate(x, y, levelText);
          // level.getWallNode().attachChild(tmp);
          levelMap.getLevelMap()[x][y] = tmp;
          desc.setType(ItemType.LEVEL_DYNAMIC);
          desc.setName("fencegate");
          desc.setDirection(((FenceGate) tmp).getDirection());
        } else if (pixel == LevelFactory.COLOR_LEVER) {
          tmp = new Lever(x, y, levelText);
          // level.getWallNode().attachChild(tmp);
          levelMap.getLevelMap()[x][y] = tmp;
          desc.setType(ItemType.LEVEL_DYNAMIC);
          desc.setName("lever");
          desc.setDirection(((Lever) tmp).getDirection());
        } else if (pixel == LevelFactory.COLOR_STONE) {
          tmp = new RollingStone(x, y, levelText, levelMap);
          // level.getWallNode().attachChild(tmp);
          levelMap.getLevelMap()[x][y] = tmp;
          desc.setType(ItemType.LEVEL_DYNAMIC);
          desc.setName("interactablestone");
          final Point p = ((RollingStone) tmp).getRollingStoneGoalField();
          desc.setTargetPositon(p.x, p.y);
          // TODO

          final XMLDialog dia = new XMLDialog();
          dia.setCharacter2(desc.getName());
          dia.setDialog(((RollingStone) tmp).getDialog());
          export.addDialog(dia);

        } else if (pixel == LevelFactory.COLOR_BLOCKER) {
          levelMap.getLevelMap()[x][y] = new Blocker();
        } else {
          // levelMap.getLevelMap()[x][y] = free;

          // desc.setType(ObjectType.LEVEL_STATIC);
          // desc.setName("free");

          if (levelStyle == LevelStyle.LS_HOUSE) {
            tmp = new HouseInside(x, y, levelImage);
            if (((HouseInside) tmp).getNewHouse()) {

              desc.setType(ItemType.LEVEL_STATIC);
              desc.setName("house1_inside");
              desc.setSize(((HouseInside) tmp).getXSize(),
                  ((HouseInside) tmp).getYSize());
              desc.setDirection(((HouseInside) tmp).getDirection());
              // level.getWallNode().attachChild(tmp);
            }
          }
        }

        if (desc != null && desc.getName() != null) {
          export.add(desc);
        }
        desc = new XMLItemRef();
        desc.setDeprecatedColor(new Color(pixel));
        desc.setLevelXY(x, y);

        if (pixel == LevelFactory.COLOR_PIG) {
          actChar = new Pig(x, y, levelMap);
          actChar.setLocalTranslation(new Vector3f(x * 5 + 2.5f, 0,
              y * 5 + 2.5f));
          // level.getNpcNode().attachChild(actChar);

          desc.setType(ItemType.CHARACTER);
          desc.setName("pig");

          levelMap.getLevelMap()[x][y] = actChar;
        } else if (pixel == LevelFactory.COLOR_PIG_HOUSE) {
          if (Values.getInstance().getBabies() > 0) {

            actChar = new PigHouse(x, y, levelMap);
            actChar.setLocalTranslation(new Vector3f(x * 5 + 2.5f, 0,
                y * 5 + 2.5f));

            // look at mrs karsch
            actChar.lookAt(new Vector3f(12 * 5f + 2.5f, 2, 11 * 5f + 2.5f),
                Vector3f.UNIT_Y);

            desc.setType(ItemType.CHARACTER);
            desc.setName("pig_house");

            // level.getNpcNode().attachChild(actChar);
            levelMap.getLevelMap()[x][y] = actChar;
          } else {
            // levelMap.getLevelMap()[x][y] = free;
          }

        } else if (pixel == LevelFactory.COLOR_SHEEP) {
          actChar = new Sheep(x, y, levelMap);
          // level.getNpcNode().attachChild(actChar);
          levelMap.getLevelMap()[x][y] = actChar;

          desc.setType(ItemType.CHARACTER);
          desc.setName("sheep");

          final XMLDialog dia = new XMLDialog();
          dia.setCharacter2(desc.getName());
          dia.setDialog(((Sheep) actChar).getDialog());
          export.addDialog(dia);

          // random direction
        } else if (pixel == LevelFactory.COLOR_CREDITSSHEEP) {
          actChar = new CreditSheep(x, y, levelMap);
          // level.getNpcNode().attachChild(actChar);
          levelMap.getLevelMap()[x][y] = actChar;

          desc.setType(ItemType.CHARACTER);
          desc.setName("credits_sheep");
        } else if (pixel == LevelFactory.COLOR_COW) {
          actChar = new Cow(x, y, levelMap);
          // level.getNpcNode().attachChild(actChar);
          levelMap.getLevelMap()[x][y] = actChar;

          desc.setType(ItemType.CHARACTER);
          desc.setName("cow");
          // random direction

          final XMLDialog dia = new XMLDialog();
          dia.setCharacter2(desc.getName());
          dia.setDialog(((Cow) actChar).getDialog());
          export.addDialog(dia);

        } else if (pixel == LevelFactory.COLOR_COW2) {
          actChar = new Cow2(x, y, levelMap);
          // level.getNpcNode().attachChild(actChar);
          levelMap.getLevelMap()[x][y] = actChar;

          desc.setType(ItemType.CHARACTER);
          desc.setName("crazy_cow");
          // random direction
        } else if (pixel == LevelFactory.COLOR_GUNTHER) {
          actChar = new Gunther(x, y, levelMap);
          // level.getNpcNode().attachChild(actChar);
          levelMap.getLevelMap()[x][y] = actChar;

          desc.setType(ItemType.CHARACTER);
          desc.setName("gunther");

          XMLDialog dia = new XMLDialog();
          dia.setCharacter2(desc.getName());
          dia.setDialog(((Gunther) actChar).getNoKeyDialog());
          dia.setCondition("keys<1");
          export.addDialog(dia);

          dia = new XMLDialog();
          dia.setCharacter2(desc.getName());
          dia.setDialog(((Gunther) actChar).getKeyDialog());
          dia.setCondition("keys>0");
          export.addDialog(dia);

        } else if (pixel == LevelFactory.COLOR_MRSKARSCH) {
          actChar = new MrsKarsch(x, y, levelMap);
          // level.getNpcNode().attachChild(actChar);
          levelMap.getLevelMap()[x][y] = actChar;

          desc.setType(ItemType.CHARACTER);
          desc.setName("mrs_karsch");

          XMLDialog dia = new XMLDialog();
          dia.setCharacter2(desc.getName());
          dia.setDialog(((MrsKarsch) actChar).getDialog());
          dia.setCondition("level==1");
          export.addDialog(dia);

          dia = new XMLDialog();
          dia.setCharacter2(desc.getName());
          dia.setDialog(((MrsKarsch) actChar).getEnoughBabiesDialog());
          dia.setCondition("babies>4");
          export.addDialog(dia);

          dia = new XMLDialog();
          dia.setCharacter2(desc.getName());
          dia.setDialog(((MrsKarsch) actChar).getTooLittleBabiesDialog());
          dia.setCondition("babies<5");
          export.addDialog(dia);

        } else if (pixel == LevelFactory.COLOR_KEY) {
          actChar = new Key(x, y, levelMap);
          // level.getNpcNode().attachChild(actChar);
          levelMap.getLevelMap()[x][y] = actChar;

          desc.setType(ItemType.ITEM);
          desc.setName("key");

        } else if (pixel == LevelFactory.COLOR_SPIKETRAP) {
          actChar = new SpikeTrap(x, y);
          // level.getFieldNode().attachChild(actChar);
          levelMap.getLevelMap()[x][y] = actChar;

          desc.setType(ItemType.LEVEL_DYNAMIC);
          desc.setName("spike_trap");

        } else if (pixel == LevelFactory.COLOR_NEXTLEVEL) {
          tmp = new LevelEntrance(x, y, levelImage, 1, levelText, new Vector3f(
              x * 5 + 2.5f, 0f, y * 5 + 2.5f), false);
          levelMap.getLevelMap()[x][y] = tmp;
          // level.getFieldNode().attachChild(tmp);
          // if (parent != null)
          // parent.setLevelExit(new Vector3f(x * 5 + 2.5f, 0f, y * 5 + 2.5f));

          desc.setType(ItemType.MARKER);
          desc.setName(((LevelEntrance) tmp).getItemName());
          desc.setLevelLink(number + 1);
          desc.setDirection(((LevelEntrance) tmp).getDirection());

        } else if (pixel == LevelFactory.COLOR_PREVLEVEL_SIGN) {
          tmp = new LevelEntrance(x, y, levelImage, -1, levelText,
              new Vector3f(x * 5 + 2.5f, 0f, y * 5 + 2.5f), false);

          levelMap.getLevelMap()[x][y] = tmp;
          // level.getFieldNode().attachChild(tmp);
          // if (parent != null)
          // parent
          // .setLevelEntrance(new Vector3f(x * 5 + 2.5f, 0f, y * 5 + 2.5f));

          final Karsch karsch = new Karsch(x, y, levelMap, levelText);
          karsch
              .setLocalTranslation(new Vector3f(x * 5 + 2.5f, 0, y * 5 + 2.5f));
          if (levelStyle == LevelStyle.LS_UNDERGRND)
            karsch.attachChild(new FogOfWar());
          // level.setKarsch(karsch);

          desc.setType(ItemType.MARKER);
          desc.setName(((LevelEntrance) tmp).getItemName());
          desc.setLevelLink(number - 1);
          desc.setDirection(((LevelEntrance) tmp).getDirection());

        } else if (pixel == LevelFactory.COLOR_PREVLEVEL) {
          tmp = new LevelEntrance(x, y, levelImage, -1, levelText,
              new Vector3f(x * 5 + 2.5f, 0f, y * 5 + 2.5f), true);

          levelMap.getLevelMap()[x][y] = tmp;
          // level.getFieldNode().attachChild(tmp);
          // if (parent != null)
          // parent
          // .setLevelEntrance(new Vector3f(x * 5 + 2.5f, 0f, y * 5 + 2.5f));

          final Karsch karsch = new Karsch(x, y, levelMap, levelText);
          karsch
              .setLocalTranslation(new Vector3f(x * 5 + 2.5f, 0, y * 5 + 2.5f));
          if (levelStyle == LevelStyle.LS_UNDERGRND) {
            karsch.attachChild(new FogOfWar());

          }
          // level.setKarsch(karsch);

          desc.setType(ItemType.MARKER);
          desc.setName("start_position");
          desc.setStartPosition(true);
          desc.setDirection(((LevelEntrance) tmp).getDirection());

        }

        if (desc != null && desc.getName() != null) {
          export.add(desc);
        }
      }
    }
    return export;
  }
}
