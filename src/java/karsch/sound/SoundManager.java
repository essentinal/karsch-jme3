package karsch.sound;

import karsch.Values;

import com.jme3.animation.AudioTrack;

// TODO reimplement for JME3 sound system
public class SoundManager {
	public static boolean soundOn = true;
	private boolean musicOn = true;
	private boolean sfxOn = true;

	private int sfxVolume = 100;
	private int musicVolume = 100;
	private final Values values = Values.getInstance();
	// private MusicTrackQueue musicTrackQueue;
	// private EnvironmentalPool environmentalPool;
	private static SoundManager instance;

	private SoundManager() {
		// try {
		// sfxVolume = values.getSettings().getInt("SFXVOLUME", musicVolume);
		// musicVolume = values.getSettings().getInt("MUSICVOLUME", musicVolume);
		//
		// musicTrackQueue = AudioSystem.getSystem().getMusicQueue();
		// musicTrackQueue.setCrossfadeinTime(.1f);
		// musicTrackQueue.setRepeatType(RepeatType.ALL);
		//
		// environmentalPool = AudioSystem.getSystem().getEnvironmentalPool();
		//
		// System.out.println("SoundManager - AudioSystem created: "
		// + AudioSystem.isCreated());
		//
		// setMusicOn(values.getSettings().isMusic());
		// setSfxOn(values.getSettings().isSFX());
		// setMusicVolume(values.getSettings().getInt("MUSICVOLUME", 100));
		// setSfxVolume(values.getSettings().getInt("SFXVOLUME", 100));
		// } catch (final Throwable t) {
		// SoundManager.soundOn = false;
		// t.printStackTrace();
		// }

	}

	public static SoundManager getInstance() {
		if (instance == null)
			instance = new SoundManager();

		return instance;
	}

	public void playCreditMusic() {
		if (SoundManager.soundOn && isMusicOn()) {
			stopMusic();

			// musicTrackQueue.clearTracks();
			// addTrack("track2.ogg", true).setLooping(false);
			// musicTrackQueue.setRepeatType(RepeatType.NONE);
			// musicTrackQueue.play();
		}
	}

	public AudioTrack addTrack(final String file, final boolean stream) {
		if (SoundManager.soundOn) {
			final AudioTrack track = null;
			// try {
			// track = AudioSystem.getSystem().createAudioTrack(
			// ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_AUDIO,
			// file), stream);
			// track.setType(TrackType.MUSIC);
			// track.setRelative(false);
			// track.setLooping(false);
			// track.setVolume(getMusicVolume() / 100f);
			// track.setTargetVolume(getMusicVolume() / 100f);
			//
			// musicTrackQueue.addTrack(track);
			//
			// if (!isMusicOn())
			// musicTrackQueue.stop();
			// } catch (final Throwable t) {
			// SoundManager.soundOn = false;
			// t.printStackTrace();
			// return null;
			// }

			return track;
		} else {
			return null;
		}
	}

	public void playEnvironmentalSound(final String file) {
		if (SoundManager.soundOn) {
			final AudioTrack track = null;
			// try {
			// track = AudioSystem.getSystem().createAudioTrack(
			// ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_AUDIO,
			// file), false);
			// track.setType(TrackType.ENVIRONMENT);
			// track.setRelative(false);
			// track.setLooping(false);
			// track.setVolume(getSfxVolume() / 100f);
			// track.setTargetVolume(getSfxVolume() / 100f);
			// environmentalPool.addTrack(track);
			// } catch (final Throwable t) {
			// SoundManager.soundOn = false;
			// t.printStackTrace();
			// }
		}
	}

	public static AudioTrack prepareTrack(final String file) {
		if (SoundManager.soundOn) {
			final AudioTrack track = null;
			// try {
			// track = AudioSystem.getSystem().createAudioTrack(
			// ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_AUDIO,
			// file), false);
			//
			// } catch (final Throwable t) {
			// SoundManager.soundOn = false;
			// t.printStackTrace();
			// }
			return track;
		} else {
			return null;
		}
	}

