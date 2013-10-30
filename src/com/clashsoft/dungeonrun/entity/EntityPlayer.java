package com.clashsoft.dungeonrun.entity;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.container.InventoryPlayer;
import com.clashsoft.dungeonrun.entity.render.RenderEntity;
import com.clashsoft.dungeonrun.entity.render.RenderPlayer;
import com.clashsoft.dungeonrun.gui.GuiDeath;
import com.clashsoft.dungeonrun.nbt.NBTTagCompound;
import com.clashsoft.dungeonrun.world.World;

public class EntityPlayer extends EntityLiving
{
	public RenderPlayer<EntityPlayer>	renderer;
	
	public String						username	= "";
	
	public InventoryPlayer				inventory;
	public boolean						isWalking	= false;
	public boolean						isSprinting	= false;
	private int							stepsWalked	= 0;
	
	public EntityPlayer(World world) throws SlickException
	{
		super(world);
		inventory = new InventoryPlayer(this);
		renderer = new RenderPlayer<EntityPlayer>(this);
	}
	
	@Override
	public RenderEntity getRenderer() throws SlickException
	{
		return renderer;
	}
	
	@Override
	public String getTexture()
	{
		return "resources/entity/knights.png";
	}
	
	@Override
	public void updateEntity()
	{
		int i = isSprinting ? 2 : 1;
		float f = Math.abs(rot == 0 || rot == 2 ? (float) posZ % 1F : (float) posX % 1F);
		
		boolean b = canMove(f, this.rot) || canMove(1 - f, this.rot);
		
		if ((stepsWalked >= 5 && (f >= 0.499F && f <= 0.501F)) || !b)
		{
			this.isWalking = false;
			this.isSprinting = false;
			stepsWalked = 0;
		}
		
		if (isWalking && b)
		{
			this.move(0.1F * i, this.rot);
			stepsWalked += i;
		}
		super.updateEntity();
	}
	
	@Override
	public void setDead()
	{
		try
		{
			DungeonRun.instance.displayGuiScreen(new GuiDeath());
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		super.setDead();
	}
	
	public void walk(int dir)
	{
		if (!isWalking)
		{
			stepsWalked = 0;
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
