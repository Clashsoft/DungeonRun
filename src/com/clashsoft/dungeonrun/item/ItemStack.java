package com.clashsoft.dungeonrun.item;

import dyvil.tools.nbt.collection.NBTMap;

public final class ItemStack
{
	public final Item item;
	public       int  metadata;
	public       int  size;

	public ItemStack(Item item)
	{
		this.item = item;
		this.size = 1;
	}

	public ItemStack(Item item, int metadata, int size)
	{
		this.item = item;
		this.size = size;
		this.metadata = metadata;
	}

	public boolean merge(ItemStack other)
	{
		if (!this.itemEquals(other))
		{
			return false;
		}

		final int maxSize = this.item.getMaxStackSize(this);

		this.size += other.size;
		if (this.size > maxSize)
		{
			other.size = this.size - maxSize;
			this.size = maxSize;

			return false;
		}

		other.size = 0;
		return true;
	}

	public void writeToNBT(NBTMap nbt)
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

	public static ItemStack readFromNBT(NBTMap nbt)
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
		return this.equals(that);
	}

	public boolean equals(ItemStack that)
	{
		return this.size == that.size && this.itemEquals(that);
	}

	public boolean itemEquals(ItemStack that)
	{
		return this.item == that.item && this.metadata == that.metadata;
	}

	@Override
	public int hashCode()
	{
		int result = this.item.name.hashCode();
		result = 31 * result + this.metadata;
		result = 31 * result + this.size;
		return result;
	}
}
