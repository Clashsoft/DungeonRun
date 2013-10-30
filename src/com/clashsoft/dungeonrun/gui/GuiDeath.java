package com.clashsoft.dungeonrun.gui;

import java.util.List;

import org.newdawn.slick.SlickException;

public class GuiDeath extends GuiListScreen
{
	@Override
	public String getTitle()
	{
		return "game.gameover.title";
	}
	
	@Override
	public void addEntrys(List<String> s)
	{
		s.add("game.respawn");
		s.add("game.mainmenu");
	}
	
	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i == 0)
			this.dr.startGame();
		else
			this.dr.endGame();
	}
	
}
