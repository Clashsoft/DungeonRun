package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.I18n;

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
			this.dr.thePlayer.respawn();
			this.dr.resumeGame();
			break;
		case 1:
			this.dr.stopGame();
			break;
		}
	}
}
