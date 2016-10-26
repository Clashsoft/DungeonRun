package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.SoundEngine;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public abstract class GuiListScreen extends GuiScreen
{
	protected int     selection  = 0;
	protected boolean drawBricks = true;

	@Override
	public void reloadGUI() throws SlickException
	{
	}

	@Override
	public void drawScreen(Graphics g, int width, int height) throws SlickException
	{
		if (this.drawBricks)
		{
			this.drawDefaultBackground(width, height);
		}

		final String title = this.getTitle();

		this.dr.fontRenderer
			.drawString((width - this.dr.fontRenderer.getStringWidth(title)) / 2, 20, title, 0x00EFFF, true);

		for (int i = 0, count = this.entryCount(); i < count; i++)
		{
			final String text = this.getEntry(i);
			final boolean selected = this.selection == i;
			final float textWidth = this.dr.fontRenderer.getStringWidth(text);
			final float x = (width - textWidth) / 2 + this.getXOffset();
			final float y = i * 20 + this.getYOffset();

			this.drawEntry(text, selected, x, y, textWidth);
		}
	}

	protected void drawEntry(String text, boolean selected, float x, float y, float width)
	{
		this.dr.fontRenderer.drawString(x, y, text, selected ? 0xFFFFFF : 0xAAAAAA, true);
	}

	@Override
	public void updateScreen() throws SlickException
	{
		if (Mouse.isButtonDown(0))
		{
			this.clickEntry();
		}
	}

	private void clickEntry() throws SlickException
	{
		this.onEntryUsed(this.selection);
		this.dr.soundEngine.playSoundEffect("click", SoundEngine.DEFAULT_LOCATION);
	}

	@Override
	public void keyTyped(int key, char c) throws SlickException
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

	public abstract void onEntryUsed(int selection) throws SlickException;

	public void onEntrySelect(int selection) throws SlickException
	{
	}

	public int getXOffset()
	{
		return 0;
	}

	public int getYOffset()
	{
		return 40;
	}

	public abstract int entryCount();

	public abstract String getEntry(int i);
}
