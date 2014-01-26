package com.clashsoft.dungeonrun.client.gui;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.client.engine.RenderBlocks;
import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.world.BlockInWorld;
import com.clashsoft.dungeonrun.world.World;

public class GuiIngame extends GuiScreen
{
	public RenderBlocks	renderBlocks;
	
	public int			mouseBlockX, mouseBlockY, mouseBlockZ;
	
	public int			displayMode	= 1;
	
	private boolean		worldSaving	= false;
	
	public GuiIngame(EntityPlayer player)
	{
		this.player = player;
	}
	
	@Override
	public void initGui() throws SlickException
	{
		this.renderBlocks = this.dr.renderEngine.blockRenderer;
	}
	
	@Override
	public void drawScreen(int width, int height) throws SlickException
	{
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		this.renderBlocks.width = width;
		this.renderBlocks.height = height;
		
		if (this.player != null)
		{
			int mode = this.displayMode;
			World world = this.player.worldObj;
			float camX = (float) this.player.posX;
			float camY = (float) this.player.posY;
			float camZ = (float) this.player.posZ;
			int posX = (int) camX;
			int posY = (int) camY + 1;
			int posZ = (int) camZ;
			int maxX = RenderBlocks.BLOCKS_X;
			int maxY = RenderBlocks.BLOCKS_Y;
			int minX = -RenderBlocks.BLOCKS_X;
			int minY = -RenderBlocks.BLOCKS_Y;
			
			if (mode == 1) // Top view
			{
				for (int i = minX; i <= maxX; i++)
				{
					for (int j = minY; j <= maxY; j++)
					{
						int x = posX + i;
						int y = posY + 0;
						int z = posZ + j;
						BlockInWorld block = world.getBlock(x, y, z);
						this.renderBlocks.renderBlock(block, x, z, camX, camZ, mode);
					}
				}
			}
			else if (mode == 2)
			{
				for (int i = minX; i <= maxX; i++)
				{
					for (int j = minY; j <= maxY; j++)
					{
						int x = posX + i;
						int y = posY + j;
						int z = posZ + 0;
						BlockInWorld block = world.getBlock(x, y, z);
						this.renderBlocks.renderBlock(block, x, y, camX, camY, mode);
					}
				}
			}
			else if (mode == 3)
			{
				for (int i = minX; i <= maxX; i++)
				{
					for (int j = minY; j <= maxY; j++)
					{
						int x = posX + 0;
						int y = posY + j;
						int z = posZ + i;
						BlockInWorld block = world.getBlock(x, y, z);
						this.renderBlocks.renderBlock(block, z, y, camZ, camY, mode);
					}
				}
			}
			else if (mode == 4)
			{
				for (int i = minX; i <= maxX; i++)
				{
					for (int j = minY; j <= maxY; j++)
					{
						int x = posX - i;
						int y = posY + j;
						int z = posZ + 0;
						BlockInWorld block = world.getBlock(x, y, z);
						this.renderBlocks.renderBlock(block, x, y, camX, camY, mode);
					}
				}
			}
			else if (mode == 5)
			{
				for (int i = minX; i <= maxX; i++)
				{
					for (int j = minY; j <= maxY; j++)
					{
						int x = posX + 0;
						int y = posY + j;
						int z = posZ - i;
						BlockInWorld block = world.getBlock(x, y, z);
						this.renderBlocks.renderBlock(block, z, y, camZ, camY, mode);
					}
				}
			}
			
			for (Entity entity : world.getEntitys())
			{
				entity.getRenderer().render(entity, 0, 0, camX, camY, mode);
			}
		}
		
		if (this.worldSaving)
		{
			String text = I18n.getString("world.saving");
			int w = this.dr.fontRenderer.getStringWidth(text);
			this.dr.fontRenderer.drawString(width - 20 - w, height - 20, text, 0xFFFFFF);
		}
	}
	
	@Override
	public void keyTyped(int key, char c) throws SlickException
	{
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
		Input input = this.dr.getInput();
		
		if (this.player != null)
		{
			if (input.isKeyDown(Input.KEY_W))
			{
				this.player.walk(2);
			}
			if (input.isKeyDown(Input.KEY_D))
			{
				this.player.walk(1);
			}
			if (input.isKeyDown(Input.KEY_S))
			{
				this.player.walk(0);
			}
			if (input.isKeyDown(Input.KEY_A))
			{
				this.player.walk(3);
			}
			if (input.isKeyDown(Input.KEY_SPACE))
			{
				this.player.jump();
			}
			this.player.isSprinting = input.isKeyDown(Input.KEY_LSHIFT);
			
			if (input.isMousePressed(0))
			{
				this.player.worldObj.setBlock(0, 0, this.mouseBlockX, this.mouseBlockY, this.mouseBlockZ);
			}
			if (input.isMousePressed(1))
			{
				this.player.worldObj.setBlock(Block.stone.blockID, 0, this.mouseBlockX, this.mouseBlockY, this.mouseBlockZ);
			}
		}
		if (input.isKeyDown(Input.KEY_ESCAPE))
		{
			this.dr.pauseGame();
		}
	}
	
	@Override
	public void setWorldSaving(boolean state)
	{
		this.worldSaving = state;
	}
}
