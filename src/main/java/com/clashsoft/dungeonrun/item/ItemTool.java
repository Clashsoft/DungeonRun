package com.clashsoft.dungeonrun.item;

public class ItemTool extends Item
{
	private final float        damage;
	private       float        knockback = 0.4F;
	private final ToolMaterial material;
	private       boolean      dagger;

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

	public ItemTool withKnockback(float knockback)
	{
		this.knockback = knockback;
		return this;
	}

	@Override
	public int getSwingType(ItemStack stack)
	{
		return this.dagger ? STILL : ROTATE;
	}

	@Override
	public float getDamage(ItemStack stack)
	{
		return this.damage + this.material.getEntityDamage();
	}

	@Override
	public float getKnockback(ItemStack stack)
	{
		return this.knockback;
	}

	@Override
	public int getValue(ItemStack stack)
	{
		return (int) this.getDamage(stack) + this.material.getMaxUses();
	}
}
