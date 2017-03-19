package com.clashsoft.dungeonrun.entity.npc;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderClerk;
import com.clashsoft.dungeonrun.entity.DamageSource;
import com.clashsoft.dungeonrun.entity.EntityLiving;
import com.clashsoft.dungeonrun.world.World;

public class EntityClerk extends EntityLiving
{
	public EntityClerk(World world)
	{
		super(world);
	}

	@Override
	public float getWidth()
	{
		return 12 / 16F;
	}

	@Override
	public float getHeight()
	{
		return 23 / 16F;
	}

	@Override
	public Render getRenderer()
	{
		return RenderClerk.INSTANCE;
	}

	@Override
	public boolean canBeDamagedBy(DamageSource source)
	{
		return false;
	}
}
