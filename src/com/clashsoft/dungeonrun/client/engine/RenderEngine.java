package com.clashsoft.dungeonrun.client.engine;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.clashsoft.dungeonrun.server.DungeonRunServer;

public class RenderEngine
{
	public DungeonRunServer	dr;
	
	public Graphics		graphics;
	
	public RenderBlocks	blockRenderer;
	public RenderItems	itemRenderer;
	
	public RenderEngine(DungeonRunServer dungeonRunServer)
	{
		this.dr = dungeonRunServer;
		blockRenderer = new RenderBlocks();
	}
	
	public void drawTexture(Image image, int x, int y, int u, int v, int sx, int sy)
	{
		graphics.drawImage(image, x, y, x + sx, y + sy, u, v, sx, sy);
	}
}
