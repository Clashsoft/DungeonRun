package com.clashsoft.dungeonrun.gui;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.block.Block;

public class GuiInfo extends GuiScreen
{
	public String message;
	
	public GuiInfo(String message)
	{
		this.message = message;
	}
	
	@Override
	public void initGui() throws SlickException
	{
	}
	
	@Override
	public void drawScreen(int par1, int par2) throws SlickException
	{
		for (int i = 0; i < par1 / 16F; i++)
		{
			for (int j = 0; j < par2 / 16F; j++)
			{
				Block.brick.getBlockTextureFromSideAndMetadata(0, 0).draw(i * 16, j * 16);
			}
		}
		this.dr.fontRenderer.drawString((par1 - (this.dr.fontRenderer.getStringWidth(message))) / 2, (par2 - this.dr.fontRenderer.getStringHeigth(message)) / 2, message, 0xFFFFFF);
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
	}
	
}
