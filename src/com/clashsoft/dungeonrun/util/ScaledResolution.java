package com.clashsoft.dungeonrun.util;

import com.clashsoft.dungeonrun.client.gui.GameSettings;

public class ScaledResolution
{
	public int		scaledWidth;
	public int		scaledHeight;
	public double	scaledWidthD;
	public double	scaledHeightD;
	public int		scaleFactor;
	
	public ScaledResolution(GameSettings settings, int width, int height)
	{
		this.scaledWidth = width;
		this.scaledHeight = height;
		this.scaleFactor = 1;
		int k = settings.guiSize;
		
		if (k == 0)
		{
			k = 1000;
		}
		
		while (this.scaleFactor < k && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240)
		{
			++this.scaleFactor;
		}
		
		this.scaledWidthD = (double) this.scaledWidth / (double) this.scaleFactor;
		this.scaledHeightD = (double) this.scaledHeight / (double) this.scaleFactor;
		this.scaledWidth = (int) Math.ceil(this.scaledWidthD);
		this.scaledHeight = (int) Math.ceil(this.scaledHeightD);
	}
	
	public int getScaledWidth()
	{
		return this.scaledWidth;
	}
	
	public int getScaledHeight()
	{
		return this.scaledHeight;
	}
	
	public double getScaledWidth_double()
	{
		return this.scaledWidthD;
	}
	
	public double getScaledHeight_double()
	{
		return this.scaledHeightD;
	}
	
	public int getScaleFactor()
	{
		return this.scaleFactor;
	}
}
