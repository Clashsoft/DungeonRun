package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.entity.EntityMonster;

import java.util.Random;

public class WorldGenerator
{
	public static void generateChunk(Chunk chunk, Random random)
	{
		if (chunk.chunkX < 0)
		{
			int sideTop = chunk.world.getHeight(chunk.worldPosX(16));

			for (int x = 15; x >= 0; --x)
			{
				final int top = sideTop + random.nextInt(3) - 1;
				generateColumn(chunk, random, x, top);
				sideTop = top;
			}
		}
		else
		{
			int sideTop = chunk.chunkX == 0 ? 64 : chunk.world.getHeight(chunk.worldPosX(-1));

			for (int x = 0; x < 16; x++)
			{
				final int top = sideTop + random.nextInt(3) - 1;
				generateColumn(chunk, random, x, top);
				sideTop = top;
			}
		}
	}

	private static void generateColumn(Chunk chunk, Random random, int x, int top)
	{
		int stone = top - 4 - random.nextInt(3);

		for (int y = 0; y <= top; y++)
		{
			final Block block;

			if (y == top)
			{
				block = Block.grass;
			}
			else if (y > stone)
			{
				block = Block.dirt;
			}
			else
			{
				block = Block.stone;
			}

			chunk.setBlock(block, 0, x, y, 0);
		}
	}

	public static void generateStructures(World world, Random random, int x)
	{
		if (random.nextInt(5) == 0)
		{
			int tx = x + random.nextInt(Chunk.WIDTH);
			int ty = world.getHeight(tx);

			if (world.getBlock(tx, ty) == Block.grass)
			{
				generateHouse(world, random, tx, ty);
			}
		}

		for (int i = random.nextInt(4); i >= 0; i--)
		{
			int tx = x + random.nextInt(Chunk.WIDTH);
			int ty = world.getHeight(tx);

			if (world.getBlock(tx, ty) == Block.grass)
			{
				generateTree(world, random, tx, ty);
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

	private static void generateTree(World world, Random random, int x, int y)
	{
		int height = random.nextInt(3) + 3;
		for (int i = 1; i <= height; i++)
		{
			world.setBlock(Block.log, 0, x, y + i);
		}

		world.setBlock(Block.leaves, 0, x - 1, y + height);
		world.setBlock(Block.leaves, 0, x + 1, y + height);
		world.setBlock(Block.leaves, 0, x, y + height + 1);
	}

	private static void generateHouse(World world, Random random, int x, int y)
	{
		int width = (5 + random.nextInt(5)) / 2;
		int height = 3;

		for (int i = -width; i <= width; i++)
		{
			// Top and bottom floor
			world.setBlock(Block.planks, 0, x + i, y);
			world.setBlock(Block.planks, 0, x + i, y + height + 1);

			// Roof
			int roofHeight = width - Math.abs(i) + 2;
			for (int j = 2; j < roofHeight; j++)
			{
				world.setBlock(Block.cobbleStone, 0, x + i, y + height + j);
			}
			world.setBlock(Block.brick, 0, x + i, y + height + roofHeight);

			// Background walls
			for (int j = 1; j <= height; j++)
			{
				world.setBlock(Block.planksWall, 0, x + i, y + j);
			}

			// Generate dirt blocks below the house
			int top = y - 1;
			while (top >= 0 && world.getBlock(x + i, top) != Block.dirt)
			{
				world.setBlock(Block.dirt, 0, x + i, top);
				top--;
			}
		}

		world.setBlock(Block.brick, 0, x - width - 1, y + height + 1);
		world.setBlock(Block.brick, 0, x + width + 1, y + height + 1);
	}
}
