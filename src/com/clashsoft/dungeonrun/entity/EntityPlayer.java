package com.clashsoft.dungeonrun.entity;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.gui.GuiDeath;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderPlayer;
import com.clashsoft.dungeonrun.inventory.InventoryPlayer;
import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;

public class EntityPlayer extends EntityLiving
{
	public RenderPlayer renderer;
	
	public String username = "";
	
	public InventoryPlayer	inventory;
	public boolean			isWalking	= false;
	public boolean			isSprinting	= false;
	
	public EntityPlayer(World world) throws SlickException
	{
		this(world, "");
	}
	
	public EntityPlayer(World world, String username) throws SlickException
	{
		super(world);
		this.username = username;
		this.inventory = new InventoryPlayer(this);
		this.renderer = new RenderPlayer(this);
	}
	
	@Override
	public RenderPlayer getRenderer() throws SlickException
	{
		return this.renderer;
	}
	
	@Override
	public String getTexture()
	{
		return "resources/textures/entity/knights.png";
	}
	
	@Override
	public void updateEntity()
	{
		int i = this.isSprinting ? 2 : 1;
		float f = Math.abs(this.rot == 0 || this.rot == 2 ? (float) this.posZ % 1F : (float) this.posX % 1F);
		
		boolean b = this.canMove(f, this.rot) || this.canMove(1F - f, this.rot);
		
		if (this.isWalking && b)
		{
			this.move(0.1F * i, this.rot);
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
		this.rot = (byte) (dir & 3);
		this.isWalking = true;
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
		
		nbt.setString("Username", this.username);
		
		NBTTagCompound inventory = new NBTTagCompound("Inventory");
		this.inventory.writeToNBT(inventory);
		nbt.setTagCompound(inventory);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		this.username = nbt.getString("Username");
		
		NBTTagCompound inventory = nbt.getTagCompound("Inventory");
		this.inventory.readFromNBT(inventory);
	}
}
