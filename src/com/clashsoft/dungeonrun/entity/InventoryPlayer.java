package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.item.ItemStack;

public class InventoryPlayer
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
}
