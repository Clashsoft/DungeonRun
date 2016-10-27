package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderMonster;
import com.clashsoft.dungeonrun.world.World;

import java.util.Random;

public class EntityMonster extends EntityLiving
{
	public int alertTicks;

	private final EntityAIFollow ai = new EntityAIFollow(8.0, true);

	public EntityMonster(World world)
	{
		super(world);
	}

	public boolean isAttacking()
	{
		return false;
	}

	@Override
	public void updateEntity(Random random)
	{
		this.ai.update(this, random);

		if (this.ai.target != null)
		{
			this.alertTicks++;
		}
		else
		{
			this.alertTicks = 0;
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
