package com.clashsoft.dungeonrun.client.gui;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.util.ScaledResolution;

public abstract class GuiScreen
{
	public static final Color	colorAlpha	= new Color(0F, 0F, 0F, 0.5F);
	
	protected DungeonRunClient	dr;
	protected EntityPlayer		player;
	
	protected Input				input;
	
	protected int				windowWidth;
	protected int				windowHeight;
	
	protected List<GuiButton>	buttonList	= new LinkedList<GuiButton>();
	
	protected ScaledResolution	scaledResolution;
	
	public final void init(DungeonRunClient game) throws SlickException
	{
		this.dr = game;
		this.input = this.dr.getInput();
		
		this.scaledResolution = new ScaledResolution(game.gameSettings, this.windowWidth, this.windowHeight);
		
		this.initGui();
	}
	
	public final void render(int width, int height) throws SlickException
	{
		if (this.windowWidth != width || this.windowHeight != height)
		{
			this.windowWidth = width;
			this.windowHeight = height;
			this.init(this.dr);
		}
		
		GL11.glPushMatrix();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glScalef(this.scaledResolution.scaleFactor, this.scaledResolution.scaleFactor, 1F);
		
		this.drawScreen(this.scaledResolution.scaledWidth, this.scaledResolution.scaledHeight);
		
		for (GuiButton button : this.buttonList)
		{
			if (button != null)
			{
				button.render();
			}
		}
		
		this.dr.fontRenderer.drawStringWithShadow(5, 5, String.format("\u00a7iDungeon Run\u00a7i %s (%d FPS)", DungeonRun.VERSION, this.dr.getFPS()));
		if (this.dr.gameSettings.debugMode)
		{
			int var = 5;
			if (this.player != null)
			{
				this.dr.fontRenderer.drawStringWithShadow(5, var += 10, String.format("PlayerPos: (%.2f;%.2f;%.2f)", this.player.posX, this.player.posY, this.player.posZ));
				this.dr.fontRenderer.drawStringWithShadow(5, var += 10, String.format("PlayerRot: %d", this.player.rot));
				this.dr.fontRenderer.drawStringWithShadow(5, var += 10, String.format("PlayerVelocity: (%.2f;%.2f;%.2f)", this.player.velocityX, this.player.velocityY, this.player.velocityZ));
				this.dr.fontRenderer.drawStringWithShadow(5, var += 10, String.format("PlayerWorld: %s", this.dr.getWorld().worldInfo.getName()));
			}
			
			{
				Runtime runtime = Runtime.getRuntime();
				long freeMemory = runtime.freeMemory() / 1024 / 1024;
				long maxMemory = runtime.maxMemory() / 1024 / 1024;
				long totalMemory = runtime.totalMemory() / 1024 / 1024;
				
				this.dr.fontRenderer.drawStringWithShadow(5, var += 10, String.format("Memory: Free: %d MB, Max: %d MB, Total: %d MB", freeMemory, maxMemory, totalMemory));
				this.dr.fontRenderer.drawStringWithShadow(5, var += 10, String.format("Processors: %d", runtime.availableProcessors()));
			}
		}
		GL11.glPopMatrix();
	}
	
	public final void update(DungeonRunClient game) throws SlickException
	{
		this.updateScreen();
	}
	
	public abstract void keyTyped(int key, char c) throws SlickException;
	
	public abstract void initGui() throws SlickException;
	
	public abstract void drawScreen(int width, int height) throws SlickException;
	
	public abstract void updateScreen() throws SlickException;
	
	public double getRescaleFactorX(int windowWidth)
	{
		return this.scaledResolution.scaleFactor;
	}
	
	public double getRescaleFactorY(int windowHeight)
	{
		return this.scaledResolution.scaleFactor;
	}
	
	public boolean isMouseInRegion(float x, float y, float width, float height)
	{
		double mouseX = Mouse.getX() / this.scaledResolution.scaleFactor;
		double mouseY = this.scaledResolution.scaledHeightD - (Mouse.getY() / this.scaledResolution.scaleFactor);
		return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
	}
	
	public void setWorldSaving(boolean state)
	{
		
	}
	
	public void drawBricks(int width, int height) throws SlickException
	{
		GL11.glScalef(2F, 2F, 1F);
		int k = width >> 1;
		int l = height >> 1;
		
		for (int i = 0; i < k; i += 16)
		{
			for (int j = 0; j < l; j += 16)
			{
				Block.brick.getTexture(0, 0).draw(i, j);
			}
		}
		GL11.glScalef(0.5F, 0.5F, 1F);
	}
	
	public void drawDefaultBackground(int width, int height) throws SlickException
	{
		if (this.dr.isGameRunning())
		{
			this.dr.getGraphics().setColor(colorAlpha);
			this.dr.getGraphics().fillRect(0, 0, width, height);
		}
		else
		{
			this.drawBricks(width, height);
		}
	}
}
