package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.List;

public class GuiMainMenu extends GuiListScreen
{
	private EntityPlayer	player;
	
	public GuiMainMenu()
	{
	}
	
	@Override
	public void initGUI() throws SlickException
	{
		this.dr.soundEngine.stopAllMusics();
		this.dr.soundEngine.playMusic("main_menu", true);
		
		this.player = new EntityPlayer(null);
	}
	
	@Override
	public void drawScreen(Graphics g, int width, int height) throws SlickException
	{
		this.drawDefaultBackground(width, height);
		
		for (int i = 0; i < this.entrys.size(); i++)
		{
			String text = this.getEntry(i);
			boolean selected = this.selection == i;
			float textWidth = this.dr.fontRenderer.getStringWidth(text);
			float x = (width - textWidth) / 2;
			float y = i * 20 + 120;
			
			if (this.isMouseInRegion(x - 10, y, textWidth, 10))
			{
				this.selection = i;
				selected = true;
			}
			
			if (selected)
			{
				Image torch = ResourceHelper.iconsSprite.getSprite(3, 0);
				torch.draw(x - torch.getWidth(), y - 4);
				torch.draw(x + textWidth, y - 4);
			}
			
			this.dr.fontRenderer.drawString(x, y, text, selected ? 0xFFFF00 : 0x00EFFF, true);
		}
		
		GL11.glScalef(3F, 3F, 1F);
		this.player.getRenderer().render(this.player, g, this.player.posX, height / 2D - 20D);
		GL11.glScalef(1F / 3F, 1F / 3F, 1F);
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
		return "mainmenu.title";
	}
	
	@Override
	public void addEntrys(List<String> s)
	{
		s.add("game.singleplayer");
		s.add("options.title");
		s.add("game.quit");
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
