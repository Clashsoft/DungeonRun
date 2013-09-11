package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.nbt.NBTTagCompound;
import com.clashsoft.dungeonrun.world.World;

public abstract class EntityLiving extends EntityDamagable
{
	private boolean	isJumping	= false;
	
	public EntityLiving(World world)
	{
		super(world);
	}
	
	public void jump()
	{
		if (!isJumping)
		{
			isJumping = true;
			this.addVelocity(0, 0.6F, 0);
		}
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (isCollidedVertically())
			isJumping = false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		nbt.setBoolean("IsJumping", this.isJumping);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		this.isJumping = nbt.getBoolean("IsJumping");
	}
}
