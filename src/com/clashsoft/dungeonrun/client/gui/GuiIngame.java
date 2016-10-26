package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.client.engine.RenderBlocks;
import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.entity.EntityLiving;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.world.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class GuiIngame extends GuiScreen
{
	public RenderBlocks renderBlocks;
	private boolean worldSaving = false;

	public GuiIngame(EntityPlayer player)
	{
		this.player = player;
	}

	@Override
	public void initGUI() throws SlickException
	{
		this.renderBlocks = this.dr.renderEngine.blockRenderer;

		this.dr.soundEngine.stopAllMusics();
		this.dr.soundEngine.playMusic("ingame_1", true);
	}

	@Override
	public void drawScreen(Graphics g, int width, int height) throws SlickException
	{
		this.renderBlocks.width = width;
		this.renderBlocks.height = height;

		if (this.player != null)
		{
			this.renderLevel(g, width, height);
		}

		if (this.worldSaving)
		{
			String text = I18n.getString("world.saving");
			float w = this.dr.fontRenderer.getStringWidth(text);
			this.dr.fontRenderer.drawStringWithShadow(width - 20F - w, height - 20F, text, 0xFFFFFF);
		}
	}

	private void renderLevel(Graphics g, int width, int height) throws SlickException
	{
		World world = this.player.worldObj;
		double camX = this.player.posX;
		double camY = this.player.posY;

		int posX = (int) camX;
		int posY = (int) camY;

		int blocksX = width / 32;
		int blocksY = height / 32;
		int minX = posX - blocksX - 2;
		int maxX = posX + blocksX + 2;
		int minY = posY - blocksY - 2;
		int maxY = posY + blocksY + 2;

		g.setColor(Color.cyan);
		g.fillRect(0, 0, width, height);

		if (minY >= 0 && maxY <= 256)
		{
			for (int y = minY; y <= maxY; y++)
			{
				for (int x = minX; x <= maxX; x++)
				{
					this.renderBlocks.renderBlock(world, x, y, posY, camX, camY);
				}
			}
		}
		for (Entity entity : this.player.worldObj.getEntitys())
		{
			final Render render = entity.getRenderer();
			render.width = width;
			render.height = height;
			render.render(entity, g, entity.posX, entity.posY, camX, camY);
		}
	}

	@Override
	public void keyTyped(int key, char c) throws SlickException
	{
	}

	@Override
	public void updateScreen() throws SlickException
	{
		final Input input = this.dr.getInput();

		int vert = this.checkVerticalMovement(input);
		int hor = this.checkHorizontalMovement(input);

		if (vert != 0 || hor != 0)
		{
			this.player.setPitch(getPitch(vert, hor));
		}

		if (input.isKeyDown(Input.KEY_SPACE))
		{
			this.player.jump();
		}
	}

	private static float getPitch(int vert, int hor)
	{
		// 135  90  45
		// 180       0
		// 225 270 315

		if (vert == 0)
		{
			return hor > 0 ? 0 : 180;
		}
		else if (vert > 0)
		{
			return hor < 0 ? 135 : hor == 0 ? 90 : 45;
		}
		else
		{
			return hor < 0 ? 225 : hor == 0 ? 270 : 315;
		}
	}

	private int checkHorizontalMovement(Input input)
	{
		int hor = 0;

		if (input.isKeyDown(Input.KEY_D))
		{
			hor += 1;
		}
		if (input.isKeyDown(Input.KEY_A))
		{
			hor -= 1;
		}

		if (hor == 0)
		{
			this.player.setMovement(EntityLiving.STANDING);
		}
		else
		{
			this.player.setMovement(input.isKeyDown(Input.KEY_LSHIFT) ? EntityLiving.SPRINTING : EntityLiving.WALKING);
		}

		return hor;
	}

	private int checkVerticalMovement(Input input)
	{
		int ver = 0;

		if (input.isKeyDown(Input.KEY_W))
		{
			ver += 1;
		}
		if (input.isKeyDown(Input.KEY_S))
		{
			ver -= 1;
		}

		this.player.setClimbing(ver != 0);
		return ver;
	}

	@Override
	public void setWorldSaving(boolean state)
	{
		this.worldSaving = state;
	}
}
