package com.clashsoft.dungeonrun.client.renderer;

import org.newdawn.slick.Graphics;

public abstract class Render<T>
{
	public int	width;
	public int	height;
	
	public void render(T renderable, Graphics g, double x, double y, double camX, double camY)
	{
		double offX = camX - x;
		double offY = camY - y;
		
		double renderX = this.width / 2D - offX * 16F;
		double renderY = this.height / 2D + offY * 16F;
		
		this.render(renderable, g, renderX, renderY);
	}
	
	public abstract void render(T renderable, Graphics g, double x, double y);
}
