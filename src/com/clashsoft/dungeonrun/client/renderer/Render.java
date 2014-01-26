package com.clashsoft.dungeonrun.client.renderer;

import org.newdawn.slick.SlickException;

public abstract class Render
{
	public int	width;
	public int	height;
	
	public abstract void render(Object renderable, int x, int y, float camX, float camY, int face) throws SlickException;
}
