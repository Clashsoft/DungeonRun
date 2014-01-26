package com.clashsoft.dungeonrun.client.engine;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.clashsoft.dungeonrun.client.DungeonRunClient;

public class RenderEngine
{
	public DungeonRunClient	dr;
	
	public Graphics		graphics;
	
	public RenderBlocks	blockRenderer;
	public RenderItems	itemRenderer;
	
	public RenderEngine(DungeonRunClient dungeonRun)
	{
		this.dr = dungeonRun;
		blockRenderer = new RenderBlocks();
	}
	
	public void drawTexture(Image image, int x, int y, int u, int v, int sx, int sy)
	{
		graphics.drawImage(image, x, y, x + sx, y + sy, u, v, sx, sy);
	}
}
