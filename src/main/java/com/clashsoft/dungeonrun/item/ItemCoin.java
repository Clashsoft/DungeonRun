package com.clashsoft.dungeonrun.item;

public class ItemCoin extends Item
{
	private final int value;

	public ItemCoin(String id, int value)
	{
		super(id);
		this.value = value;
	}

	@Override
	public int getCoinValue()
	{
		return this.value;
	}
}
