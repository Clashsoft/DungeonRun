package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.util.ResourceHelper;
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
	protected void drawEntry(String text, boolean selected, float x, float y, float width)
	{
		if (selected)
		{
			Image torch = ResourceHelper.iconsSprite.getSprite(3, 0);
			torch.draw(x - torch.getWidth(), y - 4);
			torch.draw(x + width, y - 4);
		}

		this.dr.fontRenderer.drawString(x, y, text, selected ? 0xFFFF00 : 0x00EFFF, true);
	}

	@Override
	public void updateScreen() throws SlickException
	{
		super.updateScreen();
		
		if (this.player != null)
		{
			this.player.posX += 4;
			this.player.posX %= 700;
			this.player.isWalking = true;
		}
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
		return 60;
	}
}
