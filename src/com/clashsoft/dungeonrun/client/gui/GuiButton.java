package com.clashsoft.dungeonrun.client.gui;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.server.DungeonRunServer;
import com.clashsoft.dungeonrun.util.DimensionHelper.Pos2;
import com.clashsoft.dungeonrun.util.ResourceHelper;

public class GuiButton
{
	public int				buttonID;
	public Pos2<Integer>	pos;
	public String			text;
	
	public boolean			isInvisible	= false;
	public boolean			isLocked	= false;
	public boolean			hover		= false;
	
	public GuiButton(int id, int x, int y, String text)
	{
		this.buttonID = id;
		this.pos = new Pos2<Integer>(x, y);
		this.text = text;
	}
	
	public void render() throws SlickException
	{
		if (!isInvisible)
		{
			if (isLocked)
				GL11.glColor4f(0.8F, 0.8F, 0.8F, 1F);
			int texture = isMouseHovering() ? 40 : 0;
			DungeonRunServer.instance.renderEngine.drawTexture(ResourceHelper.buttons, pos.x, pos.y, 0, texture, 200, 40);
		}
	}
	
	public boolean isMouseHovering()
	{
		return DungeonRunServer.instance.currentGui.isMouseInRegion(pos.x, pos.y, 200, 40);
	}
}
