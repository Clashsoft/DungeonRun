package com.clashsoft.dungeonrun.inventory;

import com.clashsoft.dungeonrun.item.ItemStack;
import com.clashsoft.nbt.INBTSaveable;
import com.clashsoft.nbt.NBTBase;
import com.clashsoft.nbt.NBTTagCompound;
import com.clashsoft.nbt.NBTTagList;

public abstract class AbstractInventory implements INBTSaveable
{
	public abstract void setStackInSlot(int slot, ItemStack stack);
	
	public abstract ItemStack getStackInSlot(int slot);
	
	public abstract int getFirstSlotWithItemStack(ItemStack stack);
	
	public abstract int getInventorySize();
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList slots = new NBTTagList("Slots");
		for (int i = 0; i < this.getInventorySize(); i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if (stack != null)
			{
				NBTTagCompound compound = new NBTTagCompound("Slot#" + i);
				stack.writeToNBT(compound);
				compound.setInteger("SlotID", i);
				
				slots.addTagCompound(compound);
			}
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
					NBTTagCompound compound = (NBTTagCompound) base;
					
					int slotID = compound.getInteger("SlotID");
					ItemStack stack = new ItemStack(null, 0, 0);
					stack.readFromNBT(compound);
					
					this.setStackInSlot(slotID, stack);
				}
			}
		}
	}
}
