package com.clashsoft.dungeonrun.client.renderer.entity;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.EntityLiving;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.item.Item;
import com.clashsoft.dungeonrun.item.ItemStack;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;

public class RenderPlayer extends Render<EntityPlayer>
{
	public static final RenderPlayer INSTANCE = new RenderPlayer();

	private Image     standing;
	private Image     jumping;
	private Image     attacking;
	private Animation walking;

	private RenderPlayer()
	{
		final SpriteSheet textures = ResourceHelper.playerSprites;

		this.standing = textures.getSprite(0, 0);

		this.jumping = textures.getSprite(4, 0);
		this.attacking = textures.getSprite(3, 0);

		this.walking = new Animation(new Image[] { textures.getSprite(1, 0), textures.getSprite(2, 0) }, 200);
	}

	@Override
	public void render(EntityPlayer player, Graphics g, double x, double y)
	{
		final float pitch = player.pitch;

		final int attackTime = player.getAttackTime();

		final boolean flip = pitch >= 90 && pitch <= 270;
		final Renderable sprite = this.getSprite(player, attackTime);

		final float width = 12;
		final float height = 24;

		GL11.glPushMatrix();

		GL11.glTranslated(x, y, 0);

		if (flip)
		{
			GL11.glScalef(-1, 1, 1);
		}

		GL11.glTranslatef(-width / 2, -height, 0);

		sprite.draw(0, 0);

		this.drawHeldItem(player, attackTime);

		GL11.glPopMatrix();
	}

	private void drawHeldItem(EntityPlayer player, int attackTime)
	{
		if (attackTime <= 0)
		{
			return;
		}

		final ItemStack handStack = player.inventory.getHeldStack();
		if (handStack == null)
		{
			return;
		}

		if (handStack.item.getSwingType(handStack) == Item.STILL)
		{
			attackTime = 3;
		}

		final int centerX = 2;
		final int centerY = 14;

		GL11.glTranslatef(8 + centerX, -2 + centerY, 0);
		GL11.glRotatef(90F + -15F * attackTime, 0, 0, 1);
		GL11.glTranslatef(-centerX, -centerY, 0);

		handStack.item.getIcon(handStack).draw(0, 0);
	}

	private Renderable getSprite(EntityPlayer player, int attackTime)
	{
		if (attackTime > 0)
		{
			return this.attacking;
		}
		if (player.airTime > 0)
		{
			return this.jumping;
		}
		return player.getMovement() == EntityLiving.STANDING ? this.standing : this.walking;
	}
}