	public void playSoundOnce(final String file) {
		if (SoundManager.soundOn) {
			final AudioTrack track = prepareTrack(file);
			playSoundOnce(track);
		}
	}

	public void playSoundOnce(final AudioTrack track) {
		// if (SoundManager.soundOn) {
		// track.setType(TrackType.HEADSPACE);
		// track.setRelative(false);
		// track.setLooping(false);
		// track.setVolume(getSfxVolume() / 100f);
		// track.setTargetVolume(getSfxVolume() / 100f);
		//
		// if (isSfxOn())
		// track.play();
		// }
	}

	public boolean isMusicPlaying() {
		// if (SoundManager.soundOn)
		// return musicTrackQueue.isPlaying();
		// else
		return false;
	}

	public void playMusic() {
		if (SoundManager.soundOn && isMusicOn()) {
			stopMusic();
			// musicTrackQueue.clearTracks();
			// musicTrackQueue.setRepeatType(RepeatType.ONE);
			// addTrack("track1.ogg", true);
			// musicTrackQueue.play();
		}
	}

	public void pauseMusic() {
		// if (SoundManager.soundOn)
		// musicTrackQueue.pause();
	}

	public void stopMusic() {
		// if (SoundManager.soundOn)
		// musicTrackQueue.fadeOutAndClear(1);
	}

	public boolean isMusicOn() {
		return musicOn;
	}

	public void setMusicOn(final boolean musicOn) {
		if (SoundManager.soundOn) {
			this.musicOn = musicOn;
			// values.getSettings().setMusic(musicOn);
			if (musicOn) {
				if (!isMusicPlaying()) {
					playMusic();
				}
			} else {
				stopMusic();
			}
			System.out.println("Set Music " + musicOn);
		}
	}

	public boolean isSfxOn() {
		return sfxOn;
	}

	public void setSfxOn(final boolean sfxOn) {
		if (SoundManager.soundOn) {
			this.sfxOn = sfxOn;
			// values.getSettings().setSFX(sfxOn);

			int newVol;
			if (sfxOn)
				newVol = getSfxVolume();
			else
				newVol = 0;

			// final Iterator<?> it = environmentalPool.getTrackList().iterator();
			// while (it.hasNext()) {
			// final AudioTrack actTrack = (AudioTrack) it.next();
			// actTrack.setVolume(newVol / 100f);
			// actTrack.setMaxVolume(newVol / 100f);
			// System.out.println(actTrack.getTargetVolume());
			// }
		}
	}

	public int getSfxVolume() {
		return sfxVolume;
	}

	public void setSfxVolume(final int sfxVolume) {
		if (SoundManager.soundOn) {
			this.sfxVolume = sfxVolume;
			// values.getSettings().setInt("SFXVOLUME", sfxVolume);
			// final Iterator<?> it = environmentalPool.getTrackList().iterator();
			// while (it.hasNext()) {
			// final AudioTrack actTrack = (AudioTrack) it.next();
			// actTrack.setVolume(sfxVolume / 100f);
			// actTrack.setMaxVolume(sfxVolume / 100f);
			// System.out.println(actTrack.getTargetVolume());
			// }
		}
	}

	public int getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(final int musicVolume) {
		if (SoundManager.soundOn) {
			this.musicVolume = musicVolume;
			// values.getSettings().setInt("MUSICVOLUME", musicVolume);
			// final Iterator<?> it = musicTrackQueue.getTrackList().iterator();
			// while (it.hasNext()) {
			// final AudioTrack actTrack = (AudioTrack) it.next();
			// actTrack.setVolume(musicVolume / 100f);
			// actTrack.setTargetVolume(musicVolume / 100f);
			// System.out.println(actTrack.getTargetVolume());
			// }
		}
	}
}
