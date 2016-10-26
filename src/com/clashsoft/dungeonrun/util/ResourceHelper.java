package com.clashsoft.dungeonrun.util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.ResourceLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResourceHelper
{
	public static Image       introBackground;
	public static Image       buttons;
	public static Image       icons;
	public static SpriteSheet playerSprites;
	public static SpriteSheet monsterSprites;
	public static SpriteSheet iconsSprite;

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
		playerSprites = new SpriteSheet(loadTexture("resources/textures/entity/knights.png"), 12, 24);
		monsterSprites = new SpriteSheet(loadTexture("resources/textures/entity/enemies.png"), 32, 32);

		introBackground = loadTexture("resources/textures/gui/intro_background.png");
		buttons = loadTexture("resources/textures/gui/buttons.png");
		icons = loadTexture("resources/textures/gui/icons.png");
		iconsSprite = new SpriteSheet(icons, 18, 18);
	}

	public static List<String> readAllLines(String resource)
	{
		List<String> lines = new ArrayList<>();

		InputStream is = ResourceLoader.getResourceAsStream(resource);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try
		{
			while (true)
			{
				String line = reader.readLine();
				if (line == null)
				{
					break;
				}
				lines.add(line);
			}
		}
		catch (Exception ignored)
		{
		}

		return lines;
	}

	public static Image loadTexture(String resource) throws SlickException
	{
		return new Image(resource, false, Image.FILTER_NEAREST);
	}
}
