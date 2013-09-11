package com.clashsoft.dungeonrun.container;

import com.clashsoft.dungeonrun.item.ItemStack;
import com.clashsoft.dungeonrun.nbt.INBTSaveable;
import com.clashsoft.dungeonrun.nbt.NBTBase;
import com.clashsoft.dungeonrun.nbt.NBTTagCompound;
import com.clashsoft.dungeonrun.nbt.NBTTagList;

public abstract class Inventory implements INBTSaveable
{
	public abstract void setStackInSlot(int slot, ItemStack stack);
	public abstract ItemStack getStackInSlot(int slot);
	public abstract int getFirstSlotWithItemStack(ItemStack stack);
	public abstract int getInventorySize();
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList slots = new NBTTagList("Slots");
		for (int i = 0; i < getInventorySize(); i++)
		{
			ItemStack stack = getStackInSlot(i);
			NBTTagCompound compound = new NBTTagCompound("Slot[" + i + "]");
			stack.writeToNBT(compound);
			compound.setInteger("SlotID", i);
			
			slots.addTagCompound(compound);
		}
		
		nbt.setTagList(slots);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList slots = nbt.getTagList("Slots");
		if (slots != null)
		{
			for (int i = 0; i < slots.tagCount(); i++)
			{
				NBTBase base = slots.tagAt(i);
				if (base instanceof NBTTagCompound)
				{
					NBTTagCompound compound = (NBTTagCompound)base;
					
					int slotID = compound.getInteger("SlotID");
					ItemStack stack = new ItemStack(null, 0, 0);
					stack.readFromNBT(compound);
					
					this.setStackInSlot(slotID, stack);
				}
			}
		}
	}
}
