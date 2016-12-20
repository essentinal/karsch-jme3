package karsch.sound;

import java.util.ArrayList;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.effects.SpeechBalloon;

import com.jme3.animation.AudioTrack;

public class Dialog {
  private int textNumber;

  private final ArrayList<String> texts;
  private final ArrayList<AudioTrack> speeches;
  private final ArrayList<String> speechFileNames;
  private final SpeechBalloon balloon;
  private Karsch karsch;

  public Dialog() {
    this(null);
  }

  public Dialog(final SpeechBalloon balloon) {
    this.balloon = balloon;
    texts = new ArrayList<String>();
    speeches = new ArrayList<AudioTrack>();
    speechFileNames = new ArrayList<String>();
  }

  public void addText(final String text) {
    texts.add(text);
  }

  public void addSpeech(final String filename) {
    speechFileNames.add(filename);
    final AudioTrack track = SoundManager.prepareTrack(filename);
    if (track != null) {
      speeches.add(track);
    }
  }

  public ArrayList<String> getSpeechFileNames() {
    return speechFileNames;
  }

  public ArrayList<String> getTexts() {
    return texts;
  }

  public boolean start() {
    karsch = Values.getInstance().getLevelGameState().getKarsch();
    if (textNumber < texts.size()) {

      if (textNumber > 0) {
        final AudioTrack lastTrack = getTrack(textNumber - 1);

        // TODO
        // if (lastTrack != null && lastTrack.isPlaying()) {
        // lastTrack.stop();
        // }
      }

      final String text = texts.get(textNumber);
      final AudioTrack track = getTrack(textNumber);

      textNumber++;

      if (text.isEmpty()) {
        return start();
      }

      if (track != null) {
        SoundManager.getInstance().playSoundOnce(track);
      }

      if ((textNumber % 2) == 1) {
        karsch.speak(null);
        balloon.showText(text);
      } else {
        karsch.speak(text);
        balloon.showText(null);
      }
      return true;
    } else {
      stop();
      return false;
    }
  }

  private AudioTrack getTrack(final int number) {
    if (number >= 0 && speeches.size() > number) {
      return speeches.get(number);
    } else {
      return null;
    }
  }

  private void stop() {
    karsch.speak(null);
    balloon.showText(null);
    textNumber = 0;
  }
}
