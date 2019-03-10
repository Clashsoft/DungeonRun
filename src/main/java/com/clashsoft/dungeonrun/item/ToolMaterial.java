package com.clashsoft.dungeonrun.item;

public enum ToolMaterial
{
	WOOD("Wood", 32, 1F, 2F), IRON("Iron", 128, 2.25F, 5F);

	private String name;
	private int    maxUses;
	private float  efficiency;
	private float  entityDamage;

	ToolMaterial(String name, int maxUses, float efficiency, float damage)
	{
		this.name = name;
		this.maxUses = maxUses;
		this.efficiency = efficiency;
		this.entityDamage = damage;
	}

	public String getName()
	{
		return this.name;
	}

	public int getMaxUses()
	{
		return this.maxUses;
	}

	public float getEfficiency()
	{
		return this.efficiency;
	}

	public float getEntityDamage()
	{
		return this.entityDamage;
	}
}
