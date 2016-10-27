package com.clashsoft.dungeonrun.client.renderer.entity;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.EntityLiving;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.item.ItemStack;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;

public class RenderPlayer extends Render<EntityPlayer>
{
	public static final RenderPlayer INSTANCE = new RenderPlayer();

	private Renderable[] sprites = new Renderable[6];

	private RenderPlayer()
	{
		final SpriteSheet textures = ResourceHelper.playerSprites;

		final Image standing = textures.getSprite(0, 0);
		this.sprites[0] = standing;
		this.sprites[1] = standing.getFlippedCopy(true, false);

		final Image jumping = textures.getSprite(4, 0);
		this.sprites[2] = jumping;
		this.sprites[3] = jumping.getFlippedCopy(true, false);

		final Image walk1 = textures.getSprite(1, 0);
		final Image walk2 = textures.getSprite(2, 0);
		this.sprites[4] = new Animation(new Image[] { walk1, walk2 }, 200);
		this.sprites[5] = new Animation(new Image[] { walk1.getFlippedCopy(true, false),
			walk2.getFlippedCopy(true, false) }, 200);
	}

	@Override
	public void render(EntityPlayer player, Graphics g, double x, double y)
	{
		final float pitch = player.pitch;

		final boolean flip = pitch >= 90 && pitch <= 270;
		final int index = (player.airTime > 0 ? 2 : player.getMovement() != EntityLiving.STANDING ? 4 : 0) + (flip ? 1 : 0);

		final float width = player.getWidth() * 16;
		final float height = player.getHeight() * 16;
		final float offX = (float) x - width / 2;
		final float offY = (float) y - height;

		this.sprites[index].draw(offX, offY);

		ItemStack handStack = player.inventory.getHeldStack();
		if (handStack == null)
		{
			return;
		}

		GL11.glPushMatrix();

		GL11.glTranslatef((float) x - 4, (float) y - 32, 0);
		GL11.glScalef(0.5f, 0.5f, 1);

		handStack.item.getIcon(handStack).draw(0, 0);

		GL11.glPopMatrix();
	}
}
