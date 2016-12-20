package karsch2.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import karsch2.es.component.AnimationComponent;
import karsch2.es.component.CollisionGroupComponent;
import karsch2.es.component.ModifyTerrainComponent;
import karsch2.es.component.PlayerControlComponent;
import karsch2.es.component.TerrainComponent;
import karsch2.es.component.VisualRepComponent;
import karsch2.io.IXML;

import org.jdom.Element;

import com.jme3.math.Vector3f;

import es.core.component.EComponent;

public class Item implements IXML {
  private String name;
  private String assetName;
  private Vector3f scale;
  private ItemType type;
  private Vector3f translate;
  private ControlType control;
  private float controlSpeed;
  private String collisionGroup;
  private Map<AnimationType, AnimMapping> anims;

  private ModifyTerrainComponent modifyTerrain;

  private String terrainTexture;

  @Override
  public Element toXml() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void parseXml(final Element element) throws Exception {
    name = element.getAttributeValue("name");
    type = ItemType.valueOf(element.getAttributeValue("type"));

    final Element modelElem = element.getChild("model");
    if (modelElem != null) {
      assetName = modelElem.getAttributeValue("asset");
      final Element scaleElem = modelElem.getChild("scale");

      scale = Vector3f.UNIT_XYZ.clone();
      if (scaleElem != null) {
        scale.x = scaleElem.getAttribute("x").getFloatValue();
        scale.y = scaleElem.getAttribute("y").getFloatValue();
        scale.z = scaleElem.getAttribute("z").getFloatValue();
      }

      translate = new Vector3f();
      final Element translateElem = modelElem.getChild("translate");
      if (translateElem != null) {
        translate.x = translateElem.getAttribute("x").getFloatValue();
        translate.y = translateElem.getAttribute("y").getFloatValue();
        translate.z = translateElem.getAttribute("z").getFloatValue();
      }
    }

    Iterator<?> it = element.getChildren("animation").iterator();
    if (it.hasNext()) {
      anims = new HashMap<AnimationType, AnimMapping>();
    }

    while (it.hasNext()) {
      final Element animElem = (Element) it.next();

      final AnimMapping anim = new AnimMapping(AnimationType.valueOf(animElem
          .getAttributeValue("type")), animElem.getAttributeValue("name"),
          animElem.getAttribute("speed").getFloatValue());
      anims.put(anim.getType(), anim);
    }

    it = element.getChildren("control").iterator();
    while (it.hasNext()) {
      final Element contrElem = (Element) it.next();
      // TODO only one control supported yet
      control = ControlType.valueOf(contrElem.getAttributeValue("type"));
      controlSpeed = contrElem.getAttribute("speed").getFloatValue();
    }

    it = element.getChildren("collisiongroup").iterator();
    while (it.hasNext()) {
      final Element collGroup = (Element) it.next();
      // TODO only one collision group per item supported yet
      collisionGroup = collGroup.getAttributeValue("name");
    }

    it = element.getChildren("terrain").iterator();
    while (it.hasNext()) {
      final Element terrainGroup = (Element) it.next();
      terrainTexture = terrainGroup.getAttributeValue("texture");
    }

    it = element.getChildren("modifyterrain").iterator();
    while (it.hasNext()) {
      final Element modifyElem = (Element) it.next();
      final String s = modifyElem.getAttributeValue("layer");
      final boolean stone = "stone".equals(s);
      final boolean forest = "forest".equals(s);
      final boolean water = "water".equals(s);
      modifyTerrain = new ModifyTerrainComponent(stone, forest, water);
    }
  }

  public Map<AnimationType, AnimMapping> getAnims() {
    return anims;
  }

  public String getName() {
    return name;
  }

  public Vector3f getTranslation() {
    return translate;
  }

  public EComponent[] getComponents() {
    final List<EComponent> comps = new ArrayList<EComponent>();
    if (assetName != null) {
      comps.add(new VisualRepComponent(name, assetName, scale, translate));
    }

    if (control != null) {
      // TODO implement other components
      comps.add(new PlayerControlComponent(controlSpeed));
    }

    if (anims != null && anims.containsKey(AnimationType.STAND)) {
      comps.add(new AnimationComponent(AnimationType.STAND, false));
    }

    if (collisionGroup != null) {
      comps.add(new CollisionGroupComponent(collisionGroup));
    }

    if (modifyTerrain != null) {
      comps.add(modifyTerrain);
    }

    if (terrainTexture != null) {
      comps.add(new TerrainComponent(terrainTexture));
    }

    return comps.toArray(new EComponent[comps.size()]);
  }

  public ItemType getType() {
    return type;
  }

}
