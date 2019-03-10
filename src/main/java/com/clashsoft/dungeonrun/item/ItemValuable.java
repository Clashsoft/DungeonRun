package com.clashsoft.dungeonrun.item;

public class ItemValuable extends Item
{
	private final int value;

	public ItemValuable(String id, int value)
	{
		super(id);
		this.value = value;
	}

	public int getValue()
	{
		return this.value;
	}

	@Override
	public int getValue(ItemStack stack)
	{
		return this.value;
	}
}
