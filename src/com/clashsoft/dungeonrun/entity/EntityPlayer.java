package com.clashsoft.dungeonrun.entity;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.gui.GuiDeath;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderPlayer;
import com.clashsoft.dungeonrun.inventory.InventoryPlayer;
import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.nbt.NBTTagCompound;

public class EntityPlayer extends EntityLiving
{
	public RenderPlayer		renderer;
	
	public String			username	= "";
	
	public InventoryPlayer	inventory;
	public boolean			isWalking	= false;
	public boolean			isSprinting	= false;
	private int				stepsWalked	= 0;
	
	public EntityPlayer(World world) throws SlickException
	{
		super(world);
		this.inventory = new InventoryPlayer(this);
		this.renderer = new RenderPlayer();
	}
	
	@Override
	public RenderPlayer getRenderer() throws SlickException
	{
		return this.renderer;
	}
	
	@Override
	public String getTexture()
	{
		return "resources/entity/knights.png";
	}
	
	@Override
	public void updateEntity()
	{
		int i = this.isSprinting ? 2 : 1;
		float f = Math.abs(this.rot == 0 || this.rot == 2 ? (float) this.posZ % 1F : (float) this.posX % 1F);
		
		boolean b = this.canMove(f, this.rot) || this.canMove(1 - f, this.rot);
		
		if (this.stepsWalked >= 5 && f >= 0.499F && f <= 0.501F || !b)
		{
			this.isWalking = false;
			this.isSprinting = false;
			this.stepsWalked = 0;
		}
		
		if (this.isWalking && b)
		{
			this.move(0.1F * i, this.rot);
			this.stepsWalked += i;
		}
		super.updateEntity();
	}
	
	@Override
	public void setDead()
	{
		try
		{
			DungeonRunClient.instance.displayGuiScreen(new GuiDeath());
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		super.setDead();
	}
	
	public void walk(int dir)
	{
		if (!this.isWalking)
		{
			this.stepsWalked = 0;
			if (this.rot != (byte) (dir % 4))
			{
				this.rot = (byte) (dir % 4);
				this.isWalking = false;
				return;
			}
			else
			{
				this.isWalking = true;
			}
		}
	}
	
	@Override
	public boolean canBeDamagedBy(DamageSource source)
	{
		return true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("StepsWalked", this.stepsWalked);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.stepsWalked = nbt.getInteger("StepsWalked");
		
		NBTTagCompound inventory = new NBTTagCompound("Inventory");
		this.inventory.writeToNBT(inventory);
		nbt.setTagCompound(inventory);
	}
}
