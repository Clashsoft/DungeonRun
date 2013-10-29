package com.clashsoft.dungeonrun.gui;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import com.clashsoft.dungeonrun.DungeonRun;
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
		
		GL11.glScalef(par1 / scaledResolution.scaledWidth, par2 / scaledResolution.scaledHeight, 1F);
		
		this.drawScreen(scaledResolution.scaledWidth, scaledResolution.scaledHeight);
		
		for (GuiButton button : buttonList)
		{
			if (button != null)
				button.render();
		}
		
		if (!dr.debugMode)
			dr.fontRenderer.drawString(10, 0, String.format("%d FPS", dr.theGameContainer.getFPS()));
		else
		{
			dr.fontRenderer.drawString(10, 0, String.format("Dungeon Run %s (%d FPS)", DungeonRun.VERSION, dr.theGameContainer.getFPS()));
			if (dr.thePlayer != null)
			{
				dr.fontRenderer.drawString(10, 20, String.format("PlayerPos: (%.2f;%.2f;%.2f)", dr.thePlayer.posX, dr.thePlayer.posY, dr.thePlayer.posZ));
				dr.fontRenderer.drawString(10, 30, String.format("PlayerRot: %d", dr.thePlayer.rot));
				dr.fontRenderer.drawString(10, 40, String.format("PlayerVelocity: (%.2f;%.2f;%.2f)", dr.thePlayer.velocityX, dr.thePlayer.velocityY, dr.thePlayer.velocityZ));
				dr.fontRenderer.drawString(10, 50, String.format("PlayerWorld :", dr.thePlayer.worldObj.toString()));
			}
			
			if (dr.theIngameGui != null)
			{
				dr.fontRenderer.drawString(10, 60, String.format("HoverPos: (%d;%d;%d)", dr.theIngameGui.mouseBlockX, dr.theIngameGui.mouseBlockY, dr.theIngameGui.mouseBlockZ));
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
		return windowWidth / 512D;
	}
	
	public double getRescaleFactorY(int windowHeight)
	{
		return windowHeight / 200D;
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
	
	public void drawDefaultBackground(int width, int height)
	{
		DungeonRun.instance.theGameContainer.getGraphics().fill(new Rectangle(0, 0, width, height));
	}
}
