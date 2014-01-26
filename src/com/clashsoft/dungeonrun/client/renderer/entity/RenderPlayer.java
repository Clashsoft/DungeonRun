package com.clashsoft.dungeonrun.client.renderer.entity;

import org.newdawn.slick.*;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.EntityPlayer;

public class RenderPlayer extends Render
{
	public final EntityPlayer	player;
	
	private SpriteSheet		textures;
	private Image[]			standing	= new Image[4];
	private Animation[]		walking		= new Animation[4];
	
	public RenderPlayer(EntityPlayer player) throws SlickException
	{
		this.player = player;
		
		this.textures = new SpriteSheet(new Image(player.getTexture()), 12, 24);
		for (int i = 0; i < 4; i++)
		{
			this.walking[i] = new Animation(new Image[] { this.textures.getSprite(1, i), this.textures.getSprite(2, i) }, 200, true);
			this.standing[i] = this.textures.getSprite(0, i);
		}
	}
	
	@Override
	public void render(Object renderable, int x, int y, float camX, float camY, int face) throws SlickException
	{
		this.render(this.player, this.width, this.height);
	}
	
	public void render(EntityPlayer player, int w, int h) throws SlickException
	{
		int rot = player.rot;
		Renderable r;
		if (player.isWalking)
		{
			r = this.walking[rot];
		}
		else
		{
			r = this.standing[rot];
		}
		int x = (int) (w / 2F - 6);
		int y = (int) (h / 2F - 28);
		if (DungeonRunClient.instance.gameSettings.renderHitBoxes)
		{
			DungeonRunClient.instance.getGraphics().drawRect(x, y, 12, 24);
		}
		r.draw(x, y);
	}
}
