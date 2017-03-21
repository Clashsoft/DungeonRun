package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.I18n;
import org.newdawn.slick.SlickException;

public class GuiDeath extends GuiListScreen
{
	@Override
	public String getTitle()
	{
		return I18n.getString("game.gameover.title");
	}

	@Override
	public int entryCount()
	{
		return 2;
	}

	@Override
	public String getEntry(int i)
	{
		switch (i)
		{
		case 0:
			return I18n.getString("game.respawn");
		case 1:
			return I18n.getString("mainmenu.title");
		}

		return null;
	}

	@Override
	public void onEntryUsed(int i)
	{
		switch (i)
		{
		case 0:
			this.dr.startGame();
			break;
		case 1:
			this.dr.stopGame();
			break;
		}
	}
}
