package com.clashsoft.dungeonrun.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.block.Block;
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
		DungeonRun.getGraphics().setColor(Color.lightGray);
		DungeonRun.getGraphics().drawString(getTitle(), (par1 - DungeonRun.getGraphics().getFont().getWidth(getTitle())) / 2, 20);
		for (int i = 0; i < entrys.size(); i++)
		{
			String text = getEntry(i);
			int width = DungeonRun.getGraphics().getFont().getWidth(text);
			if (i == selection)
				DungeonRun.getGraphics().setColor(Color.white);
			else
				DungeonRun.getGraphics().setColor(Color.lightGray);
			DungeonRun.getGraphics().drawString(text, (par1 - width) / 2 + getFirstEntryPosX(), getFirstEntryPosY() + (i * 20));
			DungeonRun.getGraphics().setColor(Color.lightGray);
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
		return entrys.get(i);
	}
	
	public void drawBricks(int width, int heigth) throws SlickException
	{
		for (int i = 0; i < width / 16F; i++)
		{
			for (int j = 0; j < heigth / 16F; j++)
			{
				Block.brick.getBlockTextureFromSideAndMetadata(0, 0).draw(i * 16, j * 16);
			}
		}
	}
}
