package karsch2.es.component;

import es.core.component.EComponent;

public class TerrainComponent implements EComponent {
  private final String textureName;

  public TerrainComponent(final String textureName) {
    this.textureName = textureName;
  }

  public String getTextureName() {
    return textureName;
  }
}
