package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;
import com.clashsoft.nbt.util.INBTSaveable;

import java.util.Random;

public abstract class Entity implements INBTSaveable
{
	protected static final int SOLID     = 1;
	protected static final int CLIMBABLE = 2;

	public static int nextEntityId = 0;

	public int id;

	public final World worldObj;

	private boolean isDead;

	public double posX;
	public double posY;
	public double velocityX;
	public double velocityY;
	public float  pitch;

	public int airTime = 0;

	public Entity(World world)
	{
		this.id = nextEntityId++;
		this.worldObj = world;
	}

	public double getX()
	{
		return this.posX;
	}

	public int getBlockX()
	{
		return (int) Math.round(this.posX - 0.5);
	}

	public int getBlockY()
	{
		return (int) Math.round(this.posY - 0.5);
	}

	public double getY()
	{
		return this.posY;
	}

	public void setLocation(double x, double y)
	{
		this.posX = x;
		this.posY = y;
	}

	public float getPitch()
	{
		return this.pitch;
	}

	public void setPitch(float pitch)
	{
		pitch %= 360;
		if (pitch < 0)
		{
			pitch += 360;
		}

		this.pitch = pitch;
	}

	public double getVelocityX()
	{
		return this.velocityX;
	}

	public double getVelocityY()
	{
		return this.velocityY;
	}

	public void setVelocity(double x, double y)
	{
		this.velocityX = x;
		this.velocityY = y;
	}

	public void addVelocity(double x, double y)
	{
		this.velocityX += x;
		this.velocityY += y;
	}

	public void move(double x, double y)
	{
		this.posX += x;
		this.posY += y;
	}

	public boolean tryMove(double x, double y)
	{
		final int parts = 10;
		final double partX = x / parts;
		final double partY = y / parts;

		for (int i = 0; i < parts; i++)
		{
			this.posX += partX;
			this.posY += partY;

			if (this.isCollided())
			{
				this.posX -= partX;
				this.posY -= partY;
				return true;
			}
		}

		return false;
	}

	public abstract float getWidth();

	public abstract float getHeight();

	public void setDead()
	{
		this.isDead = true;
	}

	public boolean isDead()
	{
		return this.isDead;
	}

	public void updateEntity(Random random)
	{
		this.applyGravity();
		this.processVelocity();
	}

	protected void applyGravity()
	{
		final int collide = this.checkCollide();
		if (collide != 0)
		{
			this.airTime = 0;
			return;
		}

		final double offset = 0.1 + this.airTime * 0.1;

		if (this.tryMove(0, -offset))
		{
			this.airTime = 0;
		}
		else
		{
			this.airTime++;
		}
	}

	protected void processVelocity()
	{
		if (this.tryMove(this.velocityX, this.velocityY))
		{
			this.velocityX = this.velocityY = 0;
			return;
		}

		this.addVelocity(toZero(this.velocityX, 0.1F), toZero(this.velocityY, 0.1F));
	}

	private static double toZero(double from, double by)
	{
		if (from > 0)
		{
			return from > by ? -by : -from;
		}
		else
		{
			return -from > by ? by : from;
		}
	}

	public boolean isCollided()
	{
		return (this.checkCollide() & SOLID) != 0;
	}

	public int checkCollide()
	{
		if (this.posY < 0)
		{
			return 0;
		}

		final double width = this.getWidth();
		int x1 = (int) Math.floor(this.posX - width / 2);
		int x2 = (int) Math.floor(this.posX + width / 2);
		int y1 = (int) (Math.ceil(this.posY));
		int y2 = (int) Math.floor(this.posY + this.getHeight());

		int result = 0;
		for (int x = x1; x <= x2; x++)
		{
			for (int y = y1; y <= y2; y++)
			{
				final Block block = this.worldObj.getBlock(x, y);
				if (block.isClimbable())
				{
					result |= CLIMBABLE;
				}
				if (block.isSolid())
				{
					result |= SOLID;
				}
			}
		}
		return result;
	}

	public abstract Render getRenderer();

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("type", EntityList.getNameFromClass(this.getClass()));
		nbt.setInteger("id", this.id);

		nbt.setDouble("posX", this.posX);
		nbt.setDouble("posY", this.posY);

		nbt.setDouble("velocityY", this.velocityX);
		nbt.setDouble("velocityX", this.velocityY);

		nbt.setFloat("pitch", this.pitch);
		nbt.setInteger("airtime", this.airTime);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.id = nbt.getInteger("id");

		this.posX = nbt.getDouble("posX");
		this.posY = nbt.getDouble("posY");

		this.velocityX = nbt.getDouble("velocityX");
		this.velocityY = nbt.getDouble("velocityY");

		this.pitch = nbt.getFloat("pitch");
		this.airTime = nbt.getInteger("airtime");
	}
}
