package com.clashsoft.dungeonrun.inventory;

import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.item.ItemStack;

public class InventoryPlayer extends AbstractInventory
{
	public EntityPlayer	player;
	private ItemStack[]	inventory	= new ItemStack[64];
	
	public InventoryPlayer(EntityPlayer ep)
	{
		this.player = ep;
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.inventory[i];
	}
	
	@Override
	public void setStackInSlot(int i, ItemStack stack)
	{
		this.inventory[i] = stack;
	}
	
	@Override
	public int getFirstSlotWithItemStack(ItemStack stack)
	{
		for (int i = 0; i < this.inventory.length; i++)
		{
			if (this.inventory[i].equals(stack))
			{
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int getInventorySize()
	{
		return this.inventory.length;
	}
}
