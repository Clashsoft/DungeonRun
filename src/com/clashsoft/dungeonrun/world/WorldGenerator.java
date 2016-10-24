package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;

import java.util.Random;

public class WorldGenerator
{
	public static void generate(World world)
	{

	}

	public static void generateChunk(Chunk chunk)
	{
		Random random = new Random();
		int top = 62;

		for (int x = 0; x < 16; x++)
		{
			top += random.nextBoolean() ? 1 : -1;
			int stone = top - 4 - random.nextInt(3);

			for (int y = 0; y <= top; y++)
			{
				final int blockID;

				if (y == top)
				{
					blockID = Block.grass.blockID;
				}
				else if (y > stone)
				{
					blockID = Block.dirt.blockID;
				}
				else
				{
					blockID = Block.stone.blockID;
				}

				chunk.setBlock(blockID, 0, x, y, 0);
			}

			if (x > 2 && x < 15 && random.nextInt(14) == 0)
			{
				int height = random.nextInt(2) + 3;
				for (int i = 1; i <= height; i++)
				{
					chunk.setBlock(Block.log.blockID, 0, x, top + i, 0);
				}

				chunk.setBlock(Block.leaves.blockID, 0, x - 1, top + height, 0);
				chunk.setBlock(Block.leaves.blockID, 0, x + 1, top + height, 0);
				chunk.setBlock(Block.leaves.blockID, 0, x, top + height + 1, 0);
			}
		}
		chunk.initializeLightValues();
	}
}
