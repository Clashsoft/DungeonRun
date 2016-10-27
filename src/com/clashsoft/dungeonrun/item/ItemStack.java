package com.clashsoft.dungeonrun.item;

import com.clashsoft.nbt.tags.collection.NBTTagCompound;

public final class ItemStack
{
	public final Item item;
	public       int  metadata;
	public       int  size;

	public ItemStack(Item item, int metadata, int size)
	{
		this.item = item;
		this.size = size;
		this.metadata = metadata;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{

	}

	public static ItemStack readFromNBT(NBTTagCompound nbt)
	{
		Item item = Item.items.get(nbt.getString("item"));
		if (item == null)
		{
			return null;
		}

		int metadata = nbt.getInteger("metadata");
		int size = nbt.getInteger("size");
		return new ItemStack(item, metadata, size);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!(obj instanceof ItemStack))
		{
			return false;
		}

		final ItemStack that = (ItemStack) obj;
		return this.metadata == that.metadata && this.size == that.size && this.item == that.item;
	}

	@Override
	public int hashCode()
	{
		int result = this.item.hashCode();
		result = 31 * result + this.metadata;
		result = 31 * result + this.size;
		return result;
	}
}
