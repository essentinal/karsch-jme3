package karsch2.io.out;

import java.util.Iterator;

import karsch.sound.Dialog;
import karsch2.io.IXML;
import karsch2.utils.StringUtils;
import karsch2.utils.XmlUtils;

import org.jdom.Element;

public class XMLDialog implements IXML {
  private Dialog dialog;
  private String condition;
  private String character1 = "karsch";
  private String character2;

  public XMLDialog() {
  }

  public void setDialog(final Dialog dialog) {
    this.dialog = dialog;
  }

  public void setCondition(final String condition) {
    this.condition = condition;
  }

  public void setCharacter2(final String character2) {
    this.character2 = character2;
  }

  public Dialog getDialog() {
    return dialog;
  }

  @Override
  public Element toXml() {
    final Element root = new Element("dialog");

    if (condition != null) {
      root.setAttribute("condition", condition);
    }

    if (character2 != null) {
      final Element btwnElem = XmlUtils.addTag(root, "between");

      final Element e1 = XmlUtils.addTag(btwnElem, "character");
      e1.setAttribute("name", character1);

      final Element e2 = XmlUtils.addTag(btwnElem, "character");
      e2.setAttribute("name", character2);
    }

    for (int i = 0; i < Math.max(dialog.getSpeechFileNames().size(), dialog
        .getTexts().size()); i++) {
      final Element stepElem = XmlUtils.addTag(root, "step");
      stepElem.setAttribute("index", String.valueOf(i));

      final Element e1 = XmlUtils.addTag(stepElem, "character");
      e1.setAttribute("name", i % 2 == 0 ? character2 : character1);

      try {
        final String text = dialog.getTexts().get(i);
        XmlUtils.addTag(stepElem, "text", text).setAttribute("lang", "en");
      } catch (final Exception e) {
      }

      try {
        final String speech = dialog.getSpeechFileNames().get(i);
        final Element audioElem = XmlUtils
            .addTag(stepElem, "audiofile", speech);
        audioElem.setAttribute("lang", "en");
      } catch (final Exception e) {
      }

    }

    return root;
  }

  @Override
  public void parseXml(final Element element) {
    dialog = new Dialog();
    condition = element.getAttributeValue("condition");

    final Element e = element.getChild("between");
    if (e != null) {
      // TODO make this more flexible
      character1 = ((Element) e.getChildren().get(0)).getAttributeValue("name");
      character2 = ((Element) e.getChildren().get(1)).getAttributeValue("name");
    }

    final Iterator<?> it = element.getChildren().iterator();
    while (it.hasNext()) {
      final Element stepElem = (Element) it.next();
      // TODO this can not be stored yet
      final String character = stepElem.getChild("character")
          .getAttributeValue("name");

      // TODO also parse and store the language
      final String txt = stepElem.getChildText("text");
      final String audioFile = stepElem.getChildText("text");

      dialog.addText(txt);
      dialog.addSpeech(audioFile);
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (o != null && o.getClass() == getClass()) {
      if (!StringUtils.fixNull(condition).equals(((XMLDialog) o).condition)) {
        return false;
      }

      if (!StringUtils.fixNull(character1).equals(((XMLDialog) o).character1)) {
        return false;
      }

      if (!StringUtils.fixNull(character2).equals(((XMLDialog) o).character2)) {
        return false;
      }

      return true;
    }
    return false;
  }

}
