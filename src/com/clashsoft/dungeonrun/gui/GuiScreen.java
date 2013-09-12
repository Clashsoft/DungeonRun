package com.clashsoft.dungeonrun.gui;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;

public abstract class GuiScreen
{
	protected DungeonRun		dr;
	
	protected int				width;
	protected int				height;
	
	protected List<GuiButton>	buttonList	= new LinkedList<GuiButton>();
	
	public final void init(DungeonRun game) throws SlickException
	{
		this.dr = game;
		this.initGui();
	}
	
	public final void render(int par1, int par2) throws SlickException
	{
		GL11.glScalef(par1 / 544F, par2 / 256F, 1F);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		this.width = par1;
		this.height = par2;
		this.drawScreen(544, 256);
		for (GuiButton button : buttonList)
		{
			if (button != null)
				button.render();
		}
		GL11.glScalef(544F / par1, 256F / par2, 1F);
	}
	
	public final void update(DungeonRun game) throws SlickException
	{
		this.updateScreen();
	}
	
	public abstract void initGui() throws SlickException;
	
	public abstract void drawScreen(int par1, int par2) throws SlickException;
	
	public abstract void updateScreen() throws SlickException;
	
	public double getRescaleFactorX(int width)
	{
		return width / 544D;
	}
	
	public double getRescaleFactorY(int height)
	{
		return height / 256D;
	}
	
	public boolean isMouseInRegion(float x, float y, float sizeX, float sizeY)
	{
		double mouseX = Mouse.getX() / getRescaleFactorX(width) * 1.0D;
		double mouseY = (height - Mouse.getY()) / getRescaleFactorY(height) * 1.0D;
		return (mouseX > x && mouseX < x + sizeX && mouseY > y && mouseY < y + sizeY);
	}
	
	public void setWorldSaving(boolean state)
	{
		
	}
}
