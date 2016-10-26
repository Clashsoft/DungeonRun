package com.clashsoft.dungeonrun.client.engine;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.world.World;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class RenderBlocks
{
	public int width;
	public int height;

	public RenderBlocks()
	{
	}

	public void renderBlock(World world, int x, int y, int z, double camX, double camY) throws SlickException
	{
		Block block = world.getBlock(x, y);

		if (block != null)
		{
			double offX = camX - x;
			double offY = camY - y;

			float renderX = (float) (this.width / 2D - offX * 16F);
			float renderY = (float) (this.height / 2D + offY * 16F);

			this.renderBlock(block, world.getBlockMetadata(x, y), renderX, renderY);
		}
	}

	protected void renderBlock(Block block, int metadata, float x, float y) throws SlickException
	{
		Image image = block.getTexture(metadata);
		if (image != null)
		{
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			image.draw(x, y);
		}
	}
}
