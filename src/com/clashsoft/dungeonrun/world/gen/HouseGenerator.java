package com.clashsoft.dungeonrun.world.gen;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.block.Blocks;
import com.clashsoft.dungeonrun.world.World;

import java.util.Random;

public class HouseGenerator
{
	public static void generateHouse(World world, Random random, int x, int y)
	{
		int width = (5 + random.nextInt(5)) / 2;
		int height = 4;
		int floors = 1 + random.nextInt(3);

		generateRoof(world, x, y + height * floors, width);

		// Floors
		for (int i = 0; i < floors; i++)
		{
			generateFloor(world, x, y + height * i, width, height);
		}

		boolean leftEntrance = false;
		boolean rightEntrance = false;

		for (int i = 0; ; i++)
		{
			if (!leftEntrance && generateEntrance(world, x - width, y + height * i + 1, -1))
			{
				leftEntrance = true;
			}
			if (!rightEntrance && generateEntrance(world, x + width, y + height * i + 1, 1))
			{
				rightEntrance = true;
			}

			if (i + 1 >= floors)
			{
				break;
			}

			final int off = random.nextInt(width);
			final int ladder = x + (random.nextBoolean() ? off : -off);
			for (int l = 0; l <= height; l++)
			{
				world.setBlock(Blocks.plankLadder, 0, ladder, y + i * height + l + 1);
			}
		}

		// Generate dirt blocks below the house
		for (int i = -width; i <= width; i++)
		{
			int top = y - 1;
			while (top >= 0 && world.getBlock(x + i, top) != Blocks.dirt)
			{
				world.setBlock(Blocks.dirt, 0, x + i, top);
				top--;
			}
		}
	}

	private static boolean generateEntrance(World world, int x, int y, int off)
	{
		if (world.getBlock(x + off, y + 1).isSolid())
		{
			return false;
		}

		if (world.getBlock(x + off, y).isSolid())
		{
			world.setBlock(Blocks.air, 0, x + off, y);
		}

		world.setBlock(Blocks.plankWall, 0, x, y);
		world.setBlock(Blocks.plankWall, 0, x, y + 1);
		return true;
	}

	private static void generateFloor(World world, int x, int y, int width, int height)
	{
		for (int i = -width; i <= width; i++)
		{
			world.setBlock(Blocks.planks, 0, x + i, y);

			Block wall = i == -width || i == width ? Blocks.planks : Blocks.plankWall;

			// Background walls
			for (int j = 1; j < height; j++)
			{
				world.setBlock(wall, 0, x + i, y + j);
			}
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
