package com.clashsoft.dungeonrun.inventory;

import com.clashsoft.dungeonrun.item.ItemStack;
import dyvil.tools.nbt.NamedBinaryTag;
import dyvil.tools.nbt.collection.NBTMap;
import dyvil.tools.nbt.collection.NBTList;
import dyvil.tools.nbt.util.INBTSaveable;

public abstract class AbstractInventory implements INBTSaveable
{
	public abstract void setStack(int slot, ItemStack stack);

	public abstract ItemStack getStack(int slot);

	public int indexOf(ItemStack stack)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (this.getStack(i).equals(stack))
			{
				return i;
			}
		}
		return -1;
	}

	public boolean add(ItemStack stack)
	{
		int empty = -1;

		for (int i = 0; i < this.size(); i++)
		{
			final ItemStack slot = this.getStack(i);
			if (slot == null)
			{
				if (empty < 0)
				{
					empty = i;
				}
				continue;
			}

			if (slot.merge(stack))
			{
				return true;
			}
		}

		if (empty < 0)
		{
			return false;
		}

		this.setStack(empty, stack);
		return true;
	}

	public abstract int size();

	@Override
	public void writeToNBT(NBTMap nbt)
	{
		final int size = this.size();
		final NBTList slots = new NBTList(size);

		for (int slotIndex = 0; slotIndex < size; slotIndex++)
		{
			final ItemStack stack = this.getStack(slotIndex);
			if (stack != null)
			{
				final NBTMap compound = new NBTMap();

				compound.setInteger("slot", slotIndex);
				stack.writeToNBT(compound);
				slots.addTag(compound);
			}
		}

		nbt.setTag("slots", slots);
	}

	@Override
	public void readFromNBT(NBTMap nbt)
	{
		final NBTList slots = nbt.getTagList("slots");
		if (slots == null)
		{
			return;
		}

		for (int i = 0; i < slots.size(); i++)
		{
			final NamedBinaryTag base = slots.getTag(i);
			if (!(base instanceof NBTMap))
			{
				continue;
			}

			final NBTMap compound = (NBTMap) base;

			final int slotID = compound.getInteger("slot");
			final ItemStack stack = ItemStack.readFromNBT(compound);

			this.setStack(slotID, stack);
		}
	}
}
