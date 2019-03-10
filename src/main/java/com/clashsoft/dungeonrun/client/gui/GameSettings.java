package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.client.DungeonRunClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class GameSettings
{
	private static final String VOLUME_MUSIC    = "volume.music";
	private static final String VOLUME_SOUND    = "volume.sound";
	private static final String GUI_SIZE        = "graphics.gui.size";
	private static final String FULLSCREEN      = "graphics.fullscreen";
	private static final String VSYNC           = "graphics.vsync";
	private static final String RENDER_HITBOXES = "graphics.ingame.hitboxes";
	private static final String DEBUG_MODE      = "debug";

	protected final DungeonRun dr;

	protected final File optionsFile;

	// General Options
	public String language = "en_US";

	public float musicVolume = 1F;
	public float soundVolume = 1F;

	// Video options
	public int     guiSize    = 0;
	public boolean fullScreen = false;
	public boolean useVSync   = true;

	public boolean renderHitBoxes = false;

	public boolean debugMode = true;

	public GameSettings(DungeonRun dr)
	{
		this.dr = dr;
		this.optionsFile = new File(dr.getSaveDataFolder(), "options.txt");
		this.load();
	}

	public void updateGame()
	{
		DungeonRunClient dr = DungeonRunClient.instance;
		dr.setFullScreen(this.fullScreen);
		dr.setVSync(this.useVSync);
	}

	public void load()
	{
		if (!this.optionsFile.exists())
		{
			return;
		}

		Properties props = new Properties();
		try
		{
			props.load(new FileInputStream(this.optionsFile));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		for (Map.Entry<Object, Object> entry : props.entrySet())
		{
			final String value = entry.getValue().toString();
			switch (entry.getKey().toString())
			{
			case VOLUME_MUSIC:
				this.musicVolume = Float.parseFloat(value);
				break;
			case VOLUME_SOUND:
				this.soundVolume = Float.parseFloat(value);
				break;
			case GUI_SIZE:
				this.guiSize = Integer.parseInt(value);
				break;
			case FULLSCREEN:
				this.fullScreen = Boolean.parseBoolean(value);
				break;
			case VSYNC:
				this.useVSync = Boolean.parseBoolean(value);
				break;
			case RENDER_HITBOXES:
				this.renderHitBoxes = Boolean.parseBoolean(value);
				break;
			case DEBUG_MODE:
				this.debugMode = Boolean.parseBoolean(value);
				break;
			}
		}
	}

	public void save()
	{
		Properties props = new Properties();
		props.setProperty(VOLUME_MUSIC, this.musicVolume + "");
		props.setProperty(VOLUME_SOUND, this.soundVolume + "");

		props.setProperty(GUI_SIZE, this.guiSize + "");

		props.setProperty(FULLSCREEN, this.fullScreen + "");
		props.setProperty(VSYNC, this.useVSync + "");
		props.setProperty(RENDER_HITBOXES, this.renderHitBoxes + "");

		props.setProperty(DEBUG_MODE, this.debugMode + "");

		try
		{
			props.store(new FileOutputStream(this.optionsFile), "DungeonRun configuration file");
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
