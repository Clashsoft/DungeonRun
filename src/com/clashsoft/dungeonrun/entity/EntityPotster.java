package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderPotster;
import com.clashsoft.dungeonrun.world.World;
import dyvil.tools.nbt.collection.NBTMap;

import java.util.Random;

public class EntityPotster extends EntityLiving
{
	private boolean awake;

	private final EntityAIFollow ai = new EntityAIFollow(3.0, false);

	public EntityPotster(World world)
	{
		super(world);
	}

	public boolean isAwake()
	{
		return this.awake;
	}

	@Override
	public float getWidth()
	{
		return 1;
	}

	@Override
	public float getHeight()
	{
		return 2;
	}

	@Override
	public Render getRenderer()
	{
		return RenderPotster.INSTANCE;
	}

	@Override
	public void updateEntity(Random random)
	{
		this.ai.update(this, random);
		this.awake = this.ai.target != null;

		super.updateEntity(random);
	}

	@Override
	public boolean canBeDamagedBy(DamageSource source)
	{
		return true;
	}

	@Override
	public void writeToNBT(NBTMap nbt)
	{
		super.writeToNBT(nbt);

		nbt.setBoolean("awake", this.awake);
	}

	@Override
	public void readFromNBT(NBTMap nbt)
	{
		super.readFromNBT(nbt);

		this.awake = nbt.getBoolean("awake");
	}
}
