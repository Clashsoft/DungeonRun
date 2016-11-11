package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.I18n;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class GuiPauseMenu extends GuiListScreen
{
	@Override
	public String getTitle()
	{
		return I18n.getString("pausemenu.title");
	}

	@Override
	public int entryCount()
	{
		return 3;
	}

	@Override
	public String getEntry(int i)
	{
		switch (i)
		{
		case 0:
			return I18n.getString("game.back");
		case 1:
			return I18n.getString("options.title");
		case 2:
			return I18n.getString("mainmenu.title");
		}

		return null;
	}

	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		switch (i)
		{
		case 0:
			this.dr.resumeGame();
			break;
		case 1:
			this.dr.displayGuiScreen(new GuiOptions(this));
			break;
		case 2:
			this.dr.stopGame();
			break;
		}
	}

	@Override
	public void keyTyped(int key, char c) throws SlickException
	{
		if (key == Input.KEY_ESCAPE)
		{
			this.dr.resumeGame();
			return;
		}

		super.keyTyped(key, c);
	}
}
