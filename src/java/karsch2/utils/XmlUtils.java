package karsch2.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Utility class for Xml read, write and convert operations.
 * 
 * @since 28.10.2009
 * @author Stephan
 */
public final class XmlUtils {

  private static final ThreadLocalPrettyXmlOutputter PRETTY_OUTPUTTER = new ThreadLocalPrettyXmlOutputter();
  private static final ThreadLocalXmlOutputter OUTPUTTER = new ThreadLocalXmlOutputter();
  private static final ThreadLocalSaxBuilder SAXBUILDER = new ThreadLocalSaxBuilder();

  /**
   * Private constructor of utility class to prevent instantiation.
   * 
   * @since 08.06.2011
   * @author stephan
   */
  private XmlUtils() {
  }

  /**
   * Retrieve a XMLOutputter with CompactFormat for the current Thread.<br/>
   * Because it is ThreadLocal, no synchronize-blocks are needed.
   * 
   * @return XMLOutputter with CompactFormat
   * 
   * @since 28.10.2009
   * @author Stephan
   */
  public static XMLOutputter getOutputter() {
    return OUTPUTTER.get();
  }

  /**
   * Retrieve a XMLOutputter with PrettyFormat for the current Thread.<br/>
   * Because it is ThreadLocal, no synchronize-blocks are needed.
   * 
   * @return XMLOutputter with PrettyFormat
   * 
   * @since 28.10.2009
   * @author Stephan
   */
  public static XMLOutputter getPrettyOutputter() {
    return PRETTY_OUTPUTTER.get();
  }

  /**
   * Retrieve a SaxBuilder for the current Thread.<br/>
   * Because it is ThreadLocal, no synchronize-blocks are needed.
   * 
   * @return SaxBuilder
   * 
   * @since 28.10.2009
   * @author Stephan
   */
  public static SAXBuilder getSaxBuilder() {
    return SAXBUILDER.get();
  }

  public static String removeIllegalChars(final String input) {
    return input.replaceAll("[<>\\\\'\"&/]", "").trim();
  }

  public static Element addTag(final Element element, final String name) {
    return addTag(element, name, null, false);
  }

  // TODO dOC
  public static Element addTag(final Element element, final String name,
      final String text) {
    return addTag(element, name, text, false);
  }

  public static Element addTag(final Element element, final String name,
      final String text, final boolean isCDATA) {
    final Element t = new Element(name);
    if (element != null) {
      element.addContent(t);
    }
    if (text != null) {
      if (isCDATA) {
        t.addContent(new CDATA(text));
      } else {
        t.setText(text);
      }
    }
    return t;
  }

  /**
   * Adds a new parameter TAG with the given 'name' to a given element. The
   * parameter TAG can contain an attribute 'v' with the given (but optional)
   * content 'value'.
   * 
   * @param element
   *          to add the parameter TAG to
   * @param id
   *          the id of the parameter TAG
   * @param value
   *          the value which is inserted within this TAG
   * @return the created parameter TAG
   * 
   * @since 30.08.2012
   * @author s.bathke
   */
  public static Element addParameter(final Element element, final String id,
      final String value) {

    final Element t = new Element("p");
    if (value != null) {
      t.setAttribute("id", id);
    }
    element.addContent(value);

    return t;
  }

  /**
   * Converts a list to xml.
   * 
   * @param tagName
   *          The desired name of the xml element.
   * @param list
   *          The list to convert.
   * @return A new xml element with the given tag name and the comma separated
   *         list as text.
   * 
   * @since 16.02.2010
   * @author Stephan
   */
  public static Element listToXml(final String tagName, final List<?> list) {
    final Element elem = new Element(tagName);
    final StringBuilder sb = new StringBuilder(256);
    final Iterator<?> it = list.iterator();
    while (it.hasNext()) {
      sb.append(it.next());
      if (it.hasNext()) {
        sb.append(',');
      }
    }
    elem.setText(sb.toString());
    return elem;
  }

