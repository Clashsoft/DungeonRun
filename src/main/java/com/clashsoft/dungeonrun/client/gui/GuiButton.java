package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.util.DimensionHelper.Pos2;
import org.lwjgl.opengl.GL11;

public class GuiButton
{
	public int           buttonID;
	public Pos2<Integer> pos;
	public String        text;

	public boolean isInvisible = false;
	public boolean isLocked    = false;
	public boolean hover       = false;

	public GuiButton(int id, int x, int y, String text)
	{
		this.buttonID = id;
		this.pos = new Pos2<Integer>(x, y);
		this.text = text;
	}

	public void render()
	{
		if (!this.isInvisible)
		{
			if (this.isLocked)
			{
				GL11.glColor4f(0.8F, 0.8F, 0.8F, 1F);
			}
			int texture = this.isMouseHovering() ? 40 : 0;
			// DungeonRunClient.instance.renderEngine.drawTexture(ResourceHelper.buttons, this.pos.x, this.pos.y, 0, texture, 200, 40);
		}
	}

	public boolean isMouseHovering()
	{
		return DungeonRunClient.instance.currentGui.isMouseInRegion(this.pos.x, this.pos.y, 200, 40);
	}
}
