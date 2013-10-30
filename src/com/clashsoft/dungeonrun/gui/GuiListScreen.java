package com.clashsoft.dungeonrun.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.engine.I18n;
import com.clashsoft.dungeonrun.engine.SoundEngine;

public abstract class GuiListScreen extends GuiScreen
{
	protected int			selection	= 0;
	protected List<String>	entrys		= new ArrayList<String>();
	
	@Override
	public void initGui() throws SlickException
	{
		entrys.clear();
		this.addEntrys(entrys);
	}
	
	@Override
	public void drawScreen(int par1, int par2) throws SlickException
	{
		drawBricks(par1, par2);
		
		DungeonRun.instance.fontRenderer.drawString((par1 - DungeonRun.instance.fontRenderer.getStringWidth(getTitle())) / 2, 20, I18n.getString(getTitle()), 0x00EFFF, true);
		for (int i = 0; i < entrys.size(); i++)
		{
			String text = getEntry(i);
			int width = DungeonRun.instance.fontRenderer.getStringWidth(text);
			DungeonRun.instance.fontRenderer.drawString((par1 - width) / 2 + getFirstEntryPosX(), getFirstEntryPosY() + (i * 20), text, selection == i ? 0xFFFFFF : 0xAAAAAA, true);
		}
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
		if (DungeonRun.getInput().isKeyPressed(Input.KEY_DOWN))
			if (selection == entrys.size() - 1)
				selection = 0;
			else
				selection++;
		if (DungeonRun.getInput().isKeyPressed(Input.KEY_UP))
			if (selection == 0)
				selection = entrys.size() - 1;
			else
				selection--;
		
		if (DungeonRun.getInput().isKeyPressed(Input.KEY_ENTER))
		{
			this.dr.soundEngine.playSoundEffect("resources/audio/click.wav", SoundEngine.DEFAULT_LOCATION);
			this.onEntryUsed(selection);
		}
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
		return I18n.getString(entrys.get(i));
	}
}
