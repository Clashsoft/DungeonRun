package com.clashsoft.dungeonrun.item;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.nbt.INBTSaveable;
import com.clashsoft.dungeonrun.nbt.NBTTagCompound;

public class ItemStack implements INBTSaveable
{
	public IStackable	item;
	public int			stackSize;
	public int			metadata;
	
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
		return this.item.getMaxStackSize(this);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("ItemID", this.item.getID());
		nbt.setBoolean("IsBlock", this.item.isBlock());
		nbt.setInteger("StackSize", this.stackSize);
		nbt.setInteger("DamageValue", this.metadata);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		boolean isBlock = nbt.getBoolean("IsBlock");
		int itemID = nbt.getInteger("ItemID");
		this.item = isBlock ? Block.blocksList[itemID] : Item.itemsList[itemID];
		this.stackSize = nbt.getInteger("StackSize");
		this.metadata = nbt.getInteger("DamageValue");
	}
}