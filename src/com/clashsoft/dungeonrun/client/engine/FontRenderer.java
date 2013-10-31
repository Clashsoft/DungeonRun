package com.clashsoft.dungeonrun.client.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.server.DungeonRunServer;
import com.clashsoft.dungeonrun.util.ResourceHelper;

public class FontRenderer
{
	public static final int				HEIGHT		= 9;
	
	protected Map<Character, String>	charPaths	= new HashMap();
	protected Map<Character, Image>		charMap		= new HashMap<Character, Image>();
	
	/**
	 * The DungeonRun instance
	 */
	public DungeonRunServer				dr;
	
	public boolean						globalUnicode;
	
	/**
	 * Color table for predefined color access via §[0-9A-F]
	 */
	public int[]						colorTable	= new int[16];
	
	/**
	 * Used in FontRenderer#getStringWidth(String). Setting this flag to false
	 * will disable all character rendering.
	 */
	public boolean						draw		= true;
	
	/**
	 * Current Font color.
	 */
	public float						red			= 1F, green = 1F, blue = 1F;
	
	/**
	 * Determines if the currently rendered string is rendered with a shadow
	 */
	public boolean						shadow		= false;
	
	/**
	 * Format flags.
	 */
	public boolean						italic, bold, strikeThrough, underline, unicode;
	
	public FontRenderer(DungeonRunServer dr) throws SlickException
	{
		this.dr = dr;
		loadColorTable();
		loadCharNames();
		loadChars();
	}
	
