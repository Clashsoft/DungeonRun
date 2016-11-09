package com.clashsoft.dungeonrun.item;

import com.clashsoft.nbt.tags.collection.NBTTagCompound;

public final class ItemStack
{
	public final Item item;
	public       int  metadata;
	public       int  size;

	public ItemStack(Item item)
	{
		this.item = item;
	}

	public ItemStack(Item item, int metadata, int size)
	{
		this.item = item;
		this.size = size;
		this.metadata = metadata;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("item", this.item.name);
		if (this.size != 1)
		{
			nbt.setInteger("size", this.size);
		}
		if (this.metadata != 0)
		{
			nbt.setInteger("metadata", this.metadata);
		}
	}

	public static ItemStack readFromNBT(NBTTagCompound nbt)
	{
		final Item item = Item.items.get(nbt.getString("item"));
		if (item == null)
		{
			return null;
		}

		final int size = nbt.hasTag("size") ? nbt.getInteger("size") : 1;
		final int metadata = nbt.getInteger("metadata"); // defaults to 0
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
