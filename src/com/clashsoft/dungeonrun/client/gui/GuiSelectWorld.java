package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.dungeonrun.world.WorldInfo;
import org.newdawn.slick.SlickException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GuiSelectWorld extends GuiListScreen
{
	public GuiScreen superGui;

	public List<String> worlds = new ArrayList<>();

	public GuiSelectWorld(GuiScreen superGui)
	{
		this.superGui = superGui;
	}

	@Override
	public void reloadGUI() throws SlickException
	{
		this.worlds.clear();

		final File saveFolder = new File(this.dr.getSaveDataFolder(), "saves");
		final File[] worldDirs = saveFolder.listFiles();

		if (worldDirs != null)
		{
			for (File file : worldDirs)
			{
				if (file.isDirectory())
				{
					this.worlds.add(file.getName());
				}
			}
		}

		super.reloadGUI();
	}

	@Override
	public String getTitle()
	{
		return I18n.getString("world.select");
	}

	@Override
	public int entryCount()
	{
		return this.worlds.size() + 2;
	}

	@Override
	public String getEntry(int i)
	{
		if (i < this.worlds.size())
		{
			return this.worlds.get(i);
		}

		i -= this.worlds.size();
		switch (i)
		{
		case 0:
			return I18n.getString("world.create") + "...";
		case 1:
			return I18n.getString("gui.cancel");
		}
		return null;
	}

	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i < this.worlds.size())
		{
			String worldName = this.worlds.get(i);
			this.dr.startWorld(new World(new WorldInfo(worldName)));
			return;
		}

		i -= this.worlds.size();
		switch (i)
		{
		case 0:
			this.dr.displayGuiScreen(new GuiCreateWorld(this));
			break;
		case 1:
			this.dr.displayGuiScreen(this.superGui);
			break;
		}
	}
}
