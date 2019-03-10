package com.clashsoft.dungeonrun.client.gui;

import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.entity.npc.EntityClerk;
import com.clashsoft.dungeonrun.inventory.InventoryPlayer;
import com.clashsoft.dungeonrun.item.ItemStack;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class GuiTrades extends GuiListScreen
{
	private static final int WIDTH = 240;

	private final EntityClerk     clerk;
	private final InventoryPlayer inventory;

	public GuiTrades(EntityPlayer player, EntityClerk clerk)
	{
		this.inventory = player.inventory;
		this.clerk = clerk;
		this.drawBricks = false;
	}

	@Override
	public int getYOffset()
	{
		return 36;
	}

	@Override
	public void drawScreen(Graphics g, int width, int height)
	{
		g.setColor(Color.lightGray);
		g.fillRect((width - WIDTH) / 2, this.getYOffset() - 10, WIDTH, 210);

		super.drawScreen(g, width, height);
	}

	@Override
	public String getTitle()
	{
		return I18n.getString("clerk.title");
	}

	@Override
	public void onEntryUsed(int selection)
	{
		if (selection == this.clerk.getTradeCount())
		{
			this.dr.displayGuiScreen(this.dr.theIngameGui);
			return;
		}

		this.inventory.trade(this.clerk.getTrade(selection));
	}

	@Override
	public int entryCount()
	{
		return this.clerk.getTradeCount() + 1;
	}

	@Override
	public String getEntry(int i)
	{
		if (i == this.clerk.getTradeCount())
		{
			return I18n.getString("gui.close");
		}

		final EntityClerk.Trade trade = this.clerk.getTrade(i);
		final String name = trade.item.item.getLocalizedName(trade.item);

		return trade.amount > 0 ? " <- " : " -> ";
	}

	@Override
	protected void drawEntry(int i, int width, int height)
	{
		final String entry = this.getEntry(i);
		final float textWidth = this.dr.fontRenderer.getStringWidth(entry);
		final int y = i * 20 + this.getYOffset() + 20;

		if (i == this.selection)
		{
			final int boxWidth = (int) (textWidth + 28);
			this.drawHighlight((width - boxWidth) / 2, y - 6, boxWidth, 20);
		}

		final int color;

		if (i < this.clerk.getTradeCount())
		{
			final EntityClerk.Trade trade = this.clerk.getTrade(i);
			final ItemStack stack = trade.item;
			GuiInventory.drawItem((width - WIDTH) / 2 + 20, y - 4, stack);
			GuiIngame.drawCoins(Math.abs(trade.amount), (width + WIDTH) / 2 - 20, y);

			color = this.inventory.canTrade(trade) ? SELECTION_COLOR : ENTRY_COLOR;
		}
		else
		{
			color = SELECTION_COLOR;
		}

		this.dr.fontRenderer.drawString((int) ((width - textWidth) / 2), y, entry, color, true);
	}

	@Override
	public void keyTyped(int key, char c)
	{
		if (key == Input.KEY_ESCAPE)
		{
			this.dr.displayGuiScreen(this.dr.theIngameGui);
		}

		super.keyTyped(key, c);
	}
}
