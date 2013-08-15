package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.world.World;

public abstract class EntityLiving extends Entity
{
	private float health = 20;
	private boolean isJumping = false;

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
}
