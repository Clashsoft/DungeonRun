package com.clashsoft.dungeonrun.client.renderer.entity;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import org.newdawn.slick.*;

public class RenderPlayer extends Render<EntityPlayer>
{
	private Renderable[] sprites = new Renderable[16];

	public RenderPlayer(EntityPlayer player) throws SlickException
	{
		SpriteSheet textures = new SpriteSheet(new Image(player.getTexture()), 12, 24);

		// 0 - 3 -> Top standing
		this.sprites[0] = textures.getSprite(0, 0);
		this.sprites[1] = textures.getSprite(0, 1);
		this.sprites[2] = textures.getSprite(0, 2);
		this.sprites[3] = textures.getSprite(0, 3);
		// 4 - 7 -> Top walking
		this.sprites[4] = new Animation(new Image[] { textures.getSprite(1, 0), textures.getSprite(2, 0) }, 200);
		this.sprites[5] = new Animation(new Image[] { textures.getSprite(1, 1), textures.getSprite(2, 1) }, 200);
		this.sprites[6] = new Animation(new Image[] { textures.getSprite(1, 2), textures.getSprite(2, 2) }, 200);
		this.sprites[7] = new Animation(new Image[] { textures.getSprite(1, 3), textures.getSprite(2, 3) }, 200);
		// 8 - 11 -> Side standing
		this.sprites[8] = textures.getSprite(3, 0);
		this.sprites[9] = textures.getSprite(3, 1);
		this.sprites[10] = textures.getSprite(3, 2);
		this.sprites[11] = textures.getSprite(3, 3);
		// 12 - 15 -> Side walking
		this.sprites[12] = new Animation(new Image[] { textures.getSprite(4, 0), textures.getSprite(5, 0) }, 200);
		this.sprites[13] = new Animation(new Image[] { textures.getSprite(4, 1), textures.getSprite(5, 1) }, 200);
		this.sprites[14] = new Animation(new Image[] { textures.getSprite(4, 2), textures.getSprite(5, 2) }, 200);
		this.sprites[15] = new Animation(new Image[] { textures.getSprite(4, 3), textures.getSprite(5, 3) }, 200);
	}

	@Override
	public void render(EntityPlayer player, Graphics g, double x, double y)
	{
		int index = (player.isWalking ? 12 : 8) + (player.rot % 4);

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
