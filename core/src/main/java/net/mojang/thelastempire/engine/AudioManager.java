package net.mojang.thelastempire.engine;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

	private Sound getSound(String name) {
		Sound sound = Resources.getSound(name);
		if (sound == null) {
			System.out.println("[AudioManager] getSound('" + name + "') is null!");
		}
		return sound;
	}
	
	private Music getMusic(String name) {
		Music music = Resources.getMusic(name);
		if (music == null) {
			System.out.println("[AudioManager] getMusic('" + name + "') is null!");
		}
		return music;
	}

	public void playSound(String name, float volume, float pitch) {
		Sound sound = getSound(name);
		if (sound != null) {
			sound.play(volume, pitch, 0f);
		}
	}
	
	public void playMusic(String name, boolean looping) {
		Music music = getMusic(name);
		if (music != null) {
			if (music.isLooping() != looping) {				
				music.setLooping(looping);
			}
			music.play();
		}
	}
	
	public void stopMusic(String name) {
		Music music = getMusic(name);
		if (music != null) {
			music.stop();
		}
	}

}
