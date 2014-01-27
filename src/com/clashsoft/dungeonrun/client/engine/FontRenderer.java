package com.clashsoft.dungeonrun.client.engine;

import java.util.*;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.util.ResourceHelper;

public class FontRenderer
{
	private static final int			HEIGHT			= 9;
	
	protected Map<Character, String>	charPaths		= new HashMap();
	protected List<Character>			chars			= new ArrayList();
	protected Map<Character, Image>		charMap			= new HashMap<Character, Image>();
	
	/**
	 * The DungeonRun instance
	 */
	public DungeonRunClient				dr;
	
	public Graphics						graphics;
	public Random						fontRandom;
	
	public String						fontName;
	
	/**
	 * Color table for predefined color access via §[0-9A-F]
	 */
	public int[]						colorTable		= new int[16];
	
	/**
	 * Used in FontRenderer#getStringWidth(String). Setting this flag to false
	 * will disable all character rendering.
	 */
	public boolean						draw			= true;
	
	/**
	 * Current Font color.
	 */
	public float						red				= 1F, green = 1F, blue = 1F, alpha = 1F;
	
	/**
	 * Determines if this FontRenderer uses unicode font rendering
	 */
	public boolean						globalUnicode;
	
	/**
	 * Determines if the currently rendered string is rendered with a shadow
	 */
	public boolean						globalShadow	= false;
	
	/**
	 * Format flags.
	 */
	public boolean						italic, bold, strikeThrough, underline, shadow, unicode, obfuscated;
	
