package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.gui.GuiDeath;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderPlayer;
import com.clashsoft.dungeonrun.inventory.InventoryPlayer;
import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;
import org.newdawn.slick.SlickException;

import java.util.Random;

public class EntityPlayer extends EntityLiving
{
	public String username = "";

	public InventoryPlayer inventory;
	public boolean isWalking   = false;
	public boolean isSprinting = false;

	public EntityPlayer(World world) throws SlickException
	{
		this(world, "");
	}

	public EntityPlayer(World world, String username) throws SlickException
	{
		super(world);
		this.username = username;
		this.inventory = new InventoryPlayer(this);
	}

	@Override
	public float getWidth()
	{
		return 12 / 16F;
	}

	@Override
	public float getHeight()
	{
		return 24 / 16F;
	}

	@Override
	public RenderPlayer getRenderer()
	{
		return RenderPlayer.INSTANCE;
	}

	@Override
	public void updateEntity(Random random)
	{
		if (this.isWalking)
		{
			double distance = this.airTime > 0 ? 0.4 : this.isSprinting ? 0.3 : 0.15;

			if (this.pitch >= 90 && this.pitch <= 270)
			{
				distance = -distance;
			}

			this.tryMove(distance, 0);
		}

		super.updateEntity(random);
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

	public void walk(int sign)
	{
		if (sign > 0)
		{
			this.pitch = 0;
		}
		else
		{
			this.pitch = 180;
		}

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
