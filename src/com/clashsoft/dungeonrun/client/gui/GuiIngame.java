package com.clashsoft.dungeonrun.client.gui;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.client.engine.RenderBlocks;
import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.world.BlockInWorld;
import com.clashsoft.dungeonrun.world.World;

public class GuiIngame extends GuiScreen
{
	private static final Comparator<Entity> entitySorterTop = (e1, e2) -> Double.compare(e2.posZ, e1.posZ);
	
	public RenderBlocks renderBlocks;
	
	private Entity[]	entities	= new Entity[0];
	private int			entityCount;
	private boolean		worldSaving	= false;
	
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
			this.renderLevel(width, height);
		}
		
		if (this.worldSaving)
		{
			String text = I18n.getString("world.saving");
			float w = this.dr.fontRenderer.getStringWidth(text);
			this.dr.fontRenderer.drawStringWithShadow(width - 20F - w, height - 20F, text, 0xFFFFFF);
		}
	}
	
	private void renderLevel(int width, int height) throws SlickException
	{
		World world = this.player.worldObj;
		double camX = this.player.posX;
		double camY = this.player.posY;
		double camZ = this.player.posZ;
		
		int blocksX = width / 32;
		int blocksY = height / 32;
		int minX = -blocksX - 2;
		int maxX = blocksX;
		int minZ = -blocksY;
		int maxZ = blocksY + 2;
		
		int posX = (int) camX;
		int posY = (int) camY;
		int posZ = (int) camZ;
		
		int entityIndex = 0;
		
		for (int z = minZ; z <= maxZ; z++)
		{
			int z1 = posZ + z;
			for (int x = minX; x <= maxX; x++)
			{
				int x1 = posX + x;
				int y1 = posY;
				
				BlockInWorld block = world.getBlock(x1, y1, z1);
				if (block != null && !block.isAir())
				{
					this.renderBlocks.renderBlock(block, x1, y1, z1, camX, camY, camZ);
					break;
				}
			}
			
			for (; entityIndex < this.entityCount; entityIndex++)
			{
				Entity entity = this.entities[entityIndex];
				if (entity.posZ < z1)
				{
					continue;
				}
				if (entity.posZ > z1 + 1)
				{
					break;
				}
				
				Render render = entity.getRenderer();
				render.width = width;
				render.height = height;
				render.render(entity, entity.posX, entity.posZ, camX, camZ);
			}
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
			else if (input.isKeyDown(Input.KEY_D))
			{
				this.player.walk(1);
			}
			else if (input.isKeyDown(Input.KEY_S))
			{
				this.player.walk(0);
			}
			else if (input.isKeyDown(Input.KEY_A))
			{
				this.player.walk(3);
			}
			else
			{
				this.player.isWalking = false;
			}
			
			if (input.isKeyDown(Input.KEY_SPACE))
			{
				this.player.jump();
			}
			this.player.isSprinting = input.isKeyDown(Input.KEY_LSHIFT);
		}
		if (input.isKeyDown(Input.KEY_ESCAPE))
		{
			this.dr.pauseGame();
		}
		
		// Update entities
		Collection<Entity> entities = this.player.worldObj.getEntitys();
		this.entityCount = entities.size();
		this.entities = entities.toArray(this.entities);
		Arrays.sort(this.entities, entitySorterTop);
	}
	
	@Override
	public void setWorldSaving(boolean state)
	{
		this.worldSaving = state;
	}
}
