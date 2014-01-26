package com.clashsoft.dungeonrun.client.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.dungeonrun.world.WorldInfo;

public class GuiSelectWorld extends GuiListScreen
{
	public GuiScreen	superGui;
	
	public List<String>	worlds	= new ArrayList<String>();
	
	public GuiSelectWorld(GuiScreen superGui)
	{
		this.superGui = superGui;
	}
	
	@Override
	public void initGui() throws SlickException
	{
		this.worlds.clear();
		
		File saves = new File(this.dr.getSaveDataFolder(), "saves");
		
		File[] files = saves.listFiles();
		if (files != null)
		{
			for (File f : saves.listFiles())
			{
				if (f.isDirectory())
				{
					this.worlds.add(f.getName());
				}
			}
		}
		
		super.initGui();
	}
	
	@Override
	public String getTitle()
	{
		return "world.select";
	}
	
	@Override
	public void addEntrys(List<String> s)
	{
		s.addAll(this.worlds);
		s.add("world.create");
		s.add("gui.cancel");
	}
	
	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i >= this.worlds.size())
		{
			i -= this.worlds.size();
			
			if (i == 0)
			{
				this.dr.displayGuiScreen(new GuiCreateWorld(this));
			}
			else if (i == 1)
			{
				this.dr.displayGuiScreen(this.superGui);
			}
		}
		else
		{
			String worldName = this.worlds.get(i);
			this.dr.startWorld(new World(new WorldInfo(worldName)));
		}
	}
}
