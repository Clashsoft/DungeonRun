package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.container.Inventory;
import com.clashsoft.dungeonrun.item.ItemStack;

public class InventoryPlayer extends Inventory
{
	public EntityPlayer	player;
	private ItemStack[]	inventory	= new ItemStack[64];
	
	public InventoryPlayer(EntityPlayer ep)
	{
		player = ep;
	}
	
	public ItemStack getStackInSlot(int i)
	{
		return inventory[i];
	}
	
	public void setStackInSlot(int i, ItemStack stack)
	{
		inventory[i] = stack;
	}

	@Override
	public int getFirstSlotWithItemStack(ItemStack stack)
	{
		for (int i = 0; i < inventory.length; i++)
		{
			if (inventory[i].equals(stack))
				return i;
		}
		return -1;
	}

	@Override
	public int getInventorySize()
	{
		return inventory.length;
	}
}
