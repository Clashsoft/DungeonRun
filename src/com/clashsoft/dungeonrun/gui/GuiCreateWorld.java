package com.clashsoft.dungeonrun.gui;

import java.io.File;
import java.util.List;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.engine.I18n;

public class GuiCreateWorld extends GuiListScreen
{
	public GuiScreen	superGui;
	
	public boolean		editMode	= true;
	public String		worldName	= I18n.getString("world.new.template");
	
	public GuiCreateWorld(GuiScreen superGui)
	{
		this.superGui = superGui;
	}
	
	@Override
	public String getTitle()
	{
		return "world.create";
	}
	
	@Override
	public void addEntrys(List<String> s)
	{
		s.add("worldname");
		s.add("gui.done");
		s.add("gui.cancel");
	}
	
	@Override
	public void keyTyped(int key, char c) throws SlickException
	{
		if (editMode && this.selection == 0)
		{
			if (key == 14 && !worldName.isEmpty())
				worldName = worldName.substring(0, worldName.length() - 1);
			else if (Character.isLetterOrDigit(c) || Character.isWhitespace(c))
				worldName += c;
		}
	}
	
	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i == 0)
			this.editMode = !this.editMode;
		else if (i == 1)
		{
			File saves = new File(DungeonRun.getSaveDataFolder(), "saves");
			File newWorld = new File(saves, worldName.trim());
			newWorld.mkdirs();
			
			this.dr.displayGuiScreen(superGui);
		}
		else if (i == 2)
			this.dr.displayGuiScreen(superGui);
	}
	
	@Override
	public String getEntry(int i)
	{
		if (i == 0)
			return worldName;
		return super.getEntry(i);
	}
}
