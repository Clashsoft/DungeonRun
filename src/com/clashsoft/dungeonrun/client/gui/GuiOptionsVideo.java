package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.I18n;
import org.newdawn.slick.SlickException;

public class GuiOptionsVideo extends GuiListScreen
{
	private GuiScreen superGui;

	public GuiOptionsVideo(GuiScreen superGui)
	{
		this.superGui = superGui;
	}

	@Override
	public String getTitle()
	{
		return I18n.getString("options.video.title");
	}

	@Override
	public int entryCount()
	{
		return 4;
	}

	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		switch (i)
		{
		case 0:
			this.dr.gameSettings.guiSize++;
			this.dr.gameSettings.guiSize %= 4;
			break;
		case 1:
			this.dr.gameSettings.fullScreen = !this.dr.gameSettings.fullScreen;
			break;
		case 2:
			this.dr.gameSettings.useVSync = !this.dr.gameSettings.useVSync;
			break;
		case 3:
			this.dr.displayGuiScreen(this.superGui);
			break;
		}
		this.dr.gameSettings.updateGame();
	}

	@Override
	public String getEntry(int i)
	{
		switch (i)
		{
		case 0:
			return I18n.getString("options.video.guiscale") + ": " + I18n.getString(
				"options.video.guiscale." + this.dr.gameSettings.guiSize);
		case 1:
			return I18n.getString("options.video.fullscreen") + ": " + this.dr.gameSettings.fullScreen;
		case 2:
			return I18n.getString("options.video.vsync") + ": " + this.dr.gameSettings.useVSync;
		case 3:
			return I18n.getString("gui.back");
		}
		return null;
	}
}
