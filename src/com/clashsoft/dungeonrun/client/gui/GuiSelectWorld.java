package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.dungeonrun.world.WorldInfo;
import org.newdawn.slick.SlickException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiSelectWorld extends GuiListScreen
{
	public GuiScreen superGui;

	public List<File> worlds = new ArrayList<>();

	public GuiSelectWorld(GuiScreen superGui)
	{
		this.superGui = superGui;
	}

	@Override
	public void reloadGUI() throws SlickException
	{
		this.worlds.clear();

		final File[] worldDirs = DungeonRun.SAVES_DIRECTORY.listFiles();

		if (worldDirs != null)
		{
			for (File file : worldDirs)
			{
				if (file.isDirectory())
				{
					this.worlds.add(file);
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
		return this.worlds.size() + 3;
	}

	@Override
	public String getEntry(int i)
	{
		if (i < this.worlds.size())
		{
			return this.worlds.get(i).getName();
		}

		i -= this.worlds.size();
		switch (i)
		{
		case 0:
			return I18n.getString("world.create") + "...";
		case 1:
			return I18n.getString("world.saves_dir.open") + "...";
		case 2:
			return I18n.getString("gui.cancel");
		}
		return null;
	}

	@Override
	protected void drawEntry(int i, int width, int height)
	{
		super.drawEntry(i, width, height);
	}

	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i < this.worlds.size())
		{
			final File file = this.worlds.get(i);
			this.dr.startWorld(new World(new WorldInfo(file.getName()), file));
			return;
		}
		i -= this.worlds.size();

		switch (i)
		{
		case 0:
			this.dr.displayGuiScreen(new GuiCreateWorld(this));
			break;
		case 1:
			try
			{
				Desktop.getDesktop().open(DungeonRun.SAVES_DIRECTORY);
			}
			catch (IOException ignored)
			{
			}
			break;
		case 2:
			this.dr.displayGuiScreen(this.superGui);
			break;
		}
	}
}
