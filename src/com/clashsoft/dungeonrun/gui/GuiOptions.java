package com.clashsoft.dungeonrun.gui;

import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.util.I18n;

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
			if (DungeonRun.getInput().isKeyDown(Input.KEY_RIGHT))
			{
				if (this.dr.gameSettings.soundVolume + 0.01F <= 1F)
					this.dr.gameSettings.soundVolume += 0.01F;
				else
					gs.soundVolume = 1F;
			}
			else if (DungeonRun.getInput().isKeyDown(Input.KEY_LEFT))
			{
				if (this.dr.gameSettings.soundVolume - 0.01F >= 0F)
					this.dr.gameSettings.soundVolume -= 0.01F;
				else
					gs.soundVolume = 0F;
			}
		if (selection == 1)
			if (DungeonRun.getInput().isKeyDown(Input.KEY_RIGHT))
			{
				if (this.dr.gameSettings.musicVolume + 0.01F <= 1F)
					this.dr.gameSettings.musicVolume += 0.01F;
				else
					gs.musicVolume = 1F;
			}
			else if (DungeonRun.getInput().isKeyDown(Input.KEY_LEFT))
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
		s.add("videooptions.title");
		s.add("options.back");
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
			return I18n.getStringFormatted("options.soundvolume.value", dr.gameSettings.soundVolume);
		if (i == 1)
			return I18n.getStringFormatted("options.musicvolume.value", dr.gameSettings.soundVolume);
		return super.getEntry(i);
	}
}
