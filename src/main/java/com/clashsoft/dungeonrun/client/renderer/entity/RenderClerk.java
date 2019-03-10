package com.clashsoft.dungeonrun.client.renderer.entity;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.npc.EntityClerk;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class RenderClerk extends Render<EntityClerk>
{
	public static final RenderClerk INSTANCE = new RenderClerk();

	private final Image idle;

	private RenderClerk()
	{
		this.idle = ResourceHelper.npcSprites.getSprite(0, 1);
	}

	@Override
	public void render(EntityClerk clerk, Graphics g, double x, double y)
	{
		final Image sprite = this.idle;

		final float offX = (float) x - sprite.getWidth() / 2;
		final float offY = (float) y - sprite.getHeight();

		sprite.draw(offX, offY);
	}
}