  /**
   * Parses a list of objects from the given xml element text. The text must be
   * comma (',') separated.
   * <p>
   * All primitives and their wrapper classes are supported: <code>Short, Byte,
   * Integer, Long, Float, Double, String, Char, Boolean.</code>
   * 
   * @param <T>
   *          The type to get from xml
   * @param t
   *          The desired class of the objects
   * @param tagName
   *          The tagname to parse the list from, may be <code>null</code>
   * @param element
   *          The element that contains the list or the parent element
   * @return List of objects of the desired class
   * @throws Exception
   *           If something goes wrong or the given class is not supported
   * 
   * @since 16.02.2010
   * @author Stephan
   */
  @SuppressWarnings("unchecked")
  public static <T> List<T> listFromXml(final Class<T> t, final String tagName,
      final Element element) throws Exception {
    final List<T> list = new ArrayList<T>();

    Element e = element;

    if (tagName != null && !e.getName().equals(tagName)) {
      e = e.getChild(tagName);
    }

    final String text = e.getText();

    if (text == null || text.isEmpty()) {
      return list;
    }

    // split the text by comma
    final String[] strings = e.getText().split(",");

    // check the classes
    final boolean isShort = t == Short.class || t == short.class;
    final boolean isByte = t == Byte.class || t == byte.class;
    final boolean isInteger = t == Integer.class || t == int.class;
    final boolean isLong = t == Long.class || t == long.class;
    final boolean isFloat = t == Float.class || t == float.class;
    final boolean isDouble = t == Double.class || t == double.class;
    final boolean isString = t == String.class;
    final boolean isChar = t == Character.class || t == char.class;
    final boolean isBoolean = t == Boolean.class || t == boolean.class;

    for (final String string : strings) {
      final String s = string.trim();
      if (isString) {
        list.add((T) s);
      } else if (isShort) {
        list.add((T) Short.valueOf(s));
      } else if (isByte) {
        list.add((T) Byte.valueOf(s));
      } else if (isInteger) {
        list.add((T) Integer.valueOf(s));
      } else if (isLong) {
        list.add((T) Long.valueOf(s));
      } else if (isFloat) {
        list.add((T) Float.valueOf(s));
      } else if (isDouble) {
        list.add((T) Double.valueOf(s));
      } else if (isChar) {
        list.add((T) Character.valueOf(s.charAt(0)));
      } else if (isBoolean) {
        list.add((T) Boolean.valueOf(s));
      } else {
        throw new Exception("unknown data type " + t);
      }
    }

    return list;
  }

  public static String toString(final Element element) {
    return getOutputter().outputString(element);
  }

  public static Element toXml(final String xmlString) throws JDOMException,
      IOException {
    return getSaxBuilder().build(new StringReader(xmlString)).getRootElement();
  }

  public static String toPrettyString(final Element element) {
    return getPrettyOutputter().outputString(element);
  }

  public static String toHTMLString(final Color c, final boolean withHash) {
    return (withHash ? "#" : "")
        + Integer.toHexString((c.getRGB() & 0xffffff) | 0x1000000).substring(1);
  }

  public static Color parseColor(final String s) {
    if (s.startsWith("#")) {
      return Color.decode("0x" + s.substring(1));
    } else {
      return Color.decode("0x" + s);
    }

  }

  public static Font getFontFromXML(final Element e) {
    Font f = null;
    String name = Font.SANS_SERIF;
    Float size = 10f;
    int style = Font.PLAIN;

    if (e.getChild("xfont") != null) {
      name = e.getChildText("xfont");
    }
    if (e.getChild("xfont-size") != null) {
      size = Float.parseFloat(e.getChildText("xfont-size"));
    }
    if (e.getChild("xfont-style") != null) {
      style = Integer.parseInt(e.getChildText("xfont-style"));
    }
    f = Font.decode(name);
    f = f.deriveFont(size);
    f = f.deriveFont(style);

    return f;
  }

  public static void createXMLFromStroke(final Element e, final Stroke s) {
    throw new UnsupportedOperationException("This method has no implementation");
  }

  public static Stroke getStrokeFromXML(final Element e) {
    throw new UnsupportedOperationException("This method has no implementation");
  }

  private static class ThreadLocalXmlOutputter extends
      ThreadLocal<XMLOutputter> {

    @Override
    protected XMLOutputter initialValue() {
      return new XMLOutputter(Format.getCompactFormat());
    }

  }

  private static class ThreadLocalPrettyXmlOutputter extends
      ThreadLocal<XMLOutputter> {

    @Override
    protected XMLOutputter initialValue() {
      return new XMLOutputter(Format.getPrettyFormat());
    }

  }

  private static class ThreadLocalSaxBuilder extends ThreadLocal<SAXBuilder> {

    @Override
    protected SAXBuilder initialValue() {
      final SAXBuilder sb = new SAXBuilder();

      // according to the jdom 1.1.1 documentation, these two flags should speed
      // up the parser when parse many small documents
      sb.setReuseParser(true);

      // according to the jdom documentation, the fast reconfigure is deprecated
      // because all reusable parsers are fast reconfigured
      // TODO REMOVE
      // sb.setFastReconfigure(true);

      return sb;
    }

  }

}
