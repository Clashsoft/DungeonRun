package com.clashsoft.dungeonrun.gui;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.util.DimensionHelper.Size2;

public class GameSettings
{
	public static File		optionsFile		= new File(DungeonRun.getSaveDataFolder(), "options.txt");
	
	// General Options
	public float			musicVolume		= 1F;
	public float			soundVolume		= 1F;
	
	// Video options
	public Size2<Integer>	resolution		= new Size2<Integer>(800, 450);
	public boolean			fullScreen		= false;
	public boolean			useVSync		= true;
	
	public boolean			renderHitBoxes	= false;
	
	public GameSettings()
	{
		load();
	}
	
	public void updateGame() throws SlickException
	{
		DungeonRun dr = DungeonRun.instance;
		dr.setResolution(resolution.width, resolution.heigth);
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
		
		this.musicVolume = Float.parseFloat(props.getProperty("music", "0"));
		this.soundVolume = Float.parseFloat(props.getProperty("sound", "0"));
		
		this.resolution.width = Integer.parseInt(props.getProperty("resolutionX", "800"));
		this.resolution.heigth = Integer.parseInt(props.getProperty("resolutionY", "450"));
		
		this.fullScreen = Boolean.parseBoolean(props.getProperty("fullscreen", "false"));
		this.useVSync = Boolean.parseBoolean(props.getProperty("vsync", "true"));
		this.renderHitBoxes = Boolean.parseBoolean(props.getProperty("renderhitboxes", "false"));
	}
	
	public void save()
	{
		Properties props = new Properties();
		props.setProperty("music", musicVolume + "");
		props.setProperty("sound", soundVolume + "");
		
		props.setProperty("resolutionX", resolution.width + "");
		props.setProperty("resolutionY", resolution.heigth + "");
		
		props.setProperty("fullscreen", fullScreen + "");
		props.setProperty("vsync", useVSync + "");
		props.setProperty("renderhitboxes", renderHitBoxes + "");
	}
}
