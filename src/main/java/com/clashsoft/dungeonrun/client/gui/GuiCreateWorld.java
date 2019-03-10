package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.I18n;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import java.io.File;

public class GuiCreateWorld extends GuiListScreen
{
	public GuiScreen superGui;

	public String worldName = I18n.getString("world.new.template");

	public GuiCreateWorld(GuiScreen superGui)
	{
		this.superGui = superGui;
	}

	@Override
	public String getTitle()
	{
		return I18n.getString("world.create");
	}

	@Override
	public void keyTyped(int key, char c)
	{
		if (this.selection != 0)
		{
			super.keyTyped(key, c);
			return;
		}

		if (key == Keyboard.KEY_BACK && !this.worldName.isEmpty())
		{
			this.worldName = this.worldName.substring(0, this.worldName.length() - 1);
		}
		else if (this.worldName.length() < 32 && isValidNameChar(c))
		{
			this.worldName += c;
		}
		else
		{
			super.keyTyped(key, c);
		}
	}

	private static boolean isValidNameChar(char c)
	{
		switch (c)
		{
		case ' ':
		case '-':
		case '+':
		case '_':
			return true;
		}
		return Character.isLetterOrDigit(c);
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
			return I18n.getString("world.create.name") + ": " + this.worldName;
		case 1:
			return I18n.getString("gui.done");
		case 2:
			return I18n.getString("gui.cancel");
		}
		return null;
	}

	@Override
	protected void drawEntry(String text, int index, float x, float y, float textWidth)
	{
		if (index > 0)
		{
			y += 10;
		}

		super.drawEntry(text, index, x, y, textWidth);

		if (index == 0 && this.selection == 0 && this.dr.getTick() % 20 < 10)
		{
			this.dr.getGraphics().setColor(Color.white);
			this.dr.getGraphics().fillRect(x + textWidth + 1, y, 1, 8);
		}
	}

	@Override
	public void onEntryUsed(int i)
	{
		if (i == 1)
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
}
