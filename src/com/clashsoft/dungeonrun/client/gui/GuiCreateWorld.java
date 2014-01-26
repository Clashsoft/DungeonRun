package com.clashsoft.dungeonrun.client.gui;

import java.io.File;
import java.util.List;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.client.engine.I18n;

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
		if (this.editMode && this.selection == 0)
		{
			if (key == 14 && !this.worldName.isEmpty())
			{
				this.worldName = this.worldName.substring(0, this.worldName.length() - 1);
			}
			else if (Character.isLetterOrDigit(c) || c == ' ')
			{
				this.worldName += c;
			}
		}
	}
	
	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i == 0)
		{
			this.editMode = !this.editMode;
		}
		else if (i == 1)
		{
			File saves = new File(this.dr.getSaveDataFolder(), "saves");
			File newWorld = new File(saves, this.worldName.trim());
			newWorld.mkdirs();
			
			this.dr.displayGuiScreen(this.superGui);
		}
		else if (i == 2)
		{
			this.dr.displayGuiScreen(this.superGui);
		}
	}
	
	@Override
	public String getEntry(int i)
	{
		if (i == 0)
		{
			return this.worldName;
		}
		return super.getEntry(i);
	}
}
