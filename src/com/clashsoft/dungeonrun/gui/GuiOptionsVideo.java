package com.clashsoft.dungeonrun.gui;

import java.util.List;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.engine.I18n;

public class GuiOptionsVideo extends GuiListScreen
{
	private GuiScreen	superGui;
	
	public GuiOptionsVideo(GuiScreen superGui)
	{
		this.superGui = superGui;
		this.entrys.clear();
	}
	
	@Override
	public String getTitle()
	{
		return "options.video.title";
	}
	
	@Override
	public void addEntrys(List<String> s)
	{
		s.add("options.video.guiscale");
		s.add("options.video.fullscreen");
		s.add("options.video.vsync");
		s.add("gui.back");
	}
	
	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i == 0)
		{
			this.dr.gameSettings.guiSize++;
			this.dr.gameSettings.guiSize %= 4;
		}
		else if (i == 1)
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
		if (i == 0)
			return I18n.getString("options.video.guiscale") + ": " + DungeonRun.instance.gameSettings.guiSize;
		else if (i == 1)
			return I18n.getString("options.video.fullscreen") + ": " + DungeonRun.instance.gameSettings.fullScreen;
		else if (i == 2)
			return I18n.getString("options.video.vsync") + ": " + DungeonRun.instance.gameSettings.useVSync;
		return super.getEntry(i);
	}
}
