package com.clashsoft.dungeonrun.container;

import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.item.ItemStack;

public class InventoryPlayer extends AbstractInventory
{
	public EntityPlayer	player;
	private ItemStack[]	inventory	= new ItemStack[64];
	
	public InventoryPlayer(EntityPlayer ep)
	{
		player = ep;
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return inventory[i];
	}
	
	@Override
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
