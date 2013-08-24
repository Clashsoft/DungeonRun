package com.clashsoft.dungeonrun.item;

public class Item implements IStackable
{
	public static Item[]	itemsList		= new Item[4096];
	
	public static Item		swordWood		= new ItemSword(1, EnumToolMaterial.WOOD);
	
	public int				itemID;
	
	public int				maxUses			= 0;
	public int				maxStackSize	= 64;
	
	public Item(int id)
	{
		this.itemID = id;
		itemsList[id] = this;
	}
	
	@Override
	public int getID()
	{
		return itemID;
	}
	
	public Item setMaxUses(int maxUses)
	{
		this.maxUses = maxUses;
		return this;
	}
	
	public Item setMaxStackSize(int maxStackSize)
	{
		this.maxStackSize = maxStackSize;
		return this;
	}
	
	@Override
	public int getMaxStackSize(ItemStack stack)
	{
		return maxStackSize;
	}
	
	public float getDamageVsEntity(ItemStack stack)
	{
		return 0F;
	}
	
	public float getMaxUses(ItemStack stack)
	{
		return maxUses;
	}
}
