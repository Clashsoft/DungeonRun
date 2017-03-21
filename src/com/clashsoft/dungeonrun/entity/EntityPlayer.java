package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.gui.GuiDeath;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderPlayer;
import com.clashsoft.dungeonrun.inventory.InventoryPlayer;
import com.clashsoft.dungeonrun.item.ItemStack;
import com.clashsoft.dungeonrun.item.Items;
import com.clashsoft.dungeonrun.world.World;
import dyvil.tools.nbt.collection.NBTMap;

import java.util.Random;

public class EntityPlayer extends EntityLiving
{
	private String username;

	public final InventoryPlayer inventory;

	private int attackTime;

	private int kills;

	public EntityPlayer(World world)
	{
		this(world, "");
	}

	public EntityPlayer(World world, String username)
	{
		super(world);
		this.username = username;
		this.inventory = new InventoryPlayer(this);
		this.inventory.add(new ItemStack(Items.wood_sword));
		this.inventory.add(new ItemStack(Items.gold_coin));
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
		final float damage = 1F + (selected != null ? selected.item.getDamage(selected) : 0F);
		final float knockback = 0.1F + (selected != null ? selected.item.getKnockback(selected) : 0F);

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
			entity.addVelocity((this.pitch >= 90 && this.pitch <= 270 ? -1 : 1) * knockback, 0.5);
		}
	}

	@Override
	public void setDead()
	{
		DungeonRunClient.instance.displayGuiScreen(new GuiDeath());
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
