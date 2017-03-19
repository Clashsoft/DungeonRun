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
	public static Image introBackground;
	public static Image title;
	public static Image icons;

	public static Image hotbar;
	public static Image hotbarSelection;

	public static SpriteSheet playerSprites;
	public static SpriteSheet monsterSprites;
	public static SpriteSheet npcSprites;
	public static SpriteSheet iconsSprite;

	public static void setupTextures() throws SlickException
	{
		playerSprites = new SpriteSheet(loadTexture("resources/textures/entity/knights.png"), 12, 24);
		monsterSprites = new SpriteSheet(loadTexture("resources/textures/entity/enemies.png"), 32, 32);
		npcSprites = new SpriteSheet(loadTexture("resources/textures/entity/npcs.png"), 16, 28);

		title = loadTexture("resources/textures/gui/title.png");
		introBackground = loadTexture("resources/textures/gui/intro_background.png");
		icons = loadTexture("resources/textures/gui/icons.png");
		iconsSprite = new SpriteSheet(icons, 18, 18);

		hotbar = loadTexture("resources/textures/gui/hotbar.png");
		hotbarSelection = loadTexture("resources/textures/gui/hotbar_selection.png");
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
