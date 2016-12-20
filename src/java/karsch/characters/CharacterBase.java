package karsch.characters;

import karsch.Values;
import karsch.controller.AudioController;
import karsch.controller.NPCController;
import karsch.level.LevelMap;

import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

// TODO add a controller for animations
// TODO add a controller for positional audio
public class CharacterBase extends Node {
  // protected SpatialTransformer trans;
  protected LevelMap levelMap;
  protected int x, y;
  protected Spatial model;
  // protected RangedAudioTracker tracker;
  // protected AudioTrack track;

  protected NPCController controller;
  protected AudioController audioController;

  public LevelMap getLevelMap() {
    return levelMap;
  }

  public CharacterBase(final int x, final int y, final LevelMap levelMap,
      final String name) {
    super(name + String.valueOf(x) + String.valueOf(y));
    this.x = x;
    this.y = y;
    this.levelMap = levelMap;
  }

  public CharacterBase(final String name, final int x, final int y) {
    super(name + String.valueOf(x) + String.valueOf(y));
    this.x = x;
    this.y = y;
  }

  public void setPause(final boolean pause) {
    // if (controller != null)
    // controller.setActive(!pause);
    // if (trans != null)
    // trans.setActive(!pause);
    // if (track != null){
    // if (pause){
    // track.stop();
    // } else {
    // track.play();
    // }
    // }
    // if (audioController != null)
    // audioController.setActive(!pause);
  }

  protected void addSound(final String baseName, final int min, final int max,
      final int randomSteps) {

    audioController = new AudioController(this, randomSteps);
    addControl(audioController);

    for (int i = min; i <= max; i++) {
      audioController.addTrack(baseName + i + ".ogg");
    }

  }

  public boolean isVisible() {
    updateModelBound();

    final Camera cam = Values.getInstance().getLevelGameState().getCam();
    final int planeState = cam.getPlaneState();
    cam.setPlaneState(0);
    final boolean visible = cam.contains(getWorldBound()) == Camera.FrustumIntersect.Inside;
    cam.setPlaneState(planeState);

    return visible;
  }

  public int getX() {
    return x;
  }

  public void setX(final int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(final int y) {
    this.y = y;
  }

}
