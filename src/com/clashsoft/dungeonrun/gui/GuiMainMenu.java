package com.clashsoft.dungeonrun.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.util.ResourceRegistry;

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
	public void drawScreen(int par1, int par2) throws SlickException
	{
		int longestStringLength = 0;
		for (int i = 0; i < par1 / 16F; i++)
		{
			for (int j = 0; j < par2 / 16F; j++)
			{
				Block.brick.getBlockTextureFromSideAndMetadata(0, 0).draw(i * 16, j * 16);
			}
		}
		for (int i = 0; i < entrys.size(); i++)
		{
			String s = entrys.get(i);
			int length = DungeonRun.getGraphics().getFont().getWidth(s);
			int posY = (par2 - (entrys.size() * DungeonRun.getGraphics().getFont().getHeight(s))) / 2 + (i * 20);
			DungeonRun.getGraphics().drawString(s, (int) ((par1 - length) / 2F), posY);
			if (length >= longestStringLength)
				longestStringLength = length;
			if (selection == i)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				ResourceRegistry.iconsSprite.getSprite(3, 0).draw((par1 - longestStringLength) / 2 - 18 - 4, posY);
				ResourceRegistry.iconsSprite.getSprite(3, 0).draw((par1 + longestStringLength) / 2 + 4, posY);
			}
		}
		
		int var1 = (int) (player.posX);
		int var2 = (par2 / 2) - 20;
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
		
		player.posX += 4;
		player.posX %= 700;
		player.isWalking = true;
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
