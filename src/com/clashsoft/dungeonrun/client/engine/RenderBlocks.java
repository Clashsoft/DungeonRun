package com.clashsoft.dungeonrun.client.engine;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.world.BlockInWorld;

public class RenderBlocks
{
	public static final int	BLOCKS_X	= 16;
	public static final int	BLOCKS_Y	= 10;
	
	public int				width;
	public int				height;
	
	public RenderBlocks()
	{
	}
	
	public void renderBlock(BlockInWorld block, int x, int y, int z, double camX, double camY, double camZ) throws SlickException
	{
		if (block != null)
		{
			double offX = camX - x;
			double offY = camY - y;
			
			float renderX = (float) (this.width / 2D - offX * 16F);
			float renderY = (float) (this.height / 2D + offY * 16F);
			
			this.renderBlock(block, renderX, renderY);
		}
	}
	
	protected void renderBlock(BlockInWorld block, float x, float y) throws SlickException
	{
		Image image = block.getBlockTexture(0);
		if (image != null)
		{
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			image.draw(x, y);
		}
	}
}
