package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.world.World;

public abstract class EntityDamagable extends Entity
{
	public float health = 20;
	
	public EntityDamagable(World world)
	{
		super(world);
	}
	
	public void damageEntity(DamageSource source, float damage)
	{
		if (canBeDamagedBy(source))
		{
			health -= damage;
			
			if (health <= 0)
				this.setDead();
		}
	}
	
	public abstract boolean canBeDamagedBy(DamageSource source);
}
