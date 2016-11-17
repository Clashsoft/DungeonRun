package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.gui.GuiDeath;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderPlayer;
import com.clashsoft.dungeonrun.inventory.InventoryPlayer;
import com.clashsoft.dungeonrun.item.ItemStack;
import com.clashsoft.dungeonrun.world.World;
import dyvil.tools.nbt.collection.NBTMap;
import org.newdawn.slick.SlickException;

import java.util.Random;

public class EntityPlayer extends EntityLiving
{
	private String username;

	public final InventoryPlayer inventory;

	private int attackTime;

	private int kills;

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

	public String getUsername()
	{
		return this.username;
	}

	public int getKillCount()
	{
		return this.kills;
	}

	@Override
	public RenderPlayer getRenderer()
	{
		return RenderPlayer.INSTANCE;
	}

	public int getAttackTime()
	{
		return this.attackTime;
	}

	public void attack()
	{
		if (this.attackTime == 0)
		{
			this.attackTime = 10;
		}
		else
		{
			return;
		}

		final ItemStack selected = this.inventory.getHeldStack();
		final float damage = 1F + (selected != null ? selected.item.getDamageVsEntity(selected) : 0F);

		for (Entity entity : this.worldObj.getEntitys())
		{
			if (entity.squareDistanceTo(this) > 4 || entity == this || !(entity instanceof EntityDamagable))
			{
				continue;
			}

			((EntityDamagable) entity).damageEntity(DamageSource.PLAYER, damage);
			if (entity.isDead())
			{
				this.kills++;
			}
		}
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
	public void updateEntity(Random random)
	{
		if (this.attackTime > 0)
		{
			this.attackTime--;
		}

		super.updateEntity(random);
	}

	@Override
	public void writeToNBT(NBTMap nbt)
	{
		super.writeToNBT(nbt);

		nbt.setString("username", this.username);
		nbt.setInteger("kills", this.kills);

		final NBTMap inventory = new NBTMap();
		this.inventory.writeToNBT(inventory);
		nbt.setTag("inventory", inventory);
	}

	@Override
	public void readFromNBT(NBTMap nbt)
	{
		super.readFromNBT(nbt);

		this.username = nbt.getString("username");
		this.kills = nbt.getInteger("kills");

		this.inventory.readFromNBT(nbt.getTagCompound("inventory"));
	}
}
