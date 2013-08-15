package com.clashsoft.dungeonrun.gui;

import java.util.List;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;

public class GuiOptionsVideo extends GuiListScreen
{
	private GuiScreen superGui;
	
	public GuiOptionsVideo(GuiScreen superGui)
	{
		this.superGui = superGui;
		this.entrys.clear();
	}
	
	@Override
	public String getTitle()
	{
		return "Video Options";
	}

	@Override
	public void addEntrys(List<String> s)
	{
		s.add("Resolution: ");
		s.add("Fullscreen: ");
		s.add("Use VSync: ");
		s.add("Back");
	}

	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i == 1)
			this.dr.gameSettings.fullScreen = !this.dr.gameSettings.fullScreen;
		else if (i == 2)
			this.dr.gameSettings.useVSync = !this.dr.gameSettings.useVSync;
		else if (i == 3)
			this.dr.displayGuiScreen(superGui);
		this.dr.gameSettings.updateGame();
	}

	@Override
	public String getEntry(int i)
	{
		if (i == 1)
			return entrys.get(i) + DungeonRun.instance.gameSettings.fullScreen;
		if (i == 2)
			return entrys.get(i) + DungeonRun.instance.gameSettings.useVSync;
		return super.getEntry(i);
	}
}
