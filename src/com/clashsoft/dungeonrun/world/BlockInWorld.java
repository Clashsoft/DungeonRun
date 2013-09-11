package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;

public class BlockInWorld
{
	public World	world;
	public int		blockID;
	public int		metadata;
	public int		x, y, z;
	
	public BlockInWorld(World w, int id, int metadata)
	{
		this.blockID = id;
		this.metadata = metadata;
		this.world = w;
	}
	
	public BlockInWorld(World w, Block block, int metadata)
	{
		this(w, block.blockID, metadata);
	}
	
	public Block getBlock()
	{
		return Block.blocksList[blockID];
	}
	
	public int getMetadata()
	{
		return metadata;
	}
	
	public boolean isAir()
	{
		return blockID == 0;
	}
	
	public float getLightValue()
	{
		float f = getBlock() != null ? getBlock().getLightValue() : 0.1F;
		float f1 = world.getLightValue(x + 1, y, z);
		float f2 = world.getLightValue(x - 1, y, z);
		float f3 = world.getLightValue(x, y + 1, z);
		float f4 = world.getLightValue(x, y - 1, z);
		float f5 = world.getLightValue(x, y, z + 1);
		float f6 = world.getLightValue(x, y, z - 1);
		float f7 = (f1 + f2 + f3 + f4 + f5 + f6) / 6F;
		return f7;
	}
}
