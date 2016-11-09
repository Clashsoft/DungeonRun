package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.block.Blocks;
import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GuiMainMenu extends GuiListScreen
{
	public GuiMainMenu()
	{
	}
	
	@Override
	public void initGUI() throws SlickException
	{
		this.dr.soundEngine.stopAllMusics();
		this.dr.soundEngine.playMusic("main_menu", true);
	}

	@Override
	public void drawScreen(Graphics g, int width, int height) throws SlickException
	{
		super.drawScreen(g, width, height);

		final Image title = ResourceHelper.title;
		title.draw((width - title.getWidth()) / 2, 30);

	}

	@Override
	protected void drawEntry(String text, int index, float x, float y, float textWidth)
	{
		if (index == this.selection)
		{
			this.drawHighlight(x - 20, y - 6, textWidth + 40, 20);

			final Image torch = Blocks.torch.getTexture(0);
			torch.draw(x - torch.getWidth(), y - 4);
			torch.draw(x + textWidth, y - 4);
		}

		this.dr.fontRenderer.drawString(x, y, text, index == this.selection ? 0xFFFF00 : TITLE_COLOR, true);
	}
	
	@Override
	public String getTitle()
	{
		return "";
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
			return I18n.getString("game.singleplayer");
		case 1:
			return I18n.getString("options.title");
		case 2:
			return I18n.getString("game.quit");
		}
		return null;
	}

	@Override
	public void onEntryUsed(int i) throws SlickException
	{
		switch (i)
		{
		case 0:
			this.dr.displayGuiScreen(new GuiSelectWorld(this));
			break;
		case 1:
			this.dr.displayGuiScreen(new GuiOptions(this));
			break;
		case 2:
			this.dr.shutdown();
			break;
		}
	}
	
	@Override
	public int getYOffset()
	{
		return 100;
	}
}
