package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.world.World;

import java.util.Random;

public abstract class EntityLiving extends EntityDamagable
{
	public static final int STANDING        = 0;
	public static final int WALKING         = 1;
	public static final int SPRINTING       = 2;
	public static final int ATTACK_DISTANCE = 4;

	protected boolean climbing;
	protected int     attackTime;

	protected int movement = STANDING;

	public EntityLiving(World world)
	{
		super(world);
	}

	public boolean isAttacking()
	{
		return this.attackTime > 5;
	}

	public int getAttackTime()
	{
		return this.attackTime;
	}

	public void jump()
	{
		if (this.airTime == 0 && this.velocityY == 0 && (this.collision & Entity.CLIMBABLE) == 0)
		{
			this.addVelocity(0, 1.25);
		}
	}

	public void attack(Entity entity, float damage)
	{
		if (this.attackTime > 0)
		{
			return;
		}

		if (!(entity instanceof EntityDamagable))
		{
			return;
		}

		((EntityDamagable) entity).damageEntity(DamageSource.PLAYER, damage);
		this.attackTime = 10;
		if (entity.isDead())
		{
			this.onKill(entity);
		}
	}

	public void onKill(Entity entity)
	{
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
		if (this.attackTime > 0)
		{
			this.attackTime--;
		}

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

		if (this.climbing && (this.collision & CLIMBABLE) != 0)
		{
			if (this.pitch >= 45 && this.pitch <= 135)
			{
				this.tryMove(0, 0.2);
			}
			else if (this.pitch >= 225 && this.pitch <= 315)
			{
				this.tryMove(0, -0.2);
			}
		}

		super.updateEntity(random);
	}
}
