package com.clashsoft.dungeonrun.client.engine;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class RenderEngine
{
	public DungeonRunClient dr;

	public Graphics graphics;

	public RenderBlocks blockRenderer;
	public RenderItems  itemRenderer;

	public RenderEngine(DungeonRunClient dungeonRun)
	{
		this.dr = dungeonRun;
		this.blockRenderer = new RenderBlocks();
	}

	public void drawTexture(Image image, int x, int y, int u, int v, int sx, int sy)
	{
		this.graphics.drawImage(image, x, y, x + sx, y + sy, u, v, sx, sy);
	}
}
