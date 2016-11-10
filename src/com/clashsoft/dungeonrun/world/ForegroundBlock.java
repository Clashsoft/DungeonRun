package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;
import dyvil.tools.nbt.collection.NBTMap;

public class ForegroundBlock
{
	public final int x;
	public final int y;

	public final Block block;
	public       int   metadata;

	public ForegroundBlock(int x, int y, Block block, int metadata)
	{
		this.x = x;
		this.y = y;
		this.block = block;
		this.metadata = metadata;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof ForegroundBlock))
		{
			return false;
		}

		ForegroundBlock that = (ForegroundBlock) o;

		return this.x == that.x && this.y == that.y && this.metadata == that.metadata && this.block == that.block;
	}

	@Override
	public int hashCode()
	{
		int result = this.x;
		result = 31 * result + this.y;
		result = 31 * result + this.block.hashCode();
		result = 31 * result + this.metadata;
		return result;
	}

	public void writeToNBT(NBTMap nbt, World world)
	{
		nbt.setInteger("x", this.x);
		nbt.setInteger("y", this.y);
		nbt.setInteger("id", world.getBlockID(this.block));

		if (this.metadata != 0)
		{
			nbt.setInteger("metadata", this.metadata);
		}
	}

	public static ForegroundBlock readFromNBT(NBTMap nbt, World world)
	{
		int x = nbt.getInteger("x");
		int y = nbt.getInteger("y");
		int id = nbt.getInteger("id");
		int metadata = nbt.getInteger("metadata"); // default to 0
		return new ForegroundBlock(x, y, world.getBlockByID(id), metadata);
	}
}
