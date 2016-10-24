package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;

public abstract class EntityLiving extends EntityDamagable
{
	private int jumpCounter = 0;

	public EntityLiving(World world)
	{
		super(world);
	}

	public void jump()
	{
		if (this.airTime == 0)
		{
			this.jumpCounter = 1;
		}
	}

	@Override
	public void updateEntity()
	{
		if (this.jumpCounter == 1)
		{
			this.jumpCounter = 0;
			this.addVelocity(0, 0.6);
		}
		super.updateEntity();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setInteger("JumpCounter", this.jumpCounter);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		this.jumpCounter = nbt.getInteger("JumpCounter");
	}
}