	public FontRenderer(DungeonRunClient dr, String fontName) throws SlickException
	{
		this.dr = dr;
		this.graphics = dr.getGraphics();
		this.fontRandom = new Random(fontName.hashCode());
		this.fontName = fontName;
		
		this.loadColorTable();
		this.loadCharNames();
		this.loadChars();
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
			{
				r += 85;
			}
			
			this.colorTable[i] = (r & 255) << 16 | (g & 255) << 8 | b & 255;
		}
	}
	
	public void loadCharNames() throws SlickException
	{
		List<String> lines = ResourceHelper.readAllLines("/resources/text/" + this.fontName + "/charmap.txt");
		
		for (String line : lines)
		{
			if (line.isEmpty())
			{
				continue;
			}
			
			int equalsSignIndex = line.indexOf('=', 1);
			
			if (equalsSignIndex == -1)
			{
				continue;
			}
			
			char c = 0;
			String key = line.substring(0, equalsSignIndex);
			String path = line.substring(equalsSignIndex + 1);
			
			if (key.length() > 1)
			{
				if (key.startsWith("0x"))
				{
					c = (char) Integer.parseInt(key.substring(2), 16);
				}
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
				
				if (this.dr.gameSettings.debugMode && c != 0)
				{
					System.out.println("Unicode char " + c + " [" + key + "] (" + path + ") loaded.");
				}
			}
			else
			{
				c = key.charAt(0);
			}
			
			Character character = Character.valueOf(c);
			this.chars.add(character);
			this.charPaths.put(character, path);
		}
	}
	
	public void loadChars() throws SlickException
	{
		for (Character character : this.chars)
		{
			String path = this.charPaths.get(character);
			String charPath = "resources/text/" + this.fontName + "/" + path;
			
			try
			{
				Image image = new Image(charPath);
				this.charMap.put(character, image);
			}
			catch (Exception ex)
			{
				System.err.println("Unable to load char " + character + " (" + charPath + "): " + ex.getMessage());
			}
		}
	}
	
	public void setColor_I(int color)
	{
		float a = (color >> 24 & 255) / 255F;
		float r = (color >> 16 & 255) / 255F;
		float g = (color >> 8 & 255) / 255F;
		float b = (color & 255) / 255F;
		this.setColor_F(r, g, b, a == 0F ? 1F : a);
	}
	
	public void setColor_F(float r, float g, float b)
	{
		this.setColor_F(r, g, b, this.alpha);
	}
	
	public void setColor_F(float r, float g, float b, float a)
	{
		this.red = r;
		this.green = g;
		this.blue = b;
		this.alpha = a;
	}
	
	public void setColor_S(String color)
	{
		try
		{
			if (color.startsWith("0x"))
			{
				this.setColor_I(Integer.parseInt(color.substring(2), 16));
			}
			else if (color.contains(";"))
			{
				float r = 1F;
				float g = 1F;
				float b = 1F;
				float a = 1F;
				
				String[] split = color.split(";");
				
				if (split.length >= 1)
				{
					r = Float.parseFloat(split[0]);
				}
				if (split.length >= 2)
				{
					g = Float.parseFloat(split[1]);
				}
				if (split.length >= 3)
				{
					b = Float.parseFloat(split[2]);
				}
				if (split.length >= 4)
				{
					a = Float.parseFloat(split[3]);
				}
				
				this.setColor_F(r, g, b, a);
			}
			else
			{
				this.setColor_I(Integer.parseInt(color));
			}
		}
		catch (NumberFormatException ex)
		{
		}
	}
	
	public void reset()
	{
		this.italic = false;
		this.bold = false;
		this.strikeThrough = false;
		this.underline = false;
		this.shadow = false;
		this.unicode = false;
		this.obfuscated = false;
		this.setColor_F(1F, 1F, 1F, 1F);
	}
	
	public float drawString(float x, float y, String text)
	{
		return this.drawString(x, y, text, 0xFFFFFF);
	}
	
	public float drawString(float x, float y, String text, int color)
	{
		return this.drawString(x, y, text, color, false);
	}
	
	public float drawStringWithShadow(float x, float y, String text)
	{
		return this.drawStringWithShadow(x, y, text, 0xFFFFFF);
	}
	
	public float drawStringWithShadow(float x, float y, String text, int color)
	{
		return this.drawString(x, y, text, color, true);
	}
	
	public float drawString(float x, float y, String text, int color, boolean shadow)
	{
		this.setColor_I(color);
		this.globalShadow = shadow;
		
		text = this.replaceLocalizations(text);
		int len = text.length();
		
		float x1 = x;
		float y1 = y;
		float width = 0;
		float width1 = 0;
		
		for (int i = 0; i < len; i++)
		{
			char c = text.charAt(i);
			
			if (c == '\n')
			{
				width1 = Math.max(width, width1);
				width = 0;
				
				x = x1;
				y += this.getHeight();
				continue;
			}
			else if (c == '\t')
			{
				width = (i + 4 - (i & 3)) * 6;
				x = x1 + width;
				continue;
			}
			else if (c == '\r')
			{
				width1 = Math.max(width, width1);
				width = 0;
				x = x1;
				continue;
			}
			else if (c == '\u00A7' && i + 1 < len)
			{
				char c1 = text.charAt(i + 1);
				int i1 = "0123456789ABCDEF".indexOf(c1);
				
				if (c1 == '\u00a7')
				{
					continue;
				}
				else
				{
					if (i1 != -1 && i1 < 16)
					{
						this.setColor_I(this.colorTable[i1]);
					}
					else if (c1 == 'b')
					{
						this.bold = !this.bold;
					}
					else if (c1 == 'c') // c
					{
						int i2 = text.indexOf('[', i + 2);
						int i3 = text.indexOf(']', i + 3);
						if (i2 != -1 && i3 != -1)
						{
							String s1 = text.substring(i2 + 1, i3);
							this.setColor_S(s1);
							i = i3;
							continue;
						}
					}
					else if (c1 == 'i')
					{
						this.italic = !this.italic;
					}
					else if (c1 == 's')
					{
						this.strikeThrough = !this.strikeThrough;
					}
					else if (c1 == 'S')
					{
						this.shadow = !this.shadow;
					}
					else if (c1 == 'u')
					{
						this.underline = !this.underline;
					}
					else if (c1 == 'U')
					{
						this.unicode = !this.unicode;
					}
					else if (c1 == 'o')
					{
						this.obfuscated = !this.obfuscated;
					}
					else if (c1 == 'r')
					{
						this.reset();
					}
					
					i++;
					continue;
				}
			}
			
			if (this.obfuscated)
			{
				if (c != ' ')
				{
					int k = this.getCharWidth(c);
					int j = this.fontRandom.nextInt(64);
					int l = 0;
					for (Character character : this.chars)
					{
						char c1 = character.charValue();
						if (this.getCharWidth(c1) == k)
						{
							l++;
						}
						if (l == j)
						{
							c = c1;
							break;
						}
					}
				}
			}
			
			width += this.drawChar(x, y, c) + 1;
			x = x1 + width;
		}
		
		this.reset();
		return Math.max(width, width1) - 1;
	}
	
	public String replaceLocalizations(String text)
	{
		String text1 = text;
		int len = text.length();
		
		for (int i = 0; i < len; i++)
		{
			char c = text1.charAt(i);
			
			if (c == '#')
			{
				int end = text1.indexOf(' ', i + 1);
				if (end == -1)
				{
					end = len;
				}
				if (end == i + 1)
				{
					continue;
				}
				
				String key = text1.substring(i + 1, end);
				String translatedKey = I18n.getStringFormatted(key);
				
				text = text.replace("#" + key, translatedKey);
				
				i = end;
			}
		}
		return text;
	}
	
	public float drawChar(float x, float y, char c)
	{
		if (this.globalShadow || this.shadow)
		{
			this.drawChar(x + 1, y + 1, c, this.red / 4F, this.green / 4F, this.blue / 4F, this.alpha);
		}
		
		return this.drawChar(x, y, c, this.red, this.green, this.blue, this.alpha);
	}
	
	public float drawChar(float x, float y, char c, float red, float green, float blue, float alpha)
	{
		Image image = this.charMap.get(Character.valueOf(c));
		
		if (this.globalUnicode || this.unicode || image == null)
		{
			return this.drawUnicodeChar(x, y, c);
		}
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		float b = this.bold ? 1.25F : 1F;
		
		if (this.draw)
		{
			GL11.glPushMatrix();
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			
			GL11.glScalef(b, this.getHeight() / height, 1F);
			
			GL11.glColor4f(red, green, blue, alpha);
			image.drawSheared(x / b, y, this.italic ? -1.5F : 0F, 0, null);
			
			if (this.strikeThrough || this.underline)
			{
				this.graphics.setColor(new Color(red, green, blue, alpha));
				if (this.strikeThrough)
				{
					this.graphics.drawLine(x, y + 3, x + width, y + 3);
				}
				if (this.underline)
				{
					this.graphics.drawLine(x, y + 8, x + width, y + 8);
				}
			}
			
			GL11.glPopMatrix();
		}
		
		return width * b;
	}
	
	public float drawUnicodeChar(float x, float y, char c)
	{
		Graphics g = this.dr.renderEngine.graphics;
		
		String text = String.valueOf(c);
		float width = g.getFont().getWidth(text) / 2F;
		
		float b = this.bold ? 1.25F : 1F;
		
		if (this.draw)
		{
			GL11.glScalef(b, 0.5F, 1F);
			g.drawString(text, x / b, y * 2F);
			GL11.glScalef(1F / b, 2F, 1F);
		}
		
		return width * b;
	}
	
	public float getStringWidth(String text)
	{
		this.draw = false;
		float f = this.drawString(0, 0, text);
		this.draw = true;
		return f;
	}
	
	public int getCharWidth(char c)
	{
		this.draw = false;
		float f = this.drawChar(0, 0, c);
		this.draw = true;
		return (int) f;
	}
	
	public int getStringHeigth(String text)
	{
		return this.getHeight();
	}
	
	public int getHeight()
	{
		return HEIGHT;
	}
}
