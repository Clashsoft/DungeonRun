package com.clashsoft.dungeonrun.inventory;

import com.clashsoft.dungeonrun.item.ItemStack;
import com.clashsoft.nbt.NamedBinaryTag;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;
import com.clashsoft.nbt.tags.collection.NBTTagList;
import com.clashsoft.nbt.util.INBTSaveable;

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
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList slots = new NBTTagList("Slots");
		for (int i = 0; i < this.size(); i++)
		{
			final ItemStack stack = this.getStack(i);
			if (stack != null)
			{
				final NBTTagCompound compound = new NBTTagCompound("#" + i);
				stack.writeToNBT(compound);
				compound.setInteger("slot", i);

				slots.addTagCompound(compound);
			}
		}

		nbt.setTagList(slots);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList slots = nbt.getTagList("Slots");
		if (slots == null)
		{
			return;
		}

		for (int i = 0; i < slots.size(); i++)
		{
			final NamedBinaryTag base = slots.tagAt(i);
			if (!(base instanceof NBTTagCompound))
			{
				continue;
			}

			final NBTTagCompound compound = (NBTTagCompound) base;

			final int slotID = compound.getInteger("slot");
			final ItemStack stack = ItemStack.readFromNBT(compound);

			this.setStack(slotID, stack);
		}
	}
}
