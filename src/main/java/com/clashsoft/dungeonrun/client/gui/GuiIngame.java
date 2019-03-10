package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.block.BlockLadder;
import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.engine.FontRenderer;
import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.client.engine.RenderBlocks;
import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.entity.EntityLiving;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.inventory.InventoryPlayer;
import com.clashsoft.dungeonrun.item.Items;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import com.clashsoft.dungeonrun.world.ForegroundBlock;
import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.dungeonrun.world.gen.HouseGenerator;
import com.clashsoft.dungeonrun.world.gen.TreeGenerator;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

public class GuiIngame extends GuiScreen
{
	private final EntityPlayer player;
	public        RenderBlocks renderBlocks;
	private       boolean      worldSaving = false;

	public GuiIngame(EntityPlayer player)
	{
		this.player = player;
	}

	@Override
	public void initGUI()
	{
		this.renderBlocks = this.dr.renderEngine.blockRenderer;

		this.dr.soundEngine.stopAllMusics();
		this.dr.soundEngine.playMusic("ingame_1", true);
	}

	@Override
	public void drawScreen(Graphics g, int width, int height)
	{
		this.renderBlocks.width = width;
		this.renderBlocks.height = height;

		this.renderLevel(g, width, height);

		final Image hotbar = ResourceHelper.hotbar;
		final int x = (width - hotbar.getWidth()) / 2;
		hotbar.draw(x, 2);

		final InventoryPlayer inventory = this.player.inventory;
		for (int i = 0; i < 8; i++)
		{
			GuiInventory.drawItem(x + i * 18 + 2, 4, inventory.getStack(i));
		}

		ResourceHelper.hotbarSelection.draw(x + inventory.handSlot * 18 - 1, 1);

		drawCoins(inventory.getCoins(), width - 10, 10);
		drawHealth(this.player, 10, 10);

		if (this.worldSaving)
		{
			String text = I18n.getString("world.saving");
			float w = this.dr.fontRenderer.getStringWidth(text);
			this.dr.fontRenderer.drawStringWithShadow(width - 20F - w, height - 20F, text, 0xFFFFFF);
		}
	}

	private static void drawHealth(EntityPlayer player, int x, int y)
	{
		final float health = player.getHealth();
		final float hearts = player.getMaxHealth() / 2;
		for (int i = 0; i < hearts; i++)
		{
			final int spriteX;
			if (health > i * 2 + 1)
			{
				spriteX = 0;
			}
			else if (health > i * 2)
			{
				spriteX = 1;
			}
			else
			{
				spriteX = 2;
			}
			ResourceHelper.iconsSprite.getSprite(spriteX, 0).draw(x + i * 9, y);
		}
	}

	private static final int[] COIN_VALUES = { Items.copper_coin.getCoinValue(), Items.silver_coin.getCoinValue(),
		Items.gold_coin.getCoinValue() };

	public static void drawCoins(int coins, int x, int y)
	{
		final FontRenderer fontRenderer = DungeonRunClient.instance.fontRenderer;
		x -= 10;

		for (int i = COIN_VALUES.length - 1; i >= 0; i--)
		{
			ResourceHelper.iconsSprite.getSprite(2 - i, 2).draw(x - i * 16, y);
			final int value = COIN_VALUES[i];
			final int amount = coins / value;
			coins -= amount * value;

			final String text = Integer.toString(amount);
			final float width = fontRenderer.getStringWidth(text);
			fontRenderer.drawStringWithShadow(x - i * 16 - width - 2, y, text);
		}
	}

	private void renderLevel(Graphics g, int width, int height)
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
					this.renderBlocks.renderBlock(world, x, y, camX, camY);
				}
			}
		}

		for (ForegroundBlock block : world.getForegroundBlocks(minX, minY, maxX, maxY))
		{
			this.renderBlocks.renderBlock(block.block, block.metadata, block.x, block.y, camX, camY);
		}

		for (Entity entity : world.getEntitys())
		{
			final Render render = entity.getRenderer();
			render.width = width;
			render.height = height;
			render.render(entity, g, entity.posX, entity.posY, camX, camY);
		}
	}

	@Override
	public void keyTyped(int key, char c)
	{
		final EntityPlayer player = this.player;
		final World world = player.worldObj;

		final int posX = player.getBlockX();
		final int posY = player.getBlockY();

		switch (key)
		{
		case Input.KEY_W:
			for (ForegroundBlock block : world.getForegroundBlocks(posX, posY, posX, posY + 1))
			{
				block.block.activate(world, block, this.player);
			}
			for (Entity entity : world.getEntities(posX - 1, posY - 1, posX + 1, posY + 1))
			{
				entity.onPlayerInteract(this.player);
			}

			break;
		case Input.KEY_H:
			HouseGenerator.generateHouse(world, world.random, posX, posY);
			break;
		case Input.KEY_K:
			HouseGenerator.generateStore(world, world.random, posX, posY);
			break;
		case Input.KEY_T:
			TreeGenerator.generateTree(world, world.random, posX, posY);
			break;
		case Input.KEY_L:
			BlockLadder.setLadder(world, posX, posY + 1);
			BlockLadder.setLadder(world, posX, posY + 2);
			break;
		case Input.KEY_ESCAPE:
			this.dr.pauseGame();
			this.dr.displayGuiScreen(new GuiPauseMenu());
			break;
		case Input.KEY_I:
			this.dr.displayGuiScreen(new GuiInventory(this.player));
			break;
		}

		if (key >= Input.KEY_1 && key <= Input.KEY_8)
		{
			this.player.inventory.handSlot = key - Input.KEY_1;
		}
	}

	@Override
	public void updateScreen()
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
		boolean attack = false;

		if (input.isKeyDown(Input.KEY_E))
		{
			hor += 1;
			this.player.attack();
			attack = true;
		}
		else if (input.isKeyDown(Input.KEY_D))
		{
			hor += 1;
		}

		if (input.isKeyDown(Input.KEY_Q))
		{
			hor -= 1;
			this.player.attack();
			attack = true;
		}
		else if (input.isKeyDown(Input.KEY_A))
		{
			hor -= 1;
		}

		if (hor == 0 || attack)
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
