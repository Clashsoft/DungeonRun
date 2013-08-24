package com.clashsoft.dungeonrun.util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class ResourceRegistry
{
	public static Image			chaotic_development_bg;
	public static Image			buttons;
	public static Image			icons;
	public static SpriteSheet	iconsSprite;
	
	static
	{
		try
		{
			setupTextures();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setupTextures() throws SlickException
	{
		chaotic_development_bg = new Image("resources/gui/cdbg.png");
		buttons = new Image("resources/gui/buttons.png");
		icons = new Image("resources/gui/icons.png");
		iconsSprite = new SpriteSheet(icons, 18, 18);
	}
}
