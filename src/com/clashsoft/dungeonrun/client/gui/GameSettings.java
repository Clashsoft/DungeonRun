package com.clashsoft.dungeonrun.client.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.client.DungeonRunClient;

public class GameSettings
{
	protected DungeonRun	dr;
	
	protected final File	optionsFile;
	
	// General Options
	public String			language		= "en_US";
	
	public float			musicVolume		= 1F;
	public float			soundVolume		= 1F;
	
	// Video options
	public int				guiSize			= 0;
	public boolean			fullScreen		= false;
	public boolean			useVSync		= true;
	
	public boolean			renderHitBoxes	= false;
	
	public boolean			debugMode		= true;
	
	public GameSettings(DungeonRun dr)
	{
		this.dr = dr;
		this.optionsFile = new File(dr.getSaveDataFolder(), "options.txt");
		this.load();
	}
	
	public void updateGame() throws SlickException
	{
		DungeonRunClient dr = DungeonRunClient.instance;
		dr.setFullScreen(fullScreen);
		dr.setVSync(useVSync);
	}
	
	public void load()
	{
		Properties props = new Properties();
		try
		{
			if (!optionsFile.exists())
				optionsFile.createNewFile();
			
			props.load(new FileInputStream(optionsFile));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		this.musicVolume = Float.parseFloat(props.getProperty("music", "1"));
		this.soundVolume = Float.parseFloat(props.getProperty("sound", "1"));
		
		this.guiSize = Integer.parseInt(props.getProperty("guisize", "0"));
		
		this.fullScreen = Boolean.parseBoolean(props.getProperty("fullscreen", "false"));
		this.useVSync = Boolean.parseBoolean(props.getProperty("vsync", "true"));
		this.renderHitBoxes = Boolean.parseBoolean(props.getProperty("renderhitboxes", "false"));
		
		this.debugMode = Boolean.parseBoolean(props.getProperty("debug", "false"));
	}
	
	public void save()
	{
		Properties props = new Properties();
		props.setProperty("music", musicVolume + "");
		props.setProperty("sound", soundVolume + "");
		
		props.setProperty("guisize", guiSize + "");
		
		props.setProperty("fullscreen", fullScreen + "");
		props.setProperty("vsync", useVSync + "");
		props.setProperty("renderhitboxes", renderHitBoxes + "");
		
		props.setProperty("debug", debugMode + "");
		
		try
		{
			props.store(new FileOutputStream(optionsFile), "");
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
