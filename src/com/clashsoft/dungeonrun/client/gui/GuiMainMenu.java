package com.clashsoft.dungeonrun.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.client.engine.I18n;
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
		player = new EntityPlayer(null);
		player.rot = 1;
	}
	
	@Override
	public void drawScreen(int width, int height) throws SlickException
	{
		int longestStringLength = 0;
		this.drawDefaultBackground(width, height);
		
		for (int i = 0; i < entrys.size(); i++)
		{
			String s = I18n.getString(entrys.get(i));
			
			int length = dr.fontRenderer.getStringWidth(s);
			int posX = (int) ((width - length) / 2F);
			int posY = (height - (entrys.size() * dr.fontRenderer.getStringHeigth(s))) / 2 + (i * 20);
			
			dr.fontRenderer.drawString(posX, posY, s, selection == i ? 0xFFFF00 : 0x00EFFF, true);
			
			if (length >= longestStringLength)
				longestStringLength = length;
			
			if (selection == i)
			{
				Image torch = ResourceHelper.iconsSprite.getSprite(3, 0);
				torch.draw(posX - torch.getWidth(), posY - 4);
				torch.draw(posX + length, posY - 4);
			}
		}
		
		int var1 = (int) (player.posX);
		int var2 = (height / 2) - 20;
		GL11.glTranslated(var1, var2, 0);
		GL11.glScalef(3F, 3F, 1F);
		player.getRenderer().render(player, 0, 0);
		GL11.glScalef(1F / 3F, 1F / 3F, 1F);
		GL11.glTranslated(-var1, -var2, 0);
	}
	
	@Override
	public void updateScreen() throws SlickException
	{
		super.updateScreen();
		
		if (player != null)
		{
			player.posX += 4;
			player.posX %= 700;
			player.isWalking = true;
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
			this.dr.displayGuiScreen(new GuiSelectWorld(this));
		else if (i == 1)
			this.dr.displayGuiScreen(new GuiOptions(this));
		else if (i == 2)
			this.dr.shutdown();
	}
	
	@Override
	public int getFirstEntryPosY()
	{
		return 60;
	}
}
