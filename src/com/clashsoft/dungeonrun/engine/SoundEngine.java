package com.clashsoft.dungeonrun.engine;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import com.clashsoft.dungeonrun.DungeonRun;

public class SoundEngine
{
	public class SoundLocation
	{
		public int x;
		public int y;
		public int z;
		
		public SoundLocation(int x, int y, int z)
		{
			this.x = x;
			this.y = y;
			this.z = y;
		}
	}
	
	public DungeonRun dr;
	
	private Map<String, Sound> sounds = new HashMap<String, Sound>();
	private Map<String, Music> musics = new HashMap<String, Music>();

	public SoundEngine(DungeonRun dr)
	{
		this.dr = dr;
	}
	
	public void playSound(String sound, SoundLocation sl) throws SlickException
	{
		this.playSound(sound, sl, this.dr.gameSettings.soundVolume);
	}
	
	public void playSound(String sound, SoundLocation sl, float volume) throws SlickException
	{
		Sound s = sounds.get(sound);
		if (s == null)
		{
			s = new Sound(sound);
			sounds.put(sound, s);
		}
		s.playAt(1F, volume, sl.x, sl.y, sl.z);
	}
	
	public void playMusic(String music, boolean repeat) throws SlickException
	{
		this.playMusic(music, repeat, this.dr.gameSettings.musicVolume);
	}
	
	public void playMusic(String music, boolean repeat, float volume) throws SlickException
	{
		Music m = musics.get(music);
		if (m == null)
		{
			m = new Music(music);
			musics.put(music, m);
		}
		if (repeat)
			m.loop(1F, volume);
		else
			m.play(1F, volume);
	}
	
	public void stopMusic(String music)
	{
		Music m = musics.get(music);
		if (m != null)
			m.stop();
	}

}
