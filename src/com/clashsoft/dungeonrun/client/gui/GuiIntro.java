package com.clashsoft.dungeonrun.client.gui;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.util.ResourceHelper;

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
		float f = this.tick / 40F;
		
		if (this.tick > 60)
		{
			f = 2.5F - f;
		}
		else if (this.tick > 40)
		{
			f = 1F;
		}
		
		GL11.glPushMatrix();
		GL11.glScalef(par1 / 1600F, par2 / 1200F, 1F);
		ResourceHelper.chaotic_development_bg.setAlpha(f);
		ResourceHelper.chaotic_development_bg.draw(0, 0);
		GL11.glPopMatrix();
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
		if (this.tick < 100)
		{
			this.tick++;
		}
		else
		{
			this.dr.displayGuiScreen(new GuiMainMenu());
		}
	}
	
	@Override
	public void keyTyped(int key, char c) throws SlickException
	{
	}
}
