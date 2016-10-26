package com.clashsoft.dungeonrun.item;

public interface IStackable
{
	int getID();
	
	int getMaxStackSize(ItemStack stack);
}
