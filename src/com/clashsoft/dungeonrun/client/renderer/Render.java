package com.clashsoft.dungeonrun.client.renderer;

import org.newdawn.slick.SlickException;

public abstract class Render<T>
{
	public int	width;
	public int	height;
	
	public void render(T renderable, double x, double y, double camX, double camY, int mode) throws SlickException
	{
		double offX = camX - x;
		double offY = camY - y;
		
		double renderX = this.width / 2D - offX * 16F;
		double renderY = this.height / 2D + offY * 16F;
		
		this.render(renderable, renderX, renderY, mode);
	}
	
	public abstract void render(T renderable, double x, double y, int face);
}
