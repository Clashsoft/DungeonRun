package com.clashsoft.dungeonrun.client.renderer;

import org.newdawn.slick.SlickException;

public interface Render
{
	public void render(Object renderable, int x, int y, float camX, float camY, int face) throws SlickException;
}
