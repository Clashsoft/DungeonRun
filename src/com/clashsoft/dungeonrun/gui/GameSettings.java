package com.clashsoft.dungeonrun.gui;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.util.DimensionHelper.Size2;

public class GameSettings
{
	// General Options
	public float			musicVolume		= 1F;
	public float			soundVolume		= 1F;
	
	// Video options
	public Size2<Integer>	resolution		= new Size2<Integer>(800, 450);
	public Boolean			fullScreen		= false;
	public Boolean			useVSync		= false;
	
	public Boolean			renderHitBoxes	= false;
	
	public void updateGame() throws SlickException
	{
		DungeonRun dr = DungeonRun.instance;
		dr.setResolution(resolution.width, resolution.heigth);
		// dr.setFullScreen(fullScreen);
		dr.setVSync(useVSync);
	}
}
