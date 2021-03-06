package com.clashsoft.dungeonrun.entity.npc;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.client.gui.GuiTrades;
import com.clashsoft.dungeonrun.client.renderer.Render;
import com.clashsoft.dungeonrun.client.renderer.entity.RenderClerk;
import com.clashsoft.dungeonrun.entity.DamageSource;
import com.clashsoft.dungeonrun.entity.EntityLiving;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.item.Item;
import com.clashsoft.dungeonrun.item.ItemStack;
import com.clashsoft.dungeonrun.world.World;
import dyvil.tools.nbt.NamedBinaryTag;
import dyvil.tools.nbt.collection.NBTList;
import dyvil.tools.nbt.collection.NBTMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EntityClerk extends EntityLiving
{
	public static class Trade
	{
		public final int       amount; // positive: clerk sells item, negative: clerk buys item
		public final ItemStack item;

		public Trade(int amount, ItemStack item)
		{
			this.amount = amount;
			this.item = item;
		}
	}

	private List<Trade> trades = new ArrayList<>();

	public EntityClerk(World world)
	{
		super(world);
	}

	public int getTradeCount()
	{
		return this.trades.size();
	}

	public Trade getTrade(int index)
	{
		return this.trades.get(index);
	}

	public void generateTrades(Random random)
	{
		List<ItemStack> valuables = new ArrayList<>();
		for (Item item : Item.items.values())
		{
			for (ItemStack stack : item.getSubItems())
			{
				if (item.getValue(stack) > 0)
				{
					valuables.add(stack);
				}
			}
		}
		Collections.shuffle(valuables);

		final float multiplier = 1 + random.nextFloat() * 0.5F;
		for (int n = random.nextInt(3) + 3; n >= 0; n--)
		{
			final int size = valuables.size();
			if (size == 0)
			{
				return;
			}

			ItemStack valuable = valuables.remove(size - 1);
			int price = (int) (valuable.getValue() * multiplier);
			if (random.nextBoolean())
			{
				price *= -1;
			}
			this.trades.add(new Trade(price, valuable));
		}
	}

	@Override
	public float getWidth()
	{
		return 12 / 16F;
	}

	@Override
	public float getHeight()
	{
		return 23 / 16F;
	}

	@Override
	public Render getRenderer()
	{
		return RenderClerk.INSTANCE;
	}

	@Override
	public boolean canBeDamagedBy(DamageSource source)
	{
		return false;
	}

	@Override
	public void onPlayerInteract(EntityPlayer player)
	{
		DungeonRunClient.instance.displayGuiScreen(new GuiTrades(player, this));
	}

	@Override
	public void writeToNBT(NBTMap nbt)
	{
		super.writeToNBT(nbt);

		NBTList trades = new NBTList(this.trades.size());
		for (Trade trade : this.trades)
		{
			NBTMap map = new NBTMap();
			trade.item.writeToNBT(map);
			map.setInteger("amount", trade.amount);
			trades.addTag(map);
		}

		nbt.setTag("trades", trades);
	}

	@Override
	public void readFromNBT(NBTMap nbt)
	{
		super.readFromNBT(nbt);

		NBTList trades = nbt.getTagList("trades");
		if (trades == null)
		{
			return;
		}

		this.trades.clear();
		for (int i = 0, count = trades.size(); i < count; i++)
		{
			final NamedBinaryTag tag = trades.getTag(i);
			if (!(tag instanceof NBTMap))
			{
				continue;
			}

			final NBTMap map = (NBTMap) tag;
			int amount = map.getInteger("amount");
			ItemStack item = ItemStack.readFromNBT(map);
			this.trades.add(new Trade(amount, item));
		}
	}
}
