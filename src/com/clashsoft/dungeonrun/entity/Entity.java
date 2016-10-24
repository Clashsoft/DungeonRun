package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.world.BlockInWorld;
import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;
import com.clashsoft.nbt.util.INBTSaveable;
import org.newdawn.slick.SlickException;

public abstract class Entity implements INBTSaveable
{
	public static int nextEntityId = 0;

	public int entityId;

	public final World worldObj;

	private boolean isDead;

	public double posX      = 0.5;
	public double posY      = 70;
	public double velocityX = 0;
	public double velocityY = 0;
	public byte   rot       = 3;

	public int airTime = 0;

	public Entity(World world)
	{
		this.entityId = nextEntityId++;
		this.worldObj = world;
		this.setLocation(0.5F, 32);
		this.setVelocity(0, 0);
		this.setRotation(3); // South
	}

	public void setLocation(double x, double y)
	{
		this.posX = x;
		this.posY = y;
	}

	public void setRotation(int rot)
	{
		this.rot = (byte) (rot % 4);
	}

	public void setVelocity(double x, double y)
	{
		this.velocityX = x;
		this.velocityY = y;
	}

	public void move(double x, double y)
	{
		this.posX += x;
		this.posY += y;
	}

	public void addVelocity(double x, double y)
	{
		this.velocityX += x;
		this.velocityY += y;
	}

	public void move(double distance, int dir)
	{
		switch (dir)
		{
		case 1:
			this.move(distance, 0D);
			break;
		case 3:
			this.move(-distance, 0D);
			break;
		}
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

	public void updateEntity()
	{
		this.applyGravity();
		this.processVelocity();
	}

	protected void applyGravity()
	{
		final float offset = 0.1F + this.airTime * 0.1F;
		this.posY -= offset;

		if (this.isCollided())
		{
			this.posY += offset;
			this.airTime = 0;
		}
		else
		{
			this.airTime++;
		}
	}

	protected void processVelocity()
	{
		this.posX += this.velocityX;
		this.posY += this.velocityY;

		this.addVelocity(getNormalizer(this.velocityX, 0.1F), getNormalizer(this.velocityY, 0.1F));
	}

	private static double getNormalizer(double par1, double par2)
	{
		if (par1 >= par2)
		{
			return -par2;
		}
		else if (par1 <= -par2)
		{
			return par2;
		}
		else if (par1 < par2 && par1 >= 0)
		{
			return -par1;
		}
		else if (par1 > -par2 && par1 <= 0)
		{
			return par1;
		}
		return 0F;
	}

	public boolean isCollided()
	{
		if (this.posY < 0)
		{
			return false;
		}

		final double width = this.getWidth();
		int x1 = (int) Math.floor(this.posX - width / 2);
		int x2 = (int) Math.floor(this.posX + width / 2);
		int y1 = (int) (Math.ceil(this.posY));
		int y2 = (int) Math.floor(this.posY + this.getHeight());

		for (int x = x1; x <= x2; x++)
		{
			for (int y = y1; y <= y2; y++)
			{
				BlockInWorld block = this.worldObj.getBlock(x, y);
				if (this.canCollide(block))
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean canCollide(BlockInWorld block)
	{
		return block != null && block.getBlock() != null && block.getBlock().canCollide(block.getMetadata(), this);
	}

	public abstract Render getRenderer() throws SlickException;

	public abstract String getTexture();

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("type", EntityList.getNameFromClass(this.getClass()));
		nbt.setInteger("id", this.entityId);

		NBTTagCompound pos = new NBTTagCompound("pos");
		pos.setDouble("x", this.posX);
		pos.setDouble("y", this.posY);
		nbt.setTagCompound(pos);

		NBTTagCompound momentum = new NBTTagCompound("velocity");
		momentum.setDouble("x", this.velocityX);
		momentum.setDouble("y", this.velocityY);
		nbt.setTagCompound(momentum);

		nbt.setByte("rot", this.rot);
		nbt.setInteger("airtime", this.airTime);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.entityId = nbt.getInteger("id");

		NBTTagCompound pos = nbt.getTagCompound("pos");
		if (pos != null)
		{
			this.posX = pos.getDouble("x");
			this.posY = pos.getDouble("y");
		}

		NBTTagCompound momentum = nbt.getTagCompound("velocity");
		if (momentum != null)
		{
			this.velocityX = momentum.getDouble("x");
			this.velocityY = momentum.getDouble("y");
		}

		this.rot = nbt.getByte("rot");
		this.airTime = nbt.getInteger("airtime");
	}
}
