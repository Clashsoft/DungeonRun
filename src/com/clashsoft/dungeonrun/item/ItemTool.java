package com.clashsoft.dungeonrun.item;

public class ItemTool extends Item
{
	private final float        damage;
	private final ToolMaterial material;
	private boolean dagger;

	protected ItemTool(String id, float damage, ToolMaterial material)
	{
		super(id);
		this.damage = damage;
		this.material = material;
	}

	public ItemTool asDagger(boolean dagger)
	{
		this.dagger = dagger;
		return this;
	}

	@Override
	public int getSwingType(ItemStack stack)
	{
		return this.dagger ? STILL : ROTATE;
	}

	@Override
	public float getDamageVsEntity(ItemStack stack)
	{
		return this.damage + this.material.getEntityDamage();
	}
}
