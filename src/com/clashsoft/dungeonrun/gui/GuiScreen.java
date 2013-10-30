package com.clashsoft.dungeonrun.gui;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.util.ScaledResolution;

public abstract class GuiScreen
{
	protected DungeonRun		dr;
	
	protected int				windowWidth;
	protected int				windowHeight;
	
	protected List<GuiButton>	buttonList	= new LinkedList<GuiButton>();
	
	protected ScaledResolution	scaledResolution;
	
	public final void init(DungeonRun game) throws SlickException
	{
		this.dr = game;
		
		this.scaledResolution = new ScaledResolution(game.gameSettings, windowWidth, windowHeight);
		
		this.initGui();
	}
	
	public final void render(int par1, int par2) throws SlickException
	{
		if (this.windowWidth != par1 || this.windowHeight != par2)
		{
			this.windowWidth = par1;
			this.windowHeight = par2;
			this.init(dr);
		}	
		
		GL11.glPushMatrix();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glScalef(scaledResolution.scaleFactor, scaledResolution.scaleFactor, 1F);
		
		this.drawScreen(scaledResolution.scaledWidth, scaledResolution.scaledHeight);
		
		for (GuiButton button : buttonList)
		{
			if (button != null)
				button.render();
		}
		
		if (!dr.debugMode)
			dr.fontRenderer.drawStringWithShadow(5, 5, String.format("%d FPS", dr.theGameContainer.getFPS()));
		else
		{
			dr.fontRenderer.drawStringWithShadow(5, 5, String.format("Dungeon Run %s (%d FPS)", DungeonRun.VERSION, dr.theGameContainer.getFPS()));
			if (dr.thePlayer != null)
			{
				dr.fontRenderer.drawStringWithShadow(5, 25, String.format("PlayerPos: (%.2f;%.2f;%.2f)", dr.thePlayer.posX, dr.thePlayer.posY, dr.thePlayer.posZ));
				dr.fontRenderer.drawStringWithShadow(5, 35, String.format("PlayerRot: %d", dr.thePlayer.rot));
				dr.fontRenderer.drawStringWithShadow(5, 45, String.format("PlayerVelocity: (%.2f;%.2f;%.2f)", dr.thePlayer.velocityX, dr.thePlayer.velocityY, dr.thePlayer.velocityZ));
				dr.fontRenderer.drawStringWithShadow(5, 55, String.format("PlayerWorld :", dr.thePlayer.worldObj.toString()));
			}
			
			if (dr.theIngameGui != null)
			{
				dr.fontRenderer.drawStringWithShadow(5, 65, String.format("HoverPos: (%d;%d;%d)", dr.theIngameGui.mouseBlockX, dr.theIngameGui.mouseBlockY, dr.theIngameGui.mouseBlockZ));
			}
		}
		GL11.glPopMatrix();
	}
	
	public final void update(DungeonRun game) throws SlickException
	{
		this.updateScreen();
	}
	
	public abstract void initGui() throws SlickException;
	
	public abstract void drawScreen(int par1, int par2) throws SlickException;
	
	public abstract void updateScreen() throws SlickException;
	
	public double getRescaleFactorX(int windowWidth)
	{
		return scaledResolution.scaleFactor;
	}
	
	public double getRescaleFactorY(int windowHeight)
	{
		return scaledResolution.scaleFactor;
	}
	
	public boolean isMouseInRegion(float x, float y, float sizeX, float sizeY)
	{
		double mouseX = Mouse.getX() / getRescaleFactorX(windowWidth);
		double mouseY = (windowHeight - Mouse.getY()) / getRescaleFactorY(windowHeight);
		return (mouseX > x && mouseX < x + sizeX && mouseY > y && mouseY < y + sizeY);
	}
	
	public void setWorldSaving(boolean state)
	{
		
	}
	
	public void drawBricks(int width, int heigth) throws SlickException
	{
		GL11.glScalef(2F, 2F, 1F);
		for (int i = 0; i < width / 8F; i++)
		{
			for (int j = 0; j < heigth / 8F; j++)
			{
				Block.brick.getBlockTextureFromSideAndMetadata(0, 0).draw(i * 16, j * 16);
			}
		}
		GL11.glScalef(0.5F, 0.5F, 1F);
	}
	
	public void drawDefaultBackground(int width, int height)
	{
		DungeonRun.instance.theGameContainer.getGraphics().fill(new Rectangle(0, 0, width, height));
	}
}
