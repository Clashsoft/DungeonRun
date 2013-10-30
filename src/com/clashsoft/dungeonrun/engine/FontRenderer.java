package com.clashsoft.dungeonrun.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.util.ResourceHelper;

public class FontRenderer
{
	public static final int				HEIGHT		= 9;
	
	protected Map<Character, String>	charPaths	= new HashMap();
	protected Map<Character, Image>		charMap		= new HashMap<Character, Image>();
	
	public DungeonRun					dr;
	
	public float						red			= 1F;
	public float						green		= 1F;
	public float						blue		= 1F;
	
	public FontRenderer(DungeonRun dr) throws SlickException
	{
		this.dr = dr;
		loadCharNames();
		loadChars();
	}
	
	public void loadCharNames() throws SlickException
	{
		List<String> lines = ResourceHelper.readAllLines("/resources/text/ascii/charmap.txt");
		
		for (String line : lines)
		{	
			if (line.isEmpty())
				continue;
			
			int equalsSignIndex = line.indexOf('=', 1);
			
			if (equalsSignIndex == -1)
				continue;
			
			char c = 0;
			String key = line.substring(0, equalsSignIndex);
			String path = line.substring(equalsSignIndex + 1);
			
			if (key.length() > 1)
			{
				if (key.startsWith("0x"))
					c = (char) Integer.parseInt(key.substring(2), 16);
				else
				{
					try
					{
						c = (char) Integer.parseInt(key);
					}
					catch (Exception ex)
					{
						System.err.println("Unknow char " + key + " (" + path + ")");
					}
				}
				
				if (dr.debugMode && c != 0)
					System.out.println("Unicode char " + c + " [" + key + "] (" + path + ") loaded.");
			}
			else
				c = key.charAt(0);
			
			charPaths.put(Character.valueOf(c), path);
		}
	}
	
	public void loadChars() throws SlickException
	{
		for (Character character : charPaths.keySet())
		{
			String path = charPaths.get(character);
			String charPath = "resources/text/ascii/" + path;
			
			try
			{
				Image image = new Image(charPath);
				charMap.put(character, image);
			}
			catch (Exception ex)
			{
				System.err.println("Unable to load char " + character + " (" + charPath + "): " + ex.getMessage());
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
	
	public void drawStringWithShadow(int x, int y, String text)
	{
		drawStringWithShadow(x, y, text, 0xFFFFFF);
	}
	
	public void drawStringWithShadow(int x, int y, String text, int color)
	{
		drawString(x, y, text, color, true);
	}
	
	public void drawString(int x, int y, String text, int color, boolean shadow)
	{
		GL11.glPushMatrix();
		
		red = ((color >> 16) & 255) / 255F;
		green = ((color >> 8) & 255) / 255F;
		blue = (color & 255) / 255F;
		
		for (int i = 0; i < text.length(); i++)
		{
			x += drawChar(x, y, text.charAt(i), shadow) + 1;
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
