package com.clashsoft.dungeonrun.client.renderer.entity;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.EntityMonster;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class RenderMonster extends Render<EntityMonster>
{
	public static final RenderMonster INSTANCE = new RenderMonster();

	private final Image[] sprites = new Image[4];

	private RenderMonster()
	{
		final Image standing = ResourceHelper.monsterSprites.getSprite(0, 0);
		this.sprites[0] = standing;
		this.sprites[1] = standing.getFlippedCopy(true, false);

		final Image attacking = ResourceHelper.monsterSprites.getSprite(1, 0);
		this.sprites[2] = attacking;
		this.sprites[3] = attacking.getFlippedCopy(true, false);
	}

	@Override
	public void render(EntityMonster monster, Graphics g, double x, double y)
	{
		final float pitch = monster.pitch;
		final int index = (monster.isAttacking() ? 2 : 0) + ((pitch >= 90 && pitch <= 270) ? 1 : 0);
		final Image sprite = this.sprites[index];

		final float offX = (float) x - sprite.getWidth() / 2;
		final float offY = (float) y - sprite.getHeight();

		sprite.draw(offX, offY);
	}
}
