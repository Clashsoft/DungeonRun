package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.gui.GuiDeath;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderPlayer;
import com.clashsoft.dungeonrun.inventory.InventoryPlayer;
import com.clashsoft.dungeonrun.item.ItemStack;
import com.clashsoft.dungeonrun.item.Items;
import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;
import org.newdawn.slick.SlickException;

public class EntityPlayer extends EntityLiving
{
	public String username = "";

	public final InventoryPlayer inventory;

	public EntityPlayer(World world) throws SlickException
	{
		this(world, "");
	}

	public EntityPlayer(World world, String username) throws SlickException
	{
		super(world);
		this.username = username;
		this.inventory = new InventoryPlayer(this);
		this.inventory.setStack(0, new ItemStack(Items.sword, 0, 1));
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
