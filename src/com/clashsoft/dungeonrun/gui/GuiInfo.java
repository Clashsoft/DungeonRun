package com.clashsoft.dungeonrun.gui;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.engine.I18n;

public class GuiInfo extends GuiScreen
{
	public String message;
	
	public GuiInfo(String message)
	{
		this.message = I18n.getString(message);
	}
	
	@Override
	public void initGui() throws SlickException
	{
	}
	
	@Override
	public void drawScreen(int par1, int par2) throws SlickException
	{
		this.drawBricks(par1, par2);
		this.dr.fontRenderer.drawString((par1 - (this.dr.fontRenderer.getStringWidth(message))) / 2, (par2 - this.dr.fontRenderer.getStringHeigth(message)) / 2, message, 0xFFFFFF);
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
	}
	
}
