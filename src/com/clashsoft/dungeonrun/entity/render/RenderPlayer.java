package com.clashsoft.dungeonrun.entity.render;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.entity.EntityPlayer;

public class RenderPlayer<T extends EntityPlayer> extends RenderEntity<T>
{
	public EntityPlayer player;
	
	SpriteSheet textures;
	Image[] standing = new Image[4];
	Animation[] walking = new Animation[4];
	
	public RenderPlayer(T p) throws SlickException
	{
		super();
		this.player = p;
		textures = new SpriteSheet(new Image(player.getTexture()), 12, 24);
		for (int i = 0; i < 4; i++)
		{
			walking[i] = new Animation(new Image[] {textures.getSprite(1, i), textures.getSprite(2, i)}, 200, true);
			standing[i] = textures.getSprite(0, i);
		}
	}
	
	@Override
	public Rectangle render(EntityPlayer player, int w, int h) throws SlickException
	{
		int rot = player.rot;
		Renderable r;
		if (player.isWalking)
			r = walking[rot];
		else
			r = standing[rot];
		int x = (int)((w / 2F) - 6);
		int y = (int)((h / 2F) - 28);
		if (DungeonRun.instance.gameSettings.renderHitBoxes)
			DungeonRun.instance.theGameContainer.getGraphics().drawRect(x, y, 12, 24);
		r.draw(x, y);
		return new Rectangle(x, y, 12, 24);
	}
}
