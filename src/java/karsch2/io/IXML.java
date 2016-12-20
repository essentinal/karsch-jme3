package karsch2.io;

import org.jdom.Element;

public interface IXML {
  public Element toXml();

  public void parseXml(Element element) throws Exception;
}
