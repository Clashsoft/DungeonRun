package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.world.World;

import java.util.Random;

public abstract class EntityLiving extends EntityDamagable
{
	public static final int STANDING = 0;
	public static final int WALKING = 1;
	public static final int SPRINTING = 2;

	protected boolean jumping;
	protected boolean climbing;

	protected int movement = STANDING;

	public EntityLiving(World world)
	{
		super(world);
	}

	public void jump()
	{
		if (this.airTime == 0 && !this.climbing)
		{
			this.jumping = true;
		}
	}

	public boolean isJumping()
	{
		return this.jumping;
	}

	public int getMovement()
	{
		return this.movement;
	}

	public void setMovement(int type)
	{
		this.movement = type;
	}

	public boolean isClimbing()
	{
		return this.climbing;
	}

	public void setClimbing(boolean climbing)
	{
		this.climbing = climbing;
	}

	@Override
	public void updateEntity(Random random)
	{
		switch (this.movement)
		{
		case WALKING:
		case SPRINTING:
			double distance = this.airTime > 0 ? 0.4 : this.movement == SPRINTING ? 0.3 : 0.2;

			if (this.pitch >= 90 && this.pitch <= 270)
			{
				distance = -distance;
			}

			this.tryMove(distance, 0);
			break;
		}

		if (this.climbing && (this.checkCollide() & CLIMBABLE) != 0)
		{
			this.jumping = false;

			if (this.pitch >= 45 && this.pitch <= 135)
			{
				this.tryMove(0, 0.2);
			}
			else if (this.pitch >= 225 && this.pitch <= 315)
			{
				this.tryMove(0, -0.2);
			}
		}

		if (this.jumping)
		{
			this.jumping = false;
			this.addVelocity(0, 0.6);
		}

		super.updateEntity(random);
	}
}
