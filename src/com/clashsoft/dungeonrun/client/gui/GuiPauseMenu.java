package com.clashsoft.dungeonrun.client.gui;

import java.util.List;

import org.newdawn.slick.SlickException;

public class GuiPauseMenu extends GuiListScreen
{
	@Override
	public String getTitle()
	{
		return "pausemenu.title";
	}
	
	@Override
	public void addEntrys(List<String> s)
	{
		s.add("game.back");
		s.add("options.title");
		s.add("mainmenu.title");
	}
	
	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i == 0)
		{
			this.dr.resumeGame();
		}
		else if (i == 1)
		{
			this.dr.displayGuiScreen(new GuiOptions(this));
		}
		else if (i == 2)
		{
			this.dr.stopGame();
		}
	}
}
