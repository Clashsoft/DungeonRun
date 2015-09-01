package com.clashsoft.dungeonrun.client.gui;

import java.util.Arrays;
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
	public RenderBlocks			renderBlocks;
	
	public Comparator<Entity>	entitySorterTop	= (e1, e2) -> Double.compare(e2.posZ, e1.posZ);
	
	public int					mouseBlockX, mouseBlockY, mouseBlockZ;
	public int					displayMode		= 1;
	private boolean				worldSaving		= false;
	
	public GuiIngame(EntityPlayer player)
	{
		this.player = player;
	}
	
	@Override
	public void initGUI() throws SlickException
	{
		this.renderBlocks = this.dr.renderEngine.blockRenderer;
		
		this.dr.soundEngine.stopAllMusics();
		this.dr.soundEngine.playMusic("ingame_1", true);
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
			
			int blocksX = width / 32;
			int blocksY = height / 32;
			int minI = -blocksX - 2;
			int maxI = blocksX;
			int minJ = -blocksY;
			int maxJ = blocksY + 2;
			
			int posX = (int) camX;
			int posY = (int) camY + 64;
			int posZ = (int) camZ;
			
			for (int i = minI; i <= maxI; i++)
			{
				for (int j = minJ; j <= maxJ; j++)
				{
					for (int k = 0; k < 128; k++)
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
			Entity[] entityArray = entities.toArray(new Entity[entities.size()]);
			Arrays.sort(entityArray, this.entitySorterTop);
			
			for (Entity entity : entityArray)
			{
				Render render = entity.getRenderer();
				render.width = width;
				render.height = height;
				entity.getRenderer().render(entity, entity.posX, entity.posZ, camX, camZ, mode);
			}
		}
		
		if (this.worldSaving)
		{
			String text = I18n.getString("world.saving");
			float w = this.dr.fontRenderer.getStringWidth(text);
			this.dr.fontRenderer.drawStringWithShadow(width - 20F - w, height - 20F, text, 0xFFFFFF);
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
			
			// if (input.isKeyPressed(Input.KEY_TAB))
			// {
			// this.displayMode %= 5;
			// this.displayMode++;
			// }
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
