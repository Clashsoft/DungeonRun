package com.clashsoft.dungeonrun.gui;

import java.util.List;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.util.ResourceRegistry;

public class GuiMainMenu extends GuiListScreen
{
	@Override
	public void initGui() throws SlickException
	{
		super.initGui();
	}
	

	@Override
	public void drawScreen(int par1, int par2) throws SlickException
	{
		int longestStringLength = 0;
		for (int i = 0; i < (float)par1 / 16F; i++)
		{
			for (int j = 0; j < (float)par2 / 16F; j++)
			{
				Block.brick.getBlockTextureFromSideAndMetadata(0, 0).draw(i * 16, j * 16);
			}
		}
		for (int i = 0; i < entrys.size(); i++)
		{
			String s = entrys.get(i);
			int length = DungeonRun.getGraphics().getFont().getWidth(s);
			int posY = (par2 - (entrys.size() * DungeonRun.getGraphics().getFont().getHeight(s))) / 2 + (i * 20);
			DungeonRun.getGraphics().drawString(s, (int)((float)(par1 - length) / 2F), posY);
			if (length >= longestStringLength)
				longestStringLength = length;
			if (selection == i)
			{
				ResourceRegistry.iconsSprite.getSprite(3, 0).draw((par1 - longestStringLength) / 2 - 18 - 4, posY);
				ResourceRegistry.iconsSprite.getSprite(3, 0).draw((par1 + longestStringLength) / 2 + 4, posY);
			}
		}
	}

	@Override
	public void updateScreen() throws SlickException
	{
		super.updateScreen();
	}

	@Override
	public String getTitle()
	{
		return "";
	}

	@Override
	public void addEntrys(List<String> s)
	{
		s.add("Singleplayer");
		s.add("Options");
		s.add("Exit Game");
	}

	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		if (i == 0)
			this.dr.startGame();
		else if (i == 1)
			this.dr.displayGuiScreen(new GuiOptions(this));
		else if (i == 2)
			this.dr.theGameContainer.exit();
	}

	@Override
	public int getFirstEntryPosY()
	{
		return 60;
	}
}
