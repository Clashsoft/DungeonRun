package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderMonster;
import com.clashsoft.dungeonrun.world.World;

import java.util.Random;

public class EntityMonster extends EntityLiving
{
	private double targetX;
	private EntityPlayer target;
	public int alertTicks;

	public EntityMonster(World world)
	{
		super(world);
	}

	public boolean isAttacking()
	{
		return false;
	}

	private double squareDistance(Entity entity)
	{
		double dx = entity.posX - this.posX;
		double dy = entity.posY - this.posY;
		return dx * dx + dy * dy;
	}

	@Override
	public void updateEntity(Random random)
	{
		if (this.target != null)
		{
			if (this.squareDistance(this.target) > 64)
			{
				this.target = null;
				this.alertTicks = 0;
			}
			else
			{
				this.targetX = this.target.posX;

				if (this.alertTicks > 0)
				{
					this.alertTicks--;
				}
			}
		}
		else
		{
			for (EntityPlayer player : this.worldObj.getPlayers())
			{
				if (this.squareDistance(player) <= 64)
				{
					this.alertTicks = 10;
					this.target = player;
					break;
				}
			}

			if (random.nextInt(40) == 0)
			{
				this.alertTicks = 0;
				this.targetX = (int) this.posX + random.nextInt(40) - 20;
			}
		}

		if (Math.abs(this.posX - this.targetX) > 0.2)
		{
			final double distance;
			if (this.posX > this.targetX)
			{
				distance = -0.2;
				this.pitch = 180;
			}
			else
			{
				distance = 0.2;
				this.pitch = 0;
			}

			if (!this.tryMove(distance, 0))
			{
				this.jump();
			}
		}

		super.updateEntity(random);
	}

	@Override
	public boolean canBeDamagedBy(DamageSource source)
	{
		return true;
	}

	@Override
	public float getWidth()
	{
		return 30 / 16F;
	}

	@Override
	public float getHeight()
	{
		return 25 / 16F;
	}

	@Override
	public Render getRenderer()
	{
		return RenderMonster.INSTANCE;
	}
}
