package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.SoundEngine;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public abstract class GuiListScreen extends GuiScreen
{
	protected static final int SELECTION_COLOR = 0xFFFFFF;
	protected static final int ENTRY_COLOR     = 0xAAAAAA;
	protected static final int TITLE_COLOR     = 0x00EFFF;

	protected int     selection  = 0;
	protected boolean drawBricks = true;

	@Override
	public void drawScreen(Graphics g, int width, int height)
	{
		if (this.drawBricks)
		{
			this.drawDefaultBackground(width, height);
		}

		final String title = this.getTitle();

		this.dr.fontRenderer
			.drawString((width - this.dr.fontRenderer.getStringWidth(title)) / 2, this.getYOffset(), title, TITLE_COLOR, true);

		for (int i = 0, count = this.entryCount(); i < count; i++)
		{
			this.drawEntry(i, width, height);
		}
	}

	protected void drawEntry(int i, int width, int height)
	{
		final String text = this.getEntry(i);
		final float textWidth = this.dr.fontRenderer.getStringWidth(text);
		final float x = (width - textWidth) / 2 + this.getXOffset();
		final float y = i * 20 + this.getYOffset() + 20;

		this.drawEntry(text, i, x, y, textWidth);
	}

	protected void drawEntry(String text, int index, float x, float y, float textWidth)
	{
		if (index == this.selection)
		{
			this.drawHighlight(x - 4, y - 4, textWidth + 8, 16);
		}

		this.dr.fontRenderer.drawString(x, y, text, index == this.selection ? SELECTION_COLOR : ENTRY_COLOR, true);
	}

	@Override
	public void updateScreen()
	{
		if (Mouse.isButtonDown(0))
		{
			this.clickEntry();
		}
	}

	private void clickEntry()
	{
		this.onEntryUsed(this.selection);
		this.dr.soundEngine.playSoundEffect("click", SoundEngine.DEFAULT_LOCATION);
	}

	@Override
	public void keyTyped(int key, char c)
	{
		switch (key)
		{
		case Input.KEY_UP:
			if (this.selection == 0)
			{
				this.selection = this.entryCount() - 1;
			}
			else
			{
				this.selection--;
			}
			this.onEntrySelect(this.selection);
			break;
		case Input.KEY_DOWN:
			if (this.selection == this.entryCount() - 1)
			{
				this.selection = 0;
			}
			else
			{
				this.selection++;
			}
			this.onEntrySelect(this.selection);
			break;
		case Input.KEY_ENTER:
			this.clickEntry();
			break;
		}
	}

	public abstract String getTitle();

	public abstract void onEntryUsed(int selection);

	public void onEntrySelect(int selection)
	{
	}

	public int getXOffset()
	{
		return 0;
	}

	public int getYOffset()
	{
		return 20;
	}

	public abstract int entryCount();

	public abstract String getEntry(int i);
}
