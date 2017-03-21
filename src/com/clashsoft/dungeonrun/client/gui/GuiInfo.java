package com.clashsoft.dungeonrun.client.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.client.engine.I18n;

public class GuiInfo extends GuiScreen
{
	public String	message;
	
	public GuiInfo(String message)
	{
		this.message = I18n.getString(message);
	}
	
	@Override
	public void drawScreen(Graphics g, int par1, int par2)
	{
		this.drawBricks(par1, par2);
		this.dr.fontRenderer.drawStringWithShadow((par1 - this.dr.fontRenderer.getStringWidth(this.message)) / 2, (par2 - this.dr.fontRenderer.getStringHeigth(this.message)) / 2, this.message, 0xFFFFFF);
	}
	
	@Override
	public void updateScreen()
	{
	}
	
	@Override
	public void keyTyped(int key, char c)
	{
	}
}
