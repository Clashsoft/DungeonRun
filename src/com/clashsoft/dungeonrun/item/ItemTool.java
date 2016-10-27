package com.clashsoft.dungeonrun.item;

public class ItemTool extends Item
{
	private final float damage;
	private final EnumToolMaterial material;

	protected ItemTool(String id, float damage, EnumToolMaterial material)
	{
		super(id);
		this.damage = damage;
		this.material = material;
	}

	@Override
	public float getDamageVsEntity(ItemStack stack)
	{
		return this.damage + this.material.getEntityDamage();
	}
}
