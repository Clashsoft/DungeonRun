package com.clashsoft.dungeonrun.client.renderer.entity;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import org.newdawn.slick.*;

public class RenderPlayer extends Render<EntityPlayer>
{
	private Renderable[] sprites = new Renderable[6];

	public RenderPlayer(EntityPlayer player) throws SlickException
	{
		final SpriteSheet textures = new SpriteSheet(new Image(player.getTexture()), 12, 24);

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

		final int index = (player.airTime > 0 ? 2 : player.isWalking ? 4 : 0) + (pitch >= 90 && pitch <= 270 ? 1 : 0);

		final float width = player.getWidth() * 16;
		final float height = player.getHeight() * 16;

		final float offX = (float) x - width / 2;
		final float offY = (float) y - height;

		this.sprites[index].draw(offX, offY);

		/*/ Toggle bounding box
		g.setBackground(Color.transparent);
		g.setColor(Color.white);
		g.drawRect(offX, offY, width, height);
		//*/
	}
}
