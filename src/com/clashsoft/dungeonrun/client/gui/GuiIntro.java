package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.util.ResourceHelper;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class GuiIntro extends GuiScreen
{
	private static final int   MAX_TICK = 25;
	private static final float FADEIN   = 0.25F;

	private int tick = 0;

	@Override
	public void rescaleGUI()
	{

	}

	@Override
	public void drawScreen(Graphics g, int par1, int par2) throws SlickException
	{
		float alpha;

		if (this.tick > MAX_TICK * FADEIN)
		{
			alpha = 1F;
		}
		else
		{
			alpha = this.tick / (MAX_TICK * FADEIN);
		}

		GL11.glPushMatrix();
		GL11.glScalef(par1 / 1920, par2 / 1080, 1F);
		ResourceHelper.introBackground.setAlpha(alpha);
		ResourceHelper.introBackground.draw(0, 0);
		GL11.glPopMatrix();
	}

	@Override
	public void updateScreen() throws SlickException
	{
		if (this.tick < MAX_TICK)
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
