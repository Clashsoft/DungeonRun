package com.clashsoft.dungeonrun.world.gen;

import com.clashsoft.dungeonrun.block.Blocks;
import com.clashsoft.dungeonrun.world.World;

import java.util.Random;

public class TreeGenerator
{
	public static void generateTree(World world, Random random, int x, int y)
	{
		int height = random.nextInt(3) + 3;
		for (int i = 1; i <= height; i++)
		{
			world.setBlock(Blocks.log, 0, x, y + i);
		}

		world.setBlock(Blocks.leaves, 0, x - 1, y + height);
		world.setBlock(Blocks.leaves, 0, x + 1, y + height);
		world.setBlock(Blocks.leaves, 0, x, y + height + 1);
	}
}
