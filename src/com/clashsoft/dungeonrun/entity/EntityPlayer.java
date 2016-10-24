package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.gui.GuiDeath;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderPlayer;
import com.clashsoft.dungeonrun.inventory.InventoryPlayer;
import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;
import org.newdawn.slick.SlickException;

public class EntityPlayer extends EntityLiving
{
	public RenderPlayer renderer;

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
		this.renderer = new RenderPlayer(this);
	}

	@Override
	public float getWidth()
	{
		return 0.75F;
	}

	@Override
	public float getHeight()
	{
		return 1.5F;
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
		if (this.isWalking)
		{
			final double distance = this.airTime > 0 ? 0.3 : this.isSprinting ? 0.2 : 0.1;
			this.move(distance, this.rot);

			if (this.isCollided())
			{
				this.move(-distance, this.rot);
			}
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
