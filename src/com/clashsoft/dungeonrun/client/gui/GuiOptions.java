package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.I18n;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class GuiOptions extends GuiListScreen
{
	private GuiScreen	superGui;
	
	public GuiOptions(GuiScreen superGui)
	{
		this.superGui = superGui;
	}
	
	@Override
	public void drawScreen(Graphics g, int par1, int par2) throws SlickException
	{
		super.drawScreen(g, par1, par2);
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
		GameSettings gs = this.dr.gameSettings;
		super.updateScreen();
		if (this.selection == 0)
		{
			if (this.input.isKeyDown(Input.KEY_RIGHT))
			{
				if (this.dr.gameSettings.soundVolume + 0.01F <= 1F)
				{
					this.dr.gameSettings.soundVolume += 0.01F;
				}
				else
				{
					gs.soundVolume = 1F;
				}
			}
			else if (this.input.isKeyDown(Input.KEY_LEFT))
			{
				if (this.dr.gameSettings.soundVolume - 0.01F >= 0F)
				{
					this.dr.gameSettings.soundVolume -= 0.01F;
				}
				else
				{
					gs.soundVolume = 0F;
				}
			}
		}
		if (this.selection == 1)
		{
			if (this.input.isKeyDown(Input.KEY_RIGHT))
			{
				if (this.dr.gameSettings.musicVolume + 0.01F <= 1F)
				{
					this.dr.gameSettings.musicVolume += 0.01F;
				}
				else
				{
					gs.musicVolume = 1F;
				}
			}
			else if (this.input.isKeyDown(Input.KEY_LEFT))
			{
				if (this.dr.gameSettings.musicVolume - 0.01F >= 0F)
				{
					this.dr.gameSettings.musicVolume -= 0.01F;
				}
				else
				{
					gs.musicVolume = 0F;
				}
			}
		}
	}
	
	@Override
	public String getTitle()
	{
		return I18n.getString("options.title");
	}

	@Override
	public int entryCount()
	{
		return 4;
	}

	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i == 2)
		{
			this.dr.displayGuiScreen(new GuiOptionsVideo(this));
		}
		if (i == 3)
		{
			this.dr.displayGuiScreen(this.superGui);
		}
	}
	
	@Override
	public String getEntry(int i)
	{
		switch (i)
		{
		case 0:
			return String.format(I18n.getString("options.soundvolume") + ": %.2f", this.dr.gameSettings.soundVolume);
		case 1:
			return String.format(I18n.getString("options.musicvolume") + ": %.2f", this.dr.gameSettings.musicVolume);
		case 2:
			return I18n.getString("options.video.title") + "...";
		case 3:
			return I18n.getString("gui.back");
		}
		return null;
	}
}
