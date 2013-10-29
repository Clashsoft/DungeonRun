package com.clashsoft.dungeonrun.gui;

import java.util.List;

import org.newdawn.slick.SlickException;

public class GuiPauseMenu extends GuiListScreen
{
	@Override
	public void drawScreen(int par1, int par2) throws SlickException
	{
		this.drawDefaultBackground(par1, par2);
		super.drawScreen(par1, par2);
	}
	
	@Override
	public String getTitle()
	{
		return "Game Menu";
	}
	
	@Override
	public void addEntrys(List<String> s)
	{
		s.add("Back to Game");
		s.add("Options");
		s.add("Main Menu");
	}
	
	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i == 0)
			this.dr.unpauseGame();
		else if (i == 1)
			this.dr.displayGuiScreen(new GuiOptions(this));
		else if (i == 2)
			this.dr.endGame();
	}
}
