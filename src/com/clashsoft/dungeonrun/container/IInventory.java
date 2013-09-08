package com.clashsoft.dungeonrun.container;

import com.clashsoft.dungeonrun.item.ItemStack;

public interface IInventory
{
	public void setStackInSlot(int slot, ItemStack stack);
	public ItemStack getStackInSlot(int slot);
	public int getFirstSlotWithItemStack(ItemStack stack);
}
