package com.clashsoft.dungeonrun.client.engine;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.world.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class RenderBlocks
{
	public int width;
	public int height;

	public RenderBlocks()
	{
	}

	public void renderBlock(World world, int x, int y, double camX, double camY) throws SlickException
	{
		Block block = world.getBlock(x, y);

		if (block != null)
		{
			this.renderBlock(block, world.getBlockMetadata(x, y), x, y, camX, camY);
		}
	}

	public void renderBlock(Block block, int metadata, int x, int y, double camX, double camY) throws SlickException
	{
		double offX = camX - x;
		double offY = camY - y;

		float renderX = (float) (this.width / 2D - offX * 16F);
		float renderY = (float) (this.height / 2D + offY * 16F);

		this.renderBlock(block, metadata, renderX, renderY);
	}

	protected void renderBlock(Block block, int metadata, float x, float y) throws SlickException
	{
		Image image = block.getTexture(metadata);
		if (image != null)
		{
			image.draw(x, y);
		}
	}
}
