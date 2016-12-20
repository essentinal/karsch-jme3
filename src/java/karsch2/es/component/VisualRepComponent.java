package karsch2.es.component;

import com.jme3.math.Vector3f;

import es.core.component.EComponent;

/**
 * This component indicates that an entity has a visual representation and
 * stores the asset name, local model scale and local model translation.
 * 
 * @author Stephan Dreyer
 * 
 */
public class VisualRepComponent implements EComponent {
  private final String itemName;
  private final String assetName;
  private final Vector3f scale;
  private final Vector3f translation;

  public VisualRepComponent(final String itemName, final String assetName,
      final Vector3f scale, final Vector3f translation) {
    this.itemName = itemName;
    this.assetName = assetName;
    this.scale = scale.clone();
    this.translation = translation.clone();
  }

  public Vector3f getTranslation() {
    return translation;
  }

  public String getAssetName() {
    return assetName;
  }

  public Vector3f getScale() {
    return scale;
  }

  public String getItemName() {
    return itemName;
  }

}
