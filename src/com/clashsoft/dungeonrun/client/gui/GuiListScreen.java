package com.clashsoft.dungeonrun.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.client.engine.SoundEngine;
import com.clashsoft.dungeonrun.server.DungeonRunServer;

public abstract class GuiListScreen extends GuiScreen
{
	protected int			selection	= 0;
	protected List<String>	entrys		= new ArrayList<String>();
	protected boolean		drawBricks	= true;
	
	@Override
	public void initGui() throws SlickException
	{
		entrys.clear();
		this.addEntrys(entrys);
	}
	
	@Override
	public void drawScreen(int width, int height) throws SlickException
	{
		if (drawBricks)
			drawDefaultBackground(width, height);
		
		DungeonRunServer.instance.fontRenderer.drawString((width - DungeonRunServer.instance.fontRenderer.getStringWidth(getTitle())) / 2, 20, I18n.getString(getTitle()), 0x00EFFF, true);
		for (int i = 0; i < entrys.size(); i++)
		{
			String text = getEntry(i);
			int i1 = DungeonRunServer.instance.fontRenderer.getStringWidth(text);
			DungeonRunServer.instance.fontRenderer.drawString((width - i1) / 2 + getFirstEntryPosX(), getFirstEntryPosY() + (i * 20), text, selection == i ? 0xFFFFFF : 0xAAAAAA, true);
		}
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
		if (DungeonRunServer.getInput().isKeyPressed(Input.KEY_DOWN))
			if (selection == entrys.size() - 1)
				selection = 0;
			else
				selection++;
		if (DungeonRunServer.getInput().isKeyPressed(Input.KEY_UP))
			if (selection == 0)
				selection = entrys.size() - 1;
			else
				selection--;
		
		if (DungeonRunServer.getInput().isKeyPressed(Input.KEY_ENTER))
		{
			this.dr.soundEngine.playSoundEffect("resources/audio/click.wav", SoundEngine.DEFAULT_LOCATION);
			this.onEntryUsed(selection);
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
		return I18n.getString(entrys.get(i));
	}
}
