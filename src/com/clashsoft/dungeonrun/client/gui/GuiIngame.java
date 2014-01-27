package com.clashsoft.dungeonrun.client.gui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.client.engine.RenderBlocks;
import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.world.BlockInWorld;
import com.clashsoft.dungeonrun.world.World;

public class GuiIngame extends GuiScreen
{
	public RenderBlocks	renderBlocks;
	
	public Comparator	entitySorterTop	= new Comparator<Entity>()
										{
											@Override
											public int compare(Entity o1, Entity o2)
											{
												return -Double.compare(o1.posZ, o2.posZ);
											};
										};
	
	public int			mouseBlockX, mouseBlockY, mouseBlockZ;
	public int			displayMode		= 1;
	private boolean		worldSaving		= false;
	
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
			double camX = this.player.posX;
			double camY = this.player.posY;
			double camZ = this.player.posZ;
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
						for (int k = 0;; k++)
						{
							int x = posX + i;
							int y = posY - k;
							int z = posZ + j;
							
							BlockInWorld block = world.getBlock(x, y, z);
							if (block != null && !block.isAir())
							{
								this.renderBlocks.renderBlock(block, x, z, camX, camZ, mode);
								break;
							}
						}
					}
				}
				
				List<Entity> entities = world.getEntitys();
				Collections.sort(entities, this.entitySorterTop);
				
				for (Entity entity : entities)
				{
					Render render = entity.getRenderer();
					render.width = width;
					render.height = height;
					entity.getRenderer().render(entity, entity.posX, entity.posZ, camX, camZ, mode);
				}
			}
		}
		
		if (this.worldSaving)
		{
			String text = I18n.getString("world.saving");
			float w = this.dr.fontRenderer.getStringWidth(text);
			this.dr.fontRenderer.drawString(width - 20F - w, height - 20F, text, 0xFFFFFF);
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
