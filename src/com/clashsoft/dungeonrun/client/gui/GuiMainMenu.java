package com.clashsoft.dungeonrun.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.util.ResourceHelper;

public class GuiMainMenu extends GuiListScreen
{
	private EntityPlayer	player;
	
	@Override
	public void initGui() throws SlickException
	{
		super.initGui();
		this.dr.soundEngine.playMusic("resources/audio/music/music1.wav", true);
		this.player = new EntityPlayer(null);
		this.player.rot = 1;
	}
	
	@Override
	public void drawScreen(int width, int height) throws SlickException
	{
		this.drawDefaultBackground(width, height);
		
		for (int i = 0; i < this.entrys.size(); i++)
		{
			String text = this.getEntry(i);
			boolean selected = this.selection == i;
			int i1 = this.dr.fontRenderer.getStringWidth(text);
			int x = (width - i1) / 2;
			int y = i * 20 + 120;
			
			if (this.isMouseInRegion(x - 10, y, i1, 10))
			{
				this.selection = i;
				selected = true;
			}
			
			if (selected)
			{
				Image torch = ResourceHelper.iconsSprite.getSprite(3, 0);
				torch.draw(x - torch.getWidth(), y - 4);
				torch.draw(x + i1, y - 4);
			}
			
			this.dr.fontRenderer.drawString(x, y, text, selected ? 0xFFFF00 : 0x00EFFF, true);
		}
		
		GL11.glScalef(3F, 3F, 1F);
		this.player.getRenderer().render(this.player, player.posX, height / 2D - 20D, 2);
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
		if (i == 0)
		{
			this.dr.displayGuiScreen(new GuiSelectWorld(this));
		}
		else if (i == 1)
		{
			this.dr.displayGuiScreen(new GuiOptions(this));
		}
		else if (i == 2)
		{
			this.dr.shutdown();
		}
	}
	
	@Override
	public int getYOffset()
	{
		return 60;
	}
}
