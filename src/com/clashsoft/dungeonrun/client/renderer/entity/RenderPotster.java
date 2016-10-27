package com.clashsoft.dungeonrun.client.renderer.entity;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.EntityPotster;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class RenderPotster extends Render<EntityPotster>
{
	public static final RenderPotster INSTANCE = new RenderPotster();

	private final Image idle;
	private final Image attacking;

	private RenderPotster()
	{
		this.idle = ResourceHelper.monsterSprites.getSprite(0, 1);
		this.attacking = ResourceHelper.monsterSprites.getSprite(1, 1);
	}

	@Override
	public void render(EntityPotster monster, Graphics g, double x, double y)
	{
		final float pitch = monster.pitch;
		final Image sprite = monster.isAwake() ? this.attacking : this.idle;

		final float offX = (float) x - sprite.getWidth() / 2;
		final float offY = (float) y - sprite.getHeight();

		sprite.draw(offX, offY);
	}
}
