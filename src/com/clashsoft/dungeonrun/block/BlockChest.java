package com.clashsoft.dungeonrun.block;

import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.item.Item;
import com.clashsoft.dungeonrun.item.ItemStack;
import com.clashsoft.dungeonrun.item.Items;
import com.clashsoft.dungeonrun.world.ForegroundBlock;
import com.clashsoft.dungeonrun.world.World;
import org.newdawn.slick.Image;

public class BlockChest extends BlockBackground
{
	private Image openTexture;

	public BlockChest(String name)
	{
		super(name);
	}

	@Override
	public void registerIcons()
	{
		super.registerIcons();

		this.openTexture = getIcon(this.blockName + "_open");
	}

	@Override
	public void activate(World world, ForegroundBlock block, EntityPlayer player)
	{
		if (block.metadata != 0)
		{
			return;
		}

		block.metadata = 1;

		if (this != Blocks.stoneChest)
		{
			player.inventory.add(new ItemStack(Items.copper_coin, 0, 2 + world.random.nextInt(8)));
			return;
		}

		final Object[] items = Item.items.values().toArray();
		final Item item = (Item) items[world.random.nextInt(items.length)];

		player.inventory.add(new ItemStack(item));
	}

	@Override
	public Image getTexture(int metadata)
	{
		return metadata == 1 ? this.openTexture : this.texture;
	}
}
