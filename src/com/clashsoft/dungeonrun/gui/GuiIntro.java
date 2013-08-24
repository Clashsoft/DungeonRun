package com.clashsoft.dungeonrun.gui;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.util.ResourceRegistry;

public class GuiIntro extends GuiScreen
{
	private int	tick	= 0;
	
	@Override
	public void initGui()
	{
		
	}
	
	@Override
	public void drawScreen(int par1, int par2) throws SlickException
	{
		float f = tick / 40F;
		if (f > 1F)
			f = 1F;
		GL11.glScalef(par1 / 1600F, par2 / 1200F, 1F);
		ResourceRegistry.chaotic_development_bg.draw(0, 0);
		GL11.glScalef(1600F / par1, 1200F / par2, 1F);
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
		if (tick < 20)
			tick++;
		else
		{
			this.dr.displayGuiScreen(new GuiMainMenu());
		}
	}
	
}
