package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.block.Blocks;
import com.clashsoft.dungeonrun.entity.EntityMonster;
import com.clashsoft.dungeonrun.world.gen.HouseGenerator;
import com.clashsoft.dungeonrun.world.gen.TreeGenerator;

import java.util.Random;

public class WorldGenerator
{
	public static void generateChunk(Chunk chunk, Random random)
	{
		if (chunk.chunkX < 0)
		{
			int sideTop = getTop(chunk.world, chunk.worldPosX(16));

			for (int x = 15; x >= 0; --x)
			{
				final int top = sideTop + random.nextInt(3) - 1;
				generateColumn(chunk, random, x, top);
				sideTop = top;
			}
		}
		else
		{
			int sideTop = chunk.chunkX == 0 ? 64 : getTop(chunk.world, chunk.worldPosX(-1));

			for (int x = 0; x < 16; x++)
			{
				final int top = sideTop + random.nextInt(3) - 1;
				generateColumn(chunk, random, x, top);
				sideTop = top;
			}
		}
	}

	private static int getTop(World world, int x)
	{
		int height = world.getHeight(x);

		while (height >= 0)
		{
			final Block block = world.getBlock(x, height);
			if (block == Blocks.grass || block == Blocks.dirt)
			{
				return height;
			}

			--height;
		}
		return 0;
	}

	private static void generateColumn(Chunk chunk, Random random, int x, int top)
	{
		int stone = top - 4 - random.nextInt(3);

		for (int y = 0; y <= top; y++)
		{
			final Block block;

			if (y == top)
			{
				block = Blocks.grass;
			}
			else if (y > stone)
			{
				block = Blocks.dirt;
			}
			else
			{
				block = Blocks.stone;
			}

			chunk.setBlock(block, 0, x, y, 0);
		}
	}

	public static void generateStructures(World world, Random random, int x)
	{
		if (random.nextInt(2) == 0)
		{
			int tx = x + random.nextInt(Chunk.WIDTH);
			int ty = world.getHeight(tx);

			if (world.getBlock(tx, ty) == Blocks.grass)
			{
				HouseGenerator.generateHouse(world, random, tx, ty);
			}
		}

		for (int i = random.nextInt(4); i >= 0; i--)
		{
			int tx = x + random.nextInt(Chunk.WIDTH);
			int ty = world.getHeight(tx);

			if (world.getBlock(tx, ty) == Blocks.grass)
			{
				TreeGenerator.generateTree(world, random, tx, ty);
			}
		}

		for (int i = random.nextInt(3); i >= 0; --i)
		{
			final int mx = x + random.nextInt(Chunk.WIDTH);
			final int my = Math.max(world.getHeight(x), world.getHeight(x + 1));

			final EntityMonster monster = new EntityMonster(world);
			monster.setLocation(mx + 0.5, my + 2);
			world.spawnEntity(monster);
		}
	}
}
