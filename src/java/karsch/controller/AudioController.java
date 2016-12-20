package karsch.controller;

import java.util.ArrayList;

import karsch.characters.CharacterBase;
import karsch.sound.SoundManager;

import com.jme3.animation.AudioTrack;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

// TODO add audio track handling
public class AudioController extends AbstractControl {
	private final CharacterBase character;
	private float counter = 0;
	private final ArrayList<AudioTrack> tracks;
	private final int randomSteps;

	public AudioController(final CharacterBase character, final int randomSteps) {
		this.character = character;
		this.randomSteps = randomSteps;

		tracks = new ArrayList<AudioTrack>(3);
	}

	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {

	}

	@Override
	protected void controlUpdate(final float tpf) {
		if (counter < 1.5f) {
			counter += tpf;
		} else {

			if (SoundManager.soundOn && SoundManager.getInstance().isSfxOn()
					&& character.isVisible()) {
				final int rand = FastMath.rand.nextInt(tracks.size() + randomSteps);
				if (rand < tracks.size()) {
					// final AudioTrack track = tracks.get(rand);
					// track.setMaxVolume(0.8f);
					// track.setMaxVolume(0.8f);
					// SoundManager.getInstance().playSoundOnce(track);
				}// else play nothing
			}

			counter = 0;
		}
	}

	public void addTrack(final String filename) {
		// final AudioTrack newTrack = SoundManager.prepareTrack(filename);
		// tracks.add(newTrack);
	}
}
