package com.clashsoft.dungeonrun.client.gui;

import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.server.DungeonRunServer;

public class GuiOptions extends GuiListScreen
{
	private GuiScreen	superGui;
	
	public GuiOptions(GuiScreen superGui)
	{
		this.superGui = superGui;
	}
	
	@Override
	public void initGui() throws SlickException
	{
		super.initGui();
	}
	
	@Override
	public void drawScreen(int par1, int par2) throws SlickException
	{
		super.drawScreen(par1, par2);
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
		GameSettings gs = this.dr.gameSettings;
		super.updateScreen();
		if (selection == 0)
			if (DungeonRunServer.getInput().isKeyDown(Input.KEY_RIGHT))
			{
				if (this.dr.gameSettings.soundVolume + 0.01F <= 1F)
					this.dr.gameSettings.soundVolume += 0.01F;
				else
					gs.soundVolume = 1F;
			}
			else if (DungeonRunServer.getInput().isKeyDown(Input.KEY_LEFT))
			{
				if (this.dr.gameSettings.soundVolume - 0.01F >= 0F)
					this.dr.gameSettings.soundVolume -= 0.01F;
				else
					gs.soundVolume = 0F;
			}
		if (selection == 1)
			if (DungeonRunServer.getInput().isKeyDown(Input.KEY_RIGHT))
			{
				if (this.dr.gameSettings.musicVolume + 0.01F <= 1F)
					this.dr.gameSettings.musicVolume += 0.01F;
				else
					gs.musicVolume = 1F;
			}
			else if (DungeonRunServer.getInput().isKeyDown(Input.KEY_LEFT))
			{
				if (this.dr.gameSettings.musicVolume - 0.01F >= 0F)
					this.dr.gameSettings.musicVolume -= 0.01F;
				else
					gs.musicVolume = 0F;
			}
	}
	
	@Override
	public String getTitle()
	{
		return "options.title";
	}
	
	@Override
	public void addEntrys(List<String> s)
	{
		s.add("options.soundvolume");
		s.add("options.musicvolume");
		s.add("options.video.title");
		s.add("gui.back");
	}
	
	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i == 2)
			this.dr.displayGuiScreen(new GuiOptionsVideo(this));
		if (i == 3)
			this.dr.displayGuiScreen(superGui);
	}
	
	@Override
	public String getEntry(int i)
	{
		if (i == 0)
			return String.format(I18n.getString("options.soundvolume") + ": %.2f", dr.gameSettings.soundVolume);
		if (i == 1)
			return String.format(I18n.getString("options.musicvolume") + ": %.2f", dr.gameSettings.musicVolume);
		return super.getEntry(i);
	}
}
