package karsch2.es.component;

import es.core.component.EComponent;

public class ItemNameComponent implements EComponent {
  private final String name;

  public ItemNameComponent(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
