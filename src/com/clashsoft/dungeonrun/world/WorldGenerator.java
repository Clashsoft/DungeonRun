package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;

public class WorldGenerator
{
	public static void generate(World world)
	{
		
	}
	
	public static void generateChunk(Chunk chunk)
	{
		int cy = chunk.chunkY;
		for (int y = 0; y < 16; y++)
		{
			int i = cy + y;
			int blockID = Block.stone.blockID;
			if (i == 64)
			{
				blockID = Block.grass.blockID;
			}
			else if (i > 48)
			{
				blockID = Block.dirt.blockID;
			}
			
			for (int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 16; z++)
				{
					chunk.setBlock(blockID, 0, x, y, z, 0);
				}
			}
		}
		chunk.initializeLightValues();
	}
}
