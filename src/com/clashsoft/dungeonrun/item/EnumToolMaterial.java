package com.clashsoft.dungeonrun.item;

public enum EnumToolMaterial
{
	WOOD("Wood", 32, 1F, 2F), FLINT("Flint", 48, 1.25F, 2.5F), STONE("Stone", 64, 1.5F, 3F), IRON("Iron", 128, 2.25F, 5F), STEEL("Steel", 196, 2.75F, 6F), GOLD("Gold", 96, 3F, 5.5F), DIAMOND("Diamond", 512, 5F, 7F);
	
	private String	name;
	private int		maxUses;
	private float	efficiency;
	private float	entityDamage;
	
	private EnumToolMaterial(String name, int maxUses, float efficiency, float damage)
	{
		this.name = name;
		this.maxUses = maxUses;
		this.efficiency = efficiency;
		this.entityDamage = damage;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getMaxUses()
	{
		return maxUses;
	}
	
	public float getEfficiency()
	{
		return efficiency;
	}
	
	public float getEntityDamage()
	{
		return entityDamage;
	}
}
