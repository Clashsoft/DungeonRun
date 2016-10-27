package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.inventory.InventoryPlayer;
import com.clashsoft.dungeonrun.item.ItemStack;
import org.newdawn.slick.SlickException;

public class GuiInventory extends GuiListScreen
{
	private final InventoryPlayer inventory;

	public GuiInventory(EntityPlayer player)
	{
		this.inventory = player.inventory;
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
			return "<empty>";
		}

		final String name = stack.item.getLocalizedName(stack);

		if (stack.size != 1)
		{
			return name + " * " + stack.size;
		}
		return name;
	}

	@Override
	protected void drawEntry(int i, int width, int height)
	{
		final int x = width / 2 - 30;
		final int y = this.getYOffset() + i * 20;

		int color;
		if (i == this.selection)
		{
			color = i == this.inventory.handSlot ? 0xFFFF00 : SELECTION_COLOR;
		}
		else
		{
			color = i == this.inventory.handSlot ? 0xAAAA00 : ENTRY_COLOR;
		}

		this.dr.fontRenderer.drawString(x, y, this.getEntry(i), color, true);

		if (i >= this.inventory.size())
		{
			return;
		}

		final ItemStack stack = this.inventory.getStack(i);
		if (stack != null)
		{
			stack.item.getIcon(stack).draw(x - 20, y - 5);
		}
	}
}
