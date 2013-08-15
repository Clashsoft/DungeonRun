package com.clashsoft.dungeonrun.item;

public class ItemStack
{
	public IStackable item;
	public int stackSize;
	public int metadata;
	
	public ItemStack(IStackable stackable)
	{
		this(stackable, 1);
	}
	
	public ItemStack(IStackable stackable, int stackSize)
	{
		this(stackable, stackSize, 0);
	}
	
	public ItemStack(IStackable stackable, int stackSize, int meta)
	{
		this.item = stackable;
		this.stackSize = stackSize;
		this.metadata = meta;
	}
	
	public int getMaxStackSize()
	{
		return item.getMaxStackSize(this);
	}
}