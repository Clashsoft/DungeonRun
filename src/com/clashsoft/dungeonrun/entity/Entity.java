package com.clashsoft.dungeonrun.entity;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.entity.render.RenderEntity;
import com.clashsoft.dungeonrun.world.BlockInWorld;
import com.clashsoft.dungeonrun.world.World;

public abstract class Entity
{
	public static int	nextEntityId	= 0;
	
	public int			entityId;
	
	public World		worldObj;
	
	public double		posX			= 0.5;
	public double		posY			= 32;
	public double		posZ			= 0.5;
	public double		velocityX		= 0;
	public double		velocityY		= 0;
	public double		velocityZ		= 0;
	public byte			rot				= 3;
	
	public int			airTime			= 0;
	
	public Entity(World world)
	{
		this.entityId = nextEntityId;
		nextEntityId++;
		this.worldObj = world;
		this.setLocation(0.5F, 32, 0.5F);
		this.setVelocity(0, 0, 0);
		this.setRotation(3); // South
	}
	
	public void setLocation(double x, double y, double z)
	{
		posX = x;
		posY = y;
		posZ = z;
	}
	
	public void setRotation(int rot)
	{
		this.rot = (byte) (rot % 4);
	}
	
	public void setVelocity(double x, double y, double z)
	{
		velocityX = x;
		velocityY = y;
		velocityZ = z;
	}
	
	public void move(double x, double y, double z)
	{
		if (canMove(x, y, z))
		{
			posX += x;
			posY += y;
			posZ += z;
		}
	}
	
	public void addVelocity(double x, double y, double z)
	{
		velocityX += x;
		velocityY += y;
		velocityZ += z;
	}
	
	public void move(double distance, int dir)
	{
		if (dir == 0) // North
			move(0, 0, -distance);
		else if (dir == 1) // East
			move(distance, 0, 0);
		else if (dir == 2)
			move(0, 0, distance);
		else if (dir == 3)
			move(-distance, 0, 0);
	}
	
	public void setDead()
	{
		this.worldObj.removeEntity(this.entityId);
	}
	
	public void updateEntity()
	{
		applyGravity();
		processVelocity();
	}
	
	protected void applyGravity()
	{
		if (!isCollidedVertically())
		{
			this.posY -= 0.1F + (airTime * 0.1F);
			airTime++;
		}
		else
			airTime = 0;
	}
	
	protected void processVelocity()
	{
		posX += velocityX;
		posY += velocityY;
		posZ += velocityZ;
		
		addVelocity(getNormalizer(velocityX, 0.1F), getNormalizer(velocityY, 0.1F), getNormalizer(velocityZ, 0.1F));
	}
	
	private double getNormalizer(double par1, double par2)
	{
		if (par1 >= par2)
			return -par2;
		else if (par1 <= -par2)
			return par2;
		else if (par1 < par2 && par1 >= 0)
			return 0 - par1;
		else if (par1 > -par2 && par1 <= 0)
			return par1;
		return 0F;
	}
	
	public boolean isCollidedVertically()
	{
		if (posY < 64 && posY >= 0)
		{
			BlockInWorld block = worldObj.getBlock((int) Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ));
			return canCollideWithBlockVertically(block);
		}
		else if (posY <= -64)
			this.setDead();
		return false;
	}
	
	public boolean canMove(double distance, int dir)
	{
		if (dir == 0) // North
			return canMove(0, 0, -distance);
		else if (dir == 1) // East
			return canMove(distance, 0, 0);
		else if (dir == 2)
			return canMove(0, 0, distance);
		else if (dir == 3)
			return canMove(-distance, 0, 0);
		return false;
	}
	
	public boolean canMove(double x, double y, double z)
	{
		double newX = posX + x;
		double newY = posY + y;
		double newZ = posZ + z;
		
		if (newY + 1 >= 64)
			return true;
		
		BlockInWorld block1 = worldObj.getBlock((int) Math.floor(newX), (int) Math.floor(newY + 1), (int) Math.floor(newZ));
		
		if (newY + 2 >= 64)
			return !canCollideWithBlockHorizontally(block1);
		
		BlockInWorld block2 = worldObj.getBlock((int) Math.floor(newX), (int) Math.floor(newY + 2), (int) Math.floor(newZ));
		
		boolean b1 = !canCollideWithBlockHorizontally(block1);
		boolean b2 = !canCollideWithBlockHorizontally(block2);
		return b1 && b2;
	}
	
	public boolean canCollideWithBlockVertically(BlockInWorld block)
	{
		return (block != null && (block.getBlock() != null && block.getBlock().canCollideVertically(block.getMetadata(), this)));
	}
	
	public boolean canCollideWithBlockHorizontally(BlockInWorld block)
	{
		return (block != null && (block.getBlock() != null && block.getBlock().canCollideHorizontally(block.getMetadata(), this)));
	}
	
	public abstract RenderEntity getRenderer() throws SlickException;
	
	public abstract String getTexture();
}
