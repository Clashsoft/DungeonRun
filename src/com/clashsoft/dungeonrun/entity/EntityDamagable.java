package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;

public abstract class EntityDamagable extends Entity
{
	public float	health	= 20;
	
	public EntityDamagable(World world)
	{
		super(world);
	}
	
	public void damageEntity(DamageSource source, float damage)
	{
		if (this.canBeDamagedBy(source))
		{
			this.health -= damage;
			
			if (this.health <= 0)
			{
				this.setDead();
			}
		}
	}
	
	public abstract boolean canBeDamagedBy(DamageSource source);
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		nbt.setFloat("Health", this.health);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		this.health = nbt.getFloat("Health");
	}
}
