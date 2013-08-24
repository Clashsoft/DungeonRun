package com.clashsoft.dungeonrun.gui;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.engine.RenderBlocks;
import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.util.DimensionHelper.Pos3;

public class GuiIngame extends GuiScreen
{
	public EntityPlayer					player;
	public RenderBlocks					renderBlocks;
	
	public int							mouseBlockX, mouseBlockY, mouseBlockZ;
	
	private Map<Pos3<Float>, Entity>	entityMap	= new HashMap<Pos3<Float>, Entity>();
	
	public GuiIngame(EntityPlayer player)
	{
		super();
		this.player = player;
		this.renderBlocks = DungeonRun.instance.renderEngine.blockRenderer;
	}
	
	@Override
	public void initGui() throws SlickException
	{
	}
	
	@Override
	public void drawScreen(int par1, int par2) throws SlickException
	{
		DungeonRun.getGraphics().setColor(Color.white);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		// Always sync the block renderer with the display and the player
		renderBlocks.width = par1;
		renderBlocks.heigth = par2;
		for (int x = (int) (player.posX + RenderBlocks.BLOCKS_X); x > player.posX - RenderBlocks.BLOCKS_X; x--)
		{
			for (int z = (int) (player.posZ + RenderBlocks.BLOCKS_Z); z > player.posZ - RenderBlocks.BLOCKS_Z - 4; z--)
			{
				for (int y = 0; y < 64; y++)
				{
					Entity e = entityMap.get(new Pos3<Float>((float) x, (float) y, (float) z));
					if (e != null)
						e.getRenderer().render(e, par1, par2);
					
					boolean hover = isMouseOverBlock(x, y, z);
					if (player.worldObj.getBlock(x, y, z) != null && !player.worldObj.getBlock(x, y, z).isAir())
					{
						if (hover)
						{
							mouseBlockX = x;
							mouseBlockY = y;
							mouseBlockZ = z;
						}
						
						if (renderBlocks.canSeeBlock(player.worldObj, x, y, z, player.posX, player.posY, player.posZ))
							renderBlocks.renderBlock(player.worldObj.getBlock(x, y, z), x, y, z, player.posX, player.posY, player.posZ, hover);
					}
				}
			}
		}
	}
	
	private boolean isMouseOverBlock(int x, int y, int z)
	{
		float offsetX = renderBlocks.getOffset(x, player.posX);
		float offsetY = renderBlocks.getOffset(z, player.posZ) + 1.5F;
		float offsetZ = renderBlocks.getOffset(y, player.posY);
		
		float posX = renderBlocks.getRenderPosX(renderBlocks.getMiddleBlockX(), offsetX);
		float posY = renderBlocks.getRenderPosY(renderBlocks.getMiddleBlockY(), offsetY, offsetZ);
		
		return isMouseInRegion(posX + 8F, posY, 16F, 16F) && renderBlocks.canSeeBlock(player.worldObj, x, y, z, player.posX, player.posY, player.posZ);
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
		Input input = this.dr.theGameContainer.getInput();
		for (Entity e : player.worldObj.getEntitys())
		{
			e.updateEntity();
		}
		
		entityMap.clear();
		for (Entity e : player.worldObj.getEntitys())
		{
			float x = (int) Math.floor(e.posX);
			float y = (int) Math.floor(e.posY);
			float z = (int) Math.floor(e.posZ);
			entityMap.put(new Pos3<Float>(x, y, z), e);
			entityMap.put(new Pos3<Float>(x + 1F, y, z), e);
			// entityMap.put(new Pos3<Float>(x - 1F, y, z), e);
			entityMap.put(new Pos3<Float>(x, y, z - 1), e);
		}
		
		if (input.isKeyDown(Input.KEY_W))
			this.player.walk(2);
		if (input.isKeyDown(Input.KEY_D))
			this.player.walk(1);
		if (input.isKeyDown(Input.KEY_S))
			this.player.walk(0);
		if (input.isKeyDown(Input.KEY_A))
			this.player.walk(3);
		if (input.isKeyDown(Input.KEY_SPACE))
			this.player.jump();
		this.player.isSprinting = input.isKeyDown(Input.KEY_LSHIFT);
		
		if (input.isMousePressed(0))
			this.player.worldObj.setBlock(0, 0, mouseBlockX, mouseBlockY, mouseBlockZ);
		if (input.isMousePressed(1))
			this.player.worldObj.setBlock(Block.stone.blockID, 0, mouseBlockX, mouseBlockY, mouseBlockZ);
		if (input.isKeyDown(Input.KEY_ESCAPE))
			DungeonRun.instance.pauseGame();
	}
}
