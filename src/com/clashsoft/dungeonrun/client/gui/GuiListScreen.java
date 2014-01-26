package com.clashsoft.dungeonrun.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.client.engine.SoundEngine;

public abstract class GuiListScreen extends GuiScreen
{
	protected int			selection	= 0;
	protected List<String>	entrys		= new ArrayList<String>();
	protected boolean		drawBricks	= true;
	
	@Override
	public void initGui() throws SlickException
	{
		this.entrys.clear();
		this.addEntrys(this.entrys);
	}
	
	@Override
	public void drawScreen(int width, int height) throws SlickException
	{
		if (this.drawBricks)
		{
			this.drawDefaultBackground(width, height);
		}
		
		this.dr.fontRenderer.drawString((width - this.dr.fontRenderer.getStringWidth(this.getTitle())) / 2, 20, I18n.getString(this.getTitle()), 0x00EFFF, true);
		for (int i = 0; i < this.entrys.size(); i++)
		{
			String text = this.getEntry(i);
			int i1 = this.dr.fontRenderer.getStringWidth(text);
			this.dr.fontRenderer.drawString((width - i1) / 2 + this.getFirstEntryPosX(), this.getFirstEntryPosY() + i * 20, text, this.selection == i ? 0xFFFFFF : 0xAAAAAA, true);
		}
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
		if (this.input.isKeyPressed(Input.KEY_DOWN))
		{
			if (this.selection == this.entrys.size() - 1)
			{
				this.selection = 0;
			}
			else
			{
				this.selection++;
			}
		}
		if (this.input.isKeyPressed(Input.KEY_UP))
		{
			if (this.selection == 0)
			{
				this.selection = this.entrys.size() - 1;
			}
			else
			{
				this.selection--;
			}
		}
		
		if (this.input.isKeyPressed(Input.KEY_ENTER))
		{
			this.onEntryUsed(this.selection);
			this.dr.soundEngine.playSoundEffect("resources/audio/click.wav", SoundEngine.DEFAULT_LOCATION);
		}
	}
	
	@Override
	public void keyTyped(int key, char c) throws SlickException
	{
	}
	
	public abstract String getTitle();
	
	public abstract void addEntrys(List<String> s);
	
	public abstract void onEntryUsed(int i) throws SlickException;
	
	public int getFirstEntryPosX()
	{
		return 0;
	}
	
	public int getFirstEntryPosY()
	{
		return 40;
	}
	
	public String getEntry(int i)
	{
		return I18n.getString(this.entrys.get(i));
	}
}
