package com.clashsoft.dungeonrun.block;

import com.clashsoft.dungeonrun.world.ForegroundBlock;
import com.clashsoft.dungeonrun.world.World;

public class BlockLadder extends Block
{
	public BlockLadder(String id)
	{
		super(id);
	}

	@Override
	public boolean isClimbable()
	{
		return true;
	}

	public static void setLadder(World world, int x, int y)
	{
		final Block block = world.getBlock(x, y);

		if (block.isSolid())
		{
			if (block instanceof BlockSolid)
			{
				world.setBlock(((BlockSolid) block).getWallBlock(), 0, x, y);
			}
			else
			{
				world.setBlock(Blocks.air, 0, x, y);
			}
		}

		world.addForegroundBlock(new ForegroundBlock(x, y, Blocks.ladder, 0));
	}
}
