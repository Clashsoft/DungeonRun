package com.clashsoft.dungeonrun.item;

public interface IStackable
{
	public boolean isBlock();
	
	public int getID();
	
	public int getMaxStackSize(ItemStack stack);
}
