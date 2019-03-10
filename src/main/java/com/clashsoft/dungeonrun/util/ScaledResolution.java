package com.clashsoft.dungeonrun.util;

import com.clashsoft.dungeonrun.client.gui.GameSettings;

public class ScaledResolution
{
	public final int    scaledWidth;
	public final int    scaledHeight;
	public final double scaledWidthD;
	public final double scaledHeightD;
	public final int    scaleFactor;

	public ScaledResolution(GameSettings settings, int width, int height)
	{
		int scaleFactor = 1;
		int maxIterations = settings.guiSize;

		if (maxIterations == 0)
		{
			maxIterations = 1000;
		}

		while (scaleFactor < maxIterations && width / (scaleFactor + 1) >= 320 && height / (scaleFactor + 1) >= 240)
		{
			++scaleFactor;
		}

		this.scaleFactor = scaleFactor;
		this.scaledWidthD = (double) width / (double) this.scaleFactor;
		this.scaledHeightD = (double) height / (double) this.scaleFactor;
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
