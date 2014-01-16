package com.clashsoft.dungeonrun.client.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.server.DungeonRunServer;
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
		worlds.clear();
		
		File saves = new File(DungeonRunServer.getSaveDataFolder(), "saves");
		
		File[] files = saves.listFiles();
		if (files != null)
		{
			for (File f : saves.listFiles())
			{
				if (f.isDirectory())
				{
					worlds.add(f.getName());
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
		s.addAll(worlds);
		s.add("world.create");
		s.add("gui.cancel");
	}
	
	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i >= worlds.size())
		{
			i -= worlds.size();
			
			if (i == 0)
				this.dr.displayGuiScreen(new GuiCreateWorld(this));
			else if (i == 1)
				this.dr.displayGuiScreen(superGui);
		}
		else
		{
			String worldName = worlds.get(i);
			
			this.dr.theWorld = new World(new WorldInfo(worldName));
			this.dr.startWorld();
		}
	}
}
