package com.clashsoft.dungeonrun.inventory;

import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.entity.npc.EntityClerk;
import com.clashsoft.dungeonrun.item.ItemCoin;
import com.clashsoft.dungeonrun.item.ItemStack;
import dyvil.tools.nbt.collection.NBTMap;

public class InventoryPlayer extends AbstractInventory
{
	private static final int SIZE = 8;

	public EntityPlayer player;
	private ItemStack[] inventory = new ItemStack[SIZE];

	public  int handSlot;
	private int coins;

	public InventoryPlayer(EntityPlayer ep)
	{
		this.player = ep;
	}

	public int getCoins()
	{
		return this.coins;
	}

	public boolean canTrade(EntityClerk.Trade trade)
	{
		if (trade.amount > 0)
		{
			return this.coins - trade.amount >= 0 && this.canAdd(trade.item);
		}
		return this.canRemove(trade.item);
	}

	public void trade(EntityClerk.Trade trade)
	{
		if (!this.canTrade(trade))
		{
			return;
		}

		if (trade.amount > 0)
		{
			this.add(trade.item);
			this.coins -= trade.amount;
			return;
		}

		this.remove(trade.item);
		this.coins += -trade.amount;
	}

	@Override
	public boolean canAdd(ItemStack stack)
	{
		return stack.item instanceof ItemCoin || super.canAdd(stack);
	}

	@Override
	public void add(ItemStack stack)
	{
		if (this.addCoin(stack))
		{
			return;
		}
		super.add(stack);
	}

	private boolean addCoin(ItemStack stack)
	{
		int coinValue = stack.item.getCoinValue();
		if (coinValue > 0)
		{
			this.coins += coinValue * stack.size;
			return true;
		}
		return false;
	}

	@Override
	public ItemStack getStack(int i)
	{
		return this.inventory[i];
	}

	@Override
	public void setStack(int i, ItemStack stack)
	{
		if (this.addCoin(stack))
		{
			return;
		}
		this.inventory[i] = stack;
	}

	public ItemStack getHeldStack()
	{
		return this.inventory[this.handSlot];
	}

	@Override
	public int size()
	{
		return SIZE;
	}

	@Override
	public void writeToNBT(NBTMap nbt)
	{
		super.writeToNBT(nbt);

		nbt.setInteger("handSlot", this.handSlot);
		nbt.setInteger("coins", this.coins);
	}

	@Override
	public void readFromNBT(NBTMap nbt)
	{
		super.readFromNBT(nbt);

		this.handSlot = nbt.getInteger("handSlot");
		this.coins = nbt.getInteger("coins");
	}
}
