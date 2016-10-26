package com.clashsoft.dungeonrun.item;

import com.clashsoft.nbt.tags.collection.NBTTagCompound;
import com.clashsoft.nbt.util.INBTSaveable;

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
		nbt.setInteger("StackSize", this.stackSize);
		nbt.setInteger("DamageValue", this.metadata);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		int itemID = nbt.getInteger("ItemID");
		this.item = Item.itemsList[itemID];
		this.stackSize = nbt.getInteger("StackSize");
		this.metadata = nbt.getInteger("DamageValue");
	}
}
