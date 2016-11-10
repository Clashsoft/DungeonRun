package com.clashsoft.dungeonrun.block;

import com.clashsoft.dungeonrun.entity.EntityPlayer;
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
		block.metadata = 1;
	}

	@Override
	public Image getTexture(int metadata)
	{
		return metadata == 1 ? this.openTexture : this.texture;
	}
}
