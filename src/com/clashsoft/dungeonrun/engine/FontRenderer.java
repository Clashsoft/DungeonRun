package com.clashsoft.dungeonrun.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;

import com.clashsoft.dungeonrun.DungeonRun;

public class FontRenderer
{
	public static final int			HEIGHT	= 9;
	
	protected Properties			charNames = null;
	protected Map<Character, Image>	charMap	= new HashMap<Character, Image>();
	
	public DungeonRun				dr;
	
	public float red = 1F;
	public float green = 1F;
	public float blue = 1F;
	
	public FontRenderer(DungeonRun dr) throws SlickException
	{
		this.dr = dr;
		
		this.charNames = loadCharNames();
		loadChars();
	}
	
	public Properties loadCharNames() throws SlickException
	{
		Properties props = new Properties();
		try
		{
			props.load(ResourceLoader.getResourceAsStream("/resources/text/ascii/charmap.txt"));
		}
		catch (IOException ex)
		{
			throw new SlickException("Unable to load font data: " + ex.getMessage(), ex);
		}
		return props;
	}
	
	public void loadChars() throws SlickException
	{	
		for (Object k : charNames.keySet())
		{
			String key = (String)k;
			String charPath = "resources/text/ascii/" + charNames.getProperty((String)key);
			
			if ("equals".equalsIgnoreCase(key))
				key = "=";
			else if ("backslash".equalsIgnoreCase(key))
				key = "\\";
			else if ("colon".equalsIgnoreCase(key))
				key = ":";
			else if ("numbersign".equalsIgnoreCase(key))
				key = "#";
			else if ("space".equalsIgnoreCase(key))
				key = " ";
				
			try
			{
				Image image = new Image(charPath);
				charMap.put(Character.valueOf(key.charAt(0)), image);
			}
			catch (Exception ex)
			{
				System.out.println("Unable to load char " + key + " (" + charPath + "): " + ex.getMessage());
			}
		}
	}
	
	public void drawString(int x, int y, String text)
	{
		drawString(x, y, text, 0xFFFFFF);
	}
	
	public void drawString(int x, int y, String text, int color)
	{
		drawString(x, y, text, color, false);
	}
	
	public void drawString(int x, int y, String text, int color, boolean shadow)
	{
		GL11.glPushMatrix();
		
		red = ((color >> 16) & 255) / 255F;
		green = ((color >> 8) & 255) / 255F;
		blue = (color & 255) / 255F;
		
		for (int i = 0; i < text.length(); i++)
		{
			x += drawChar(x, y + 4, text.charAt(i), shadow) + 1;
		}
		
		GL11.glPopMatrix();
	}
	
	public int drawChar(int x, int y, char c, boolean shadow)
	{
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		Image image = charMap.get(Character.valueOf(c));
		
		if (shadow)
		{
			image.setImageColor(red / 4F, green / 4F, blue / 4F);
			image.draw(x + 1, y + 1);
		}
		
		image.setImageColor(red, green, blue);
		image.draw(x, y);
		
		return image.getWidth();
	}
	
	public int getStringWidth(String text)
	{
		int width = 0;
		for (int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			width += getCharWidth(c);
			if (i != text.length() - 1)
				width++;
		}
		return width;
	}
	
	public int getCharWidth(char c)
	{
		return charMap.get(Character.valueOf(c)).getWidth();
	}
	
	public int getStringHeigth(String message)
	{
		return HEIGHT;
	}
}
