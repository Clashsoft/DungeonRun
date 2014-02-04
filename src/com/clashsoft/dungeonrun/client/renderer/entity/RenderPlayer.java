package com.clashsoft.dungeonrun.client.renderer.entity;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.EntityPlayer;

public class RenderPlayer extends Render<EntityPlayer>
{
	private SpriteSheet		textures;
	private Renderable[]	sprites	= new Renderable[16];
	
	public RenderPlayer(EntityPlayer player) throws SlickException
	{
		this.textures = new SpriteSheet(new Image(player.getTexture()), 12, 24);
		
		// 0 - 3 -> Top standing
		sprites[0] = this.textures.getSprite(0, 0);
		sprites[1] = this.textures.getSprite(0, 1);
		sprites[2] = this.textures.getSprite(0, 2);
		sprites[3] = this.textures.getSprite(0, 3);
		// 4 - 7 -> Top walking
		sprites[4] = new Animation(new Image[] { this.textures.getSprite(1, 0), this.textures.getSprite(2, 0) }, 200);
		sprites[5] = new Animation(new Image[] { this.textures.getSprite(1, 1), this.textures.getSprite(2, 1) }, 200);
		sprites[6] = new Animation(new Image[] { this.textures.getSprite(1, 2), this.textures.getSprite(2, 2) }, 200);
		sprites[7] = new Animation(new Image[] { this.textures.getSprite(1, 3), this.textures.getSprite(2, 3) }, 200);
		// 8 - 11 -> Side standing
		sprites[8] = this.textures.getSprite(3, 0);
		sprites[9] = this.textures.getSprite(3, 1);
		sprites[10] = this.textures.getSprite(3, 2);
		sprites[11] = this.textures.getSprite(3, 3);
		// 12 - 15 -> Side walking
		sprites[12] = new Animation(new Image[] { this.textures.getSprite(4, 0), this.textures.getSprite(5, 0) }, 200);
		sprites[13] = new Animation(new Image[] { this.textures.getSprite(4, 1), this.textures.getSprite(5, 1) }, 200);
		sprites[14] = new Animation(new Image[] { this.textures.getSprite(4, 2), this.textures.getSprite(5, 2) }, 200);
		sprites[15] = new Animation(new Image[] { this.textures.getSprite(4, 3), this.textures.getSprite(5, 3) }, 200);
	}
	
	@Override
	public void render(EntityPlayer player, double x, double y, int face)
	{
		float fx = (float) x;
		float fy = (float) y;
		
		int rot = player.rot;
		int index = 0;
		
		if (face == 1)
		{
			index = (player.isWalking ? 4 : 0) + rot;
		}
		else
		{
			index = (player.isWalking ? 12 : 8) + ((rot + face) % 4);
		}
		
		float width = DungeonRunClient.instance.fontRenderer.getStringWidth(player.username);
		
		GL11.glTranslatef(fx, fy, 0F);
		this.sprites[index].draw(-6F, -12F);
		DungeonRunClient.instance.fontRenderer.drawStringWithShadow(width / -2F, -8F, player.username);
		GL11.glTranslatef(-fx, -fy, 0F);
	}
}
