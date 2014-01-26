package com.clashsoft.dungeonrun.client.engine;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.world.BlockInWorld;

public class RenderBlocks extends Render
{
	public static final int	BLOCKS_X	= 16;
	public static final int	BLOCKS_Y	= 9;
	
	public RenderBlocks()
	{
	}
	
	@Override
	public void render(Object renderable, int x, int y, float camX, float camY, int face) throws SlickException
	{
		this.render(renderable, x, y, camX, camY, face);
	}
	
	public void renderBlock(BlockInWorld block, int x, int y, float camX, float camY, int face) throws SlickException
	{
		if (block != null)
		{
			Image image = block.getBlockTexture(face);
			
			float offX = camX - x;
			float offY = camY - y;
			
			float renderX = this.width / 2F - offX * 16F;
			float renderY = this.height / 2F + offY * 16F;
			
			if (image != null)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				image.draw(renderX, renderY);
			}
		}
	}
}
