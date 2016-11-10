package com.clashsoft.dungeonrun.inventory;

import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.item.ItemStack;
import dyvil.tools.nbt.collection.NBTMap;

public class InventoryPlayer extends AbstractInventory
{
	private static final int SIZE = 8;

	public EntityPlayer player;
	private ItemStack[]	inventory	= new ItemStack[SIZE];

	public int handSlot;
	
	public InventoryPlayer(EntityPlayer ep)
	{
		this.player = ep;
	}
	
	@Override
	public ItemStack getStack(int i)
	{
		return this.inventory[i];
	}
	
	@Override
	public void setStack(int i, ItemStack stack)
	{
		this.inventory[i] = stack;
	}

	public ItemStack getHeldStack()
	{
		return this.inventory[this.handSlot];
	}

	@Override
	public int size()
	{
		return SIZE;
	}

	@Override
	public void writeToNBT(NBTMap nbt)
	{
		super.writeToNBT(nbt);

		nbt.setInteger("handSlot", this.handSlot);
	}

	@Override
	public void readFromNBT(NBTMap nbt)
	{
		super.readFromNBT(nbt);

		this.handSlot = nbt.getInteger("handSlot");
	}
}