	public void loadColorTable()
	{
		for (int i = 0; i < 16; i++)
		{
			int light = (i >> 3 & 1) * 85;
			int r = (i >> 2 & 1) * 170 + light;
			int g = (i >> 1 & 1) * 170 + light;
			int b = (i & 1) * 170 + light;
			
			if (i == 6)
				r += 85;
			
			this.colorTable[i] = (r & 255) << 16 | (g & 255) << 8 | b & 255;
		}
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
				
				if (dr.gameSettings.debugMode && c != 0)
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
	
	public void setColor_I(int color)
	{
		red = ((color >> 16) & 255) / 255F;
		green = ((color >> 8) & 255) / 255F;
		blue = (color & 255) / 255F;
	}
	
	public void setColor_F(float r, float g, float b)
	{
		red = r;
		green = g;
		blue = b;
	}
	
	public void setColor_S(String color)
	{
		try
		{
			if (color.startsWith("0x"))
				setColor_I(Integer.parseInt(color.substring(2), 16));
			else if (color.contains(";"))
			{
				float r = 1F;
				float g = 1F;
				float b = 1F;
				
				String[] split = color.split(";");
				
				if (split.length >= 1)
					r = Float.parseFloat(split[0]);
				if (split.length >= 2)
					g = Float.parseFloat(split[1]);
				if (split.length >= 3)
					b = Float.parseFloat(split[2]);
				
				setColor_F(r, g, b);
			}
			else
				setColor_I(Integer.parseInt(color));
		}
		catch (NumberFormatException ex)
		{
		}
	}
	
	public void resetStyles()
	{
		this.shadow = false;
		this.italic = false;
		this.bold = false;
		this.strikeThrough = false;
		this.underline = false;
		this.unicode = false;
		this.setColor_F(1F, 1F, 1F);
	}
	
	public float drawString(int x, int y, String text)
	{
		return drawString(x, y, text, 0xFFFFFF);
	}
	
	public float drawString(int x, int y, String text, int color)
	{
		return drawString(x, y, text, color, false);
	}
	
	public float drawStringWithShadow(int x, int y, String text)
	{
		return drawStringWithShadow(x, y, text, 0xFFFFFF);
	}
	
	public float drawStringWithShadow(int x, int y, String text, int color)
	{
		return drawString(x, y, text, color, true);
	}
	
	public float drawString(float x, float y, String text, int color, boolean shadow)
	{
		GL11.glPushMatrix();
		
		this.resetStyles();
		this.setColor_I(color);
		this.shadow = shadow;
		
		text = replaceLocalizations(text);
		
		for (int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			
			if (c == '\u00A7' && i + 2 != text.length())
			{
				char c1 = text.charAt(i + 1);
				int i1 = "0123456789ABCDEFbcisSuUr".indexOf(c1);
				
				if (i1 != -1)
				{
					if (i1 < 16) // 0-9 A-F
						this.setColor_I(this.colorTable[i1]);
					else if (i1 == 16) // b
						this.bold = !this.bold;
					else if (i1 == 17) // c
					{
						int i2 = text.indexOf('[', i + 2);
						int i3 = text.indexOf(']', i + 3);
						if (i2 != -1 && i3 != -1)
						{
							String s1 = text.substring(i2 + 1, i3);
							setColor_S(s1);
							i = i3;
							continue;
						}
					}
					else if (i1 == 18) // i
						this.italic = !this.italic;
					else if (i1 == 19) // s
						this.strikeThrough = !this.strikeThrough;
					else if (i1 == 20) // S
						this.shadow = !this.shadow;
					else if (i1 == 21) // u
						this.underline = !this.underline;
					else if (i1 == 22) // Us
						this.unicode = !this.unicode;
					else if (i1 == 23) // r
						this.resetStyles();
					
					i++;
					continue;
				}
				else if (c1 == '\u00a7')
					continue;
			}
			
			x += drawChar(x, y, c) + 1;
		}
		x--;
		GL11.glPopMatrix();
		
		return x;
	}
	
	public String replaceLocalizations(String text)
	{
		String text1 = new String(text);
		for (int i = 0; i < text1.length(); i++)
		{
			char c = text1.charAt(i);
			
			if (c == '#')
			{
				int end = text1.indexOf(' ', i + 1);
				if (end == -1)
					end = text1.length();
				if (end == i + 1)
					continue;
				
				String key = text1.substring(i + 1, end);
				String translatedKey = I18n.getString(key.replace('_', '.'));
				
				text = text.replace("#" + key, translatedKey);
				
				i = end;
			}
		}
		return text;
	}
	
	public float drawChar(float x, float y, char c)
	{
		Image image = charMap.get(Character.valueOf(c));
		
		if (globalUnicode || unicode || image == null)
			return drawUnicodeChar(x, y, c);
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		float b = this.bold ? 1.25F : 1F;
		
		if (draw)
		{
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glScalef(b, HEIGHT / height, 1F);
			
			if (this.shadow)
			{
				this.shadow = false;
				this.red /= 4;
				this.green /= 4;
				this.blue /= 4;
				
				this.drawChar(x + 1, y + 1, c);
				
				this.red *= 4;
				this.green *= 4;
				this.blue *= 4;
				this.shadow = true;
			}
			
			GL11.glColor3f(red, green, blue);
			image.drawSheared(x / b, y, this.italic ? -1.5F : 0F, 0, null);
			
			if (this.strikeThrough || this.underline)
			{
				this.dr.renderEngine.graphics.setColor(new Color(red, green, blue));
				
				if (this.strikeThrough)
					this.dr.renderEngine.graphics.drawLine(x, y + 3, x + width, y + 3);
				if (this.underline)
					this.dr.renderEngine.graphics.drawLine(x, y + 8, x + width, y + 8);
			}
			
			GL11.glScalef(1F / b, height / HEIGHT, 1F);
		}
		
		return width * b;
	}
	
	public float drawUnicodeChar(float x, float y, char c)
	{
		Graphics g = this.dr.renderEngine.graphics;
		
		String text = String.valueOf(c);
		float width = g.getFont().getWidth(text) / 2F;
		
		float b = this.bold ? 1.25F : 1F;
		
		if (draw)
		{
			GL11.glScalef(b, 0.5F, 1F);
			g.drawString(text, x / b, y * 2F);
			GL11.glScalef(1F / b, 2F, 1F);
		}
		
		return width * b;
	}
	
	public int getStringWidth(String text)
	{
		this.draw = false;
		float f = drawString(0, 0, text);
		this.draw = true;
		return (int) f;
	}
	
	public int getCharWidth(char c)
	{
		this.draw = false;
		float f = drawChar(0, 0, c);
		this.draw = true;
		return (int) f;
	}
	
	public int getStringHeigth(String text)
	{
		return HEIGHT;
	}
}
