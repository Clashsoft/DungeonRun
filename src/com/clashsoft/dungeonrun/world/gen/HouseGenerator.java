package com.clashsoft.dungeonrun.world.gen;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.block.Blocks;
import com.clashsoft.dungeonrun.entity.EntityPotster;
import com.clashsoft.dungeonrun.world.ForegroundBlock;
import com.clashsoft.dungeonrun.world.World;

import java.util.Random;

public class HouseGenerator
{
	public static void generateHouse(World world, Random random, int x, int y)
	{
		generateHouse(world, random, x, y, 5 + random.nextInt(10));
	}

	public static void generateHouse(World world, Random random, int x, int y, int width)
	{
		final int halfWidth = width / 2;
		final int height = 4;

		final int groundFloors = random.nextInt(4);
		final int floors = 1 + random.nextInt(3) + groundFloors;

		y -= groundFloors * height;
		if (y < 0)
		{
			return;
		}

		// Find out where to generate the entrances
		int leftEntrance = 0;
		int rightEntrance = 0;
		for (int i = floors - 1; i >= 0; i--)
		{
			if (!world.getBlock(x - halfWidth - 1, y + height * i + 1).isSolid())
			{
				leftEntrance = i;
			}
			if (!world.getBlock(x + halfWidth + 1, y + height * i + 1).isSolid())
			{
				rightEntrance = i;
			}
		}

		generateRoof(world, x, y + height * floors, halfWidth);

		// Floors
		for (int i = 0; i < floors; i++)
		{
			generateFloor(world, random, x, y + height * i, halfWidth, height, i < leftEntrance && i < rightEntrance);
		}

		generateEntrance(world, x - halfWidth, y + height * leftEntrance + 1, -1);
		generateEntrance(world, x + halfWidth, y + height * rightEntrance + 1, 1);

		// Generate ladders and chests

		for (int i = 0; i < floors; i++)
		{
			final int dir = random.nextBoolean() ? 1 : -1;
			final int off = dir * random.nextInt(halfWidth - 1);
			final int ladder = x + off;

			// Ladders
			for (int l = 0; l <= height; l++)
			{
				world.addForegroundBlock(new ForegroundBlock(ladder, y + i * height + l + 1, Blocks.ladder, 0));
			}

			// Chest
			if (random.nextInt(4) == 0)
			{
				Block block = i < leftEntrance && i < rightEntrance ? Blocks.stoneChest : Blocks.woodChest;
				world.addForegroundBlock(new ForegroundBlock(x + dir * (halfWidth - 1), y + i * height + 1, block, 0));
			}
		}

		// Generate dirt blocks below the house
		for (int i = -halfWidth; i <= halfWidth; i++)
		{
			int top = y - 1;
			while (top >= 0)
			{
				Block block = world.getBlock(x + i, top);
				if (block == Blocks.air || block == Blocks.grass)
				{
					world.setBlock(Blocks.dirt, 0, x + i, top);
					top--;
				}
				else
				{
					break;
				}
			}
		}
	}

	private static void generateEntrance(World world, int x, int y, int off)
	{
		if (world.getBlock(x + off, y).isSolid())
		{
			world.setBlock(Blocks.air, 0, x + off, y);
		}

		world.setBlock(Blocks.plankWall, 0, x, y);
		world.setBlock(Blocks.plankWall, 0, x, y + 1);
	}

	private static void generateFloor(World world, Random random, int x, int y, int width, int height, boolean stone)
	{
		final Block solid = !stone ? Blocks.planks : Blocks.cobbleStone;
		final Block wall = !stone ? Blocks.plankWall : Blocks.cobbleStoneWall;

		for (int i = -width; i <= width; i++)
		{
			world.setBlock(solid, 0, x + i, y);

			final Block wall1 = i == -width || i == width ? solid : wall;

			// Background walls
			for (int j = 1; j < height; j++)
			{
				world.setBlock(wall1, 0, x + i, y + j);
			}
		}

		final int rand = random.nextInt(2) - 1;
		if (rand != 0)
		{
			final EntityPotster potster = new EntityPotster(world);
			potster.setLocation(x + (width - 1) * rand + 0.5, y + 1);
			world.spawnEntity(potster);
		}
	}

	private static void generateRoof(World world, int x, int y, int width)
	{
		for (int i = -width; i <= width; i++)
		{
			int roofHeight = width - Math.abs(i) + 1;

			for (int j = 1; j < roofHeight; j++)
			{
				world.setBlock(Blocks.cobbleStoneWall, 0, x + i, y + j);
			}
			world.setBlock(Blocks.planks, 0, x + i, y);
			world.setBlock(Blocks.cobbleStone, 0, x + i, y + roofHeight);
		}

		world.setBlock(Blocks.cobbleStone, 0, x - width - 1, y);
		world.setBlock(Blocks.cobbleStone, 0, x + width + 1, y);
	}
}
