package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;

import java.util.Random;

public abstract class EntityDamagable extends Entity
{
	public float health = 20;

	protected int hurtTime = 0;

	public EntityDamagable(World world)
	{
		super(world);
	}

	public float getHealth()
	{
		return this.health;
	}

	public int getHurtTime()
	{
		return this.hurtTime;
	}

	@Override
	public void updateEntity(Random random)
	{
		super.updateEntity(random);

		if (this.hurtTime > 0)
		{
			this.hurtTime--;
		}
	}

	public void damageEntity(DamageSource source, float damage)
	{
		if (!this.canBeDamagedBy(source))
		{
			return;
		}

		this.hurtTime = 10;

		this.health -= damage;

		if (this.health <= 0)
		{
			this.setDead();
		}
	}

	public abstract boolean canBeDamagedBy(DamageSource source);

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setFloat("health", this.health);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		this.health = nbt.getFloat("health");
	}
}
