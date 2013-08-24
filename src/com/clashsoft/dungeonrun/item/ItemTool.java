package com.clashsoft.dungeonrun.item;

public class ItemTool extends Item
{
	protected ItemTool(int id, float damage, EnumToolMaterial material)
	{
		super(id);
		this.setMaxStackSize(1);
	}
	
}
