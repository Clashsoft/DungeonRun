package com.clashsoft.dungeonrun.client.engine;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import com.clashsoft.dungeonrun.server.DungeonRunServer;

public class SoundEngine
{
	public static class SoundLocation
	{
		public final int	x;
		public final int	y;
		public final int	z;
		
		public SoundLocation(int x, int y, int z)
		{
			this.x = x;
			this.y = y;
			this.z = y;
		}
	}
	
	public static final SoundLocation	DEFAULT_LOCATION	= new SoundLocation(0, 0, 0);
	
	public final DungeonRunServer				dr;
	
	private Map<String, Sound>			sounds				= new HashMap<String, Sound>();
	private Map<String, Music>			musics				= new HashMap<String, Music>();
	
	public SoundEngine(DungeonRunServer dr)
	{
		this.dr = dr;
	}
	
	public void playSoundEffect(String sound, SoundLocation sl) throws SlickException
	{
		this.playSoundEffect(sound, sl, this.dr.gameSettings.soundVolume);
	}
	
	public void playSoundEffect(String sound, SoundLocation sl, float volume) throws SlickException
	{
		Sound s = sounds.get(sound);
		if (s == null)
		{
			s = new Sound(sound);
			sounds.put(sound, s);
		}
		s.playAt(1F, volume, sl.x, sl.y, sl.z);
	}
	
	public void stopSoundEffect(String sound)
	{
		Sound s = sounds.get(sound);
		if (s != null)
			s.stop();
	}
	
	public void stopAllSoundEffects()
	{
		for (Sound s : this.sounds.values())
			s.stop();
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
	
	public void stopAllMusics()
	{
		for (Music m : this.musics.values())
			m.stop();
	}
	
	public void stopAllSounds()
	{
		stopAllSoundEffects();
		stopAllMusics();
	}
}
