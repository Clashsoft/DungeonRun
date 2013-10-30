package com.clashsoft.dungeonrun.engine;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.world.BlockInWorld;
import com.clashsoft.dungeonrun.world.World;

public class RenderBlocks
{
	public enum EnumBlockFace
	{
		TOP, BOTTOM, NORTH, EAST, SOUTH, WEST
	}
	
	public static final int	BLOCKS_X	= 18;
	public static final int	BLOCKS_Z	= 8;
	
	public int				width, heigth;
	
	public RenderBlocks()
	{
	}
	
	public void renderBlock(BlockInWorld block, int x, int y, int z, double playerPosX, double playerPosY, double playerPosZ, boolean hover) throws SlickException
	{
		if (block != null && !block.isAir() && block.getBlock() != null)
		{
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			
			int middleX = getMiddleBlockX();
			int middleY = getMiddleBlockY();
			
			float offsetX = getOffset(x, playerPosX) + 0.5F;
			float offsetY = getOffset(z, playerPosZ) + 1.5F;
			float offsetZ = getOffset(y, playerPosY);
			
			float posX = getRenderPosX(middleX, offsetX);
			float posY = getRenderPosY(middleY, offsetY, offsetZ);
			
			BlockInWorld topBlock = block.world.getBlock(x, y + 1, z);
			BlockInWorld sideBlock = block.world.getBlock(x, y, z - 1);
			
			float topLight = block.world.getLightValue(x, y + 1, z);
			float sideLight = block.world.getLightValue(x, y, z - 1);
			
			// Top Face
			if (topBlock == null || topBlock.isAir())
			{
				Image image1 = block.getBlock().getBlockTextureFromSideAndMetadata(0, block.getMetadata());
				if (image1 != null)
				{
					image1.setImageColor(topLight, topLight, topLight);
					image1.draw(posX, posY);
					image1.setImageColor(1, 1, 1);
					if (hover)
					{
						DungeonRun.instance.renderEngine.graphics.setColor(Color.black);
						DungeonRun.instance.renderEngine.graphics.drawRect(posX, posY, 15, 15);
					}
				}
			}
			
			// Front/South Face
			if (sideBlock == null || sideBlock.isAir())
			{
				Image image2 = block.getBlock().getBlockTextureFromSideAndMetadata(4, block.getMetadata());
				if (image2 != null)
				{
					GL11.glScalef(1F, 12F / 16F, 1F);
					image2.setImageColor(0.8F * sideLight, 0.8F * sideLight, 0.8F * sideLight);
					float posY2 = ((posY + 16) * (4F / 3F));
					image2.draw(posX, posY2);
					image2.setImageColor(1F, 1F, 1F);
					if (hover)
					{
						DungeonRun.instance.renderEngine.graphics.setColor(Color.black);
						DungeonRun.instance.renderEngine.graphics.drawRect(posX, posY2, 15, 15);
					}
					GL11.glScalef(1F, 16F / 12F, 1F);
				}
			}
		}
	}
	
	public float getRenderPosY(int middleY, float offsetY, float offsetZ)
	{
		return middleY - (offsetY * 16) - (offsetZ * 12);
	}
	
	public float getRenderPosX(int middleX, float offsetX)
	{
		return middleX + (offsetX * 16);
	}
	
	public float getOffset(int x, double playerPosX)
	{
		return (float) (x - playerPosX);
	}
	
	public int getMiddleBlockY()
	{
		return (heigth - 16) / 2;
	}
	
	public int getMiddleBlockX()
	{
		return (width - 16) / 2;
	}
	
	public boolean canSeeBlock(World world, int x, int y, int z, double playerPosX, double palyerPosY, double playerPosZ)
	{
		BlockInWorld topBlock = world.getBlock(x, y + 1, z);
		BlockInWorld sideBlock = world.getBlock(x, y, z - 1);
		return (topBlock == null || topBlock.isAir()) || (sideBlock == null || sideBlock.isAir());
	}
}
