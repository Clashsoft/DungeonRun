package com.clashsoft.dungeonrun.engine;

import com.clashsoft.dungeonrun.DungeonRun;

public class FontRenderer
{
	public DungeonRun	dr;
	
	public FontRenderer(DungeonRun kq)
	{
		this.dr = kq;
	}
	
	public void drawString(int x, int y, String text, int color)
	{
		DungeonRun.getGraphics().drawString(text, x, y);
	}
	
	public int getStringWidth(String text)
	{
		return DungeonRun.getGraphics().getFont().getWidth(text);
	}

	public int getStringHeigth(String message)
	{
		return DungeonRun.getGraphics().getFont().getLineHeight();
	}
}
