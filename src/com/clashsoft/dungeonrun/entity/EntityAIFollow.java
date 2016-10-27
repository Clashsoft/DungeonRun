package com.clashsoft.dungeonrun.entity;

import java.util.Random;

public class EntityAIFollow implements EntityAI<EntityLiving>
{
	public  EntityPlayer target;
	private double       targetX;

	private final double squareDistance;
	private final boolean walkRandom;

	public EntityAIFollow(double maxDistance, boolean walkRandom)
	{
		this.squareDistance = maxDistance * maxDistance;
		this.walkRandom = walkRandom;
	}

	private double squareDistance(Entity e1, Entity e2)
	{
		double dx = e2.posX - e1.posX;
		double dy = e2.posY - e1.posY;
		return dx * dx + dy * dy;
	}

	@Override
	public void update(EntityLiving entity, Random random)
	{
		if (this.target != null)
		{
			if (this.squareDistance(entity, this.target) > this.squareDistance)
			{
				this.target = null;
			}
			else
			{
				this.targetX = this.target.posX;
			}
		}
		else
		{
			for (EntityPlayer player : entity.worldObj.getPlayers())
			{
				if (this.squareDistance(entity, player) <= this.squareDistance)
				{
					this.target = player;
					break;
				}
			}

			if (this.walkRandom && random.nextInt(40) == 0)
			{
				this.targetX = (int) entity.posX + random.nextInt(40) - 20;
			}
		}

		if (Math.abs(entity.posX - this.targetX) > 0.2)
		{
			final double distance;
			if (entity.posX > this.targetX)
			{
				distance = -0.2;
				entity.pitch = 180;
			}
			else
			{
				distance = 0.2;
				entity.pitch = 0;
			}

			if (!entity.tryMove(distance, 0))
			{
				entity.jump();
			}
		}
	}
}
