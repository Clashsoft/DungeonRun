package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.block.Blocks;
import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.engine.FontRenderer;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.util.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.LinkedList;
import java.util.List;

public abstract class GuiScreen
{
	public static final Color TRANSPARENT_BLACK = new Color(0F, 0F, 0F, 0.5F);

	protected DungeonRunClient dr;

	protected Input input;

	protected int windowWidth;
	protected int windowHeight;

	protected List<GuiButton> buttonList = new LinkedList<GuiButton>();

	protected ScaledResolution scaledResolution;

	public final void init(DungeonRunClient game, int width, int height) throws SlickException
	{
		this.dr = game;
		this.input = this.dr.getInput();

		this.rescale(width, height);
		this.initGUI();
	}

	public final void rescale(int width, int height) throws SlickException
	{
		this.windowWidth = width;
		this.windowHeight = height;
		this.scaledResolution = new ScaledResolution(this.dr.gameSettings, this.windowWidth, this.windowHeight);

		this.rescaleGUI();
	}

	public final void render(Graphics g, int width, int height) throws SlickException
	{
		if (this.windowWidth != width || this.windowHeight != height)
		{
			this.rescale(width, height);
		}

		GL11.glPushMatrix();

		GL11.glScalef(this.scaledResolution.scaleFactor, this.scaledResolution.scaleFactor, 1F);

		this.drawScreen(g, this.scaledResolution.scaledWidth, this.scaledResolution.scaledHeight);

		for (GuiButton button : this.buttonList)
		{
			if (button != null)
			{
				button.render();
			}
		}

		if (this.dr.gameSettings.debugMode)
		{
			final FontRenderer fontRenderer = this.dr.smallFontRenderer;
			fontRenderer.drawStringWithShadow(5, 5, String.format("\u00a7iDungeon Run\u00a7i %s (%d FPS)",
			                                                      DungeonRun.VERSION, this.dr.getFPS()));
			int var = 5;
			final int offset = 7;

			fontRenderer.drawStringWithShadow(5, var += offset, String.format("Tick: %d", this.dr.getTick()));
			fontRenderer.drawStringWithShadow(5, var += offset, String.format("Width: %d", width));
			fontRenderer.drawStringWithShadow(5, var += offset, String.format("Height: %d", height));

			final EntityPlayer player = this.dr.thePlayer;
			if (player != null)
			{
				fontRenderer.drawStringWithShadow(5, var += offset,
				                                  String.format("PlayerPos: (%.2f; %.2f)", player.posX, player.posY));
				fontRenderer.drawStringWithShadow(5, var += offset, String.format("PlayerPitch: %f", player.pitch));
				fontRenderer.drawStringWithShadow(5, var += offset, String.format("PlayerVelocity: (%.2f; %.2f)",
				                                                                  player.velocityX, player.velocityY));
				fontRenderer.drawStringWithShadow(5, var += offset, String.format("PlayerWorld: %s",
				                                                                  this.dr.getWorld().getWorldInfo()
				                                                                         .getName()));
			}

			{
				Runtime runtime = Runtime.getRuntime();
				long freeMemory = runtime.freeMemory() / 1024 / 1024;
				long maxMemory = runtime.maxMemory() / 1024 / 1024;
				long totalMemory = runtime.totalMemory() / 1024 / 1024;

				fontRenderer.drawStringWithShadow(5, var += offset, String.format(
					"Memory: Free: %d MB, Max: %d MB, Total: %d MB", freeMemory, maxMemory, totalMemory));
				fontRenderer.drawStringWithShadow(5, var += offset,
				                                  String.format("Processors: %d", runtime.availableProcessors()));
			}
		}
		GL11.glPopMatrix();
	}

	public void initGUI() throws SlickException
	{
	}

	public abstract void keyTyped(int key, char c) throws SlickException;

	public void rescaleGUI() throws SlickException
	{
	}

	public abstract void drawScreen(Graphics g, int width, int height) throws SlickException;

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
		final int scale = 2;
		final int scaledWidth = width / scale;
		final int scaledHeight = height / scale;

		GL11.glScalef(scale, scale, 1F);

		for (int i = 0; i < scaledWidth; i += 16)
		{
			for (int j = 0; j < scaledHeight; j += 16)
			{
				Blocks.cobbleStoneWall.getTexture(0).draw(i, j);
			}
		}
		GL11.glScalef(1F / scale, 1F / scale, 1F);
	}

	public void drawDefaultBackground(int width, int height) throws SlickException
	{
		if (this.dr.isGameRunning())
		{
			this.dr.getGraphics().setColor(TRANSPARENT_BLACK);
			this.dr.getGraphics().fillRect(0, 0, width, height);
		}
		else
		{
			this.drawBricks(width, height);
		}
	}

	public void drawHighlight(float x, float y, float width, float height)
	{
		final Graphics graphics = this.dr.getGraphics();

		graphics.setColor(TRANSPARENT_BLACK);
		graphics.fillRect(x, y, width, height);
	}
}
