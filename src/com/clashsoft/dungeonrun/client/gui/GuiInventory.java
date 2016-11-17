package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.inventory.InventoryPlayer;
import com.clashsoft.dungeonrun.item.ItemStack;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class GuiInventory extends GuiListScreen
{
	private final InventoryPlayer inventory;

	public GuiInventory(EntityPlayer player)
	{
		this.inventory = player.inventory;
		this.drawBricks = false;
	}

	@Override
	public int getYOffset()
	{
		return 36;
	}

	@Override
	public void drawScreen(Graphics g, int width, int height) throws SlickException
	{
		final int windowWidth = 120;

		g.setColor(Color.lightGray);
		g.fillRect((width - windowWidth) / 2, this.getYOffset() - 10, windowWidth, 210);

		super.drawScreen(g, width, height);
	}

	@Override
	public String getTitle()
	{
		return I18n.getString("inventory.title");
	}

	@Override
	public void onEntryUsed(int selection) throws SlickException
	{
		if (selection == this.inventory.size())
		{
			this.dr.displayGuiScreen(this.dr.theIngameGui);
			return;
		}

		this.inventory.handSlot = selection;
	}

	@Override
	public int entryCount()
	{
		return this.inventory.size() + 1;
	}

	@Override
	public String getEntry(int i)
	{
		if (i == this.inventory.size())
		{
			return I18n.getString("gui.close");
		}

		ItemStack stack = this.inventory.getStack(i);
		if (stack == null)
		{
			return "";
		}

		return stack.item.getLocalizedName(stack);
	}

	@Override
	protected void drawEntry(int i, int width, int height)
	{
		final int x = width / 2 - 30;
		final int y = i * 20 + this.getYOffset() + 20;
		final String entry = this.getEntry(i);
		final int color = i == this.inventory.handSlot ? 0xFFFF00 : SELECTION_COLOR;

		if (i == this.selection)
		{
			final float textWidth = this.dr.fontRenderer.getStringWidth(entry);
			this.drawHighlight(x - 24, y - 6, textWidth + 24 + 4, 20);
		}

		this.dr.fontRenderer.drawString(x, y, entry, color, true);

		if (i >= this.inventory.size())
		{
			return;
		}

		final ItemStack stack = this.inventory.getStack(i);
		drawItem(x - 20, y - 4, stack, this.dr);
	}

	public static void drawItem(int x, int y, ItemStack stack, DungeonRunClient dr)
	{
		if (stack == null)
		{
			return;
		}

		stack.item.getIcon(stack).draw(x, y);

		if (stack.size != 1)
		{
			dr.smallFontRenderer.drawString(x, y, Integer.toString(stack.size), 0xFFFFFF, true);
		}
	}

	@Override
	public void keyTyped(int key, char c) throws SlickException
	{
		if (key == Input.KEY_ESCAPE)
		{
			this.dr.displayGuiScreen(this.dr.theIngameGui);
		}

		super.keyTyped(key, c);
	}
}
