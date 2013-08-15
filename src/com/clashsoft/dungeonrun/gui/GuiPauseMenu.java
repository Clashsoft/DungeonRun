package com.clashsoft.dungeonrun.gui;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import com.clashsoft.dungeonrun.DungeonRun;

public class GuiPauseMenu extends GuiListScreen
{
	@Override
	public void drawScreen(int par1, int par2) throws SlickException
	{
		this.dr.theIngameGui.drawScreen(par1, par2);
		
		Color c = new Color(0F, 0F, 0F, 0.5F);
		DungeonRun.getGraphics().setColor(c);
		DungeonRun.getGraphics().fill(new Rectangle(0, 0, par1, par2));
		
		super.drawScreen(par1, par2);
	}

	@Override
	public String getTitle()
	{
		return "Game Menu";
	}

	@Override
	public void addEntrys(List<String> s)
	{
		s.add("Back to Game");
		s.add("Options");
		s.add("Main Menu");
	}

	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i == 0)
			this.dr.unpauseGame();
		else if (i == 1)
			this.dr.displayGuiScreen(new GuiOptions(this));
		else if (i == 2)
			this.dr.displayGuiScreen(new GuiMainMenu());
	}
}
