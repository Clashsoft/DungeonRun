package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.nbt.INBTSaveable;
import com.clashsoft.dungeonrun.nbt.NBTTagCompound;
import com.clashsoft.dungeonrun.nbt.NBTTagList;
import com.clashsoft.dungeonrun.util.ArrayConverter;

public class Chunk implements INBTSaveable
{
	public final World		world;
	
	public int				chunkX;
	public int				chunkZ;
	
	private int[]			blockIDs;
	private int[]			metadataValues;
	
	private float[]			lightValues;
	private int[]			maxY;
	
	public boolean dummy;
	
	public Chunk(World w, int x, int y)
	{
		this.world = w;
		this.chunkX = x;
		this.chunkZ = y;
		
		int length = index(15, 63, 15) + 1;
		
		this.blockIDs = new int[length];
		this.metadataValues = new int[length];
		
		this.lightValues = new float[length];
		this.maxY = new int[16 * 16];
		
		this.dummy = true;
	}
	
	protected Chunk generate()
	{
		for (int x = 0; x < 16; ++x)
		{
			for (int y = 0; y < 32; ++y)
			{
				for (int z = 0; z < 16; ++z)
				{
					int blockID = 0;
					if (y == 31)
						blockID = Block.grass.blockID;
					else if (y < 31 && y >= 27)
						blockID = Block.dirt.blockID;
					else if (y < 27)
						blockID = Block.stone.blockID;
					this.setBlock(blockID, 0, x, y, z, 0);
				}
			}
		}
		this.dummy = false;
		return this;
	}
	
	protected void initializeLightValues(boolean flag)
	{
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 64; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					BlockInWorld block = world.getBlock(chunkPosToWorldPosX(i), j, chunkPosToWorldPosZ(k));
					lightValues[index(i, j, k)] = (block != null ? block.getLightValue() : 0.1F);
				}
			}
		}
	}
	
	public void setBlock(int blockId, int metadata, int x, int y, int z, int flags)
	{
		x &= 15;
		z &= 15;
		int index = index(x, y, z);
		
		blockIDs[index] = blockId;
		metadataValues[index] = metadata;
		
		float f = this.getLightValue(x, y, z);
		if ((flags & 1) != 0)
		{
			updateLightValues(x, y, z, f);
		}
	}
	
	public void updateLightValues(int x, int y, int z, float f)
	{
		int var1 = 8;
		for (int i = x - 8; i <= x + 8; i++)
		{
			for (int j = (y - var1 >= 0 ? y - var1 : 0); j <= (y + var1 < 64 ? y + var1 : 63); j++)
			{
				for (int k = z - 8; k <= z + 8; k++)
				{
					int x1 = i + (chunkX * 16) - 512;
					int z1 = k + (chunkZ * 16) - 512;
					
					int offX = Math.abs(i - x);
					int offY = Math.abs(j - y);
					int offZ = Math.abs(k - z);
					int offset = offX + offY + offZ;
					float f1 = offset * 0.1F;
					float f2 = f - f1 * 0.1F;
					float f3 = Math.max(world.getLightValue(x1, j, z1), f2);
					world.setLightValue(x1, j, z1, f3);
				}
			}
		}
	}
	
	public BlockInWorld getBlock(int x, int y, int z)
	{
		x &= 15;
		z &= 15;
		int index = index(x, y, z);
		return new BlockInWorld(this.world, getBlockID(index), getBlockMetadata(index));
	}
	
	public int getBlockID(int x, int y, int z)
	{
		x &= 15;
		z &= 15;
		return this.getBlockID(index(x, y, z));
	}
	
	protected int getBlockID(int index)
	{
		try
		{
			return this.blockIDs[index];
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}
	
	public int getBlockMetadata(int x, int y, int z)
	{
		x &= 15;
		z &= 15;
		return this.getBlockMetadata(index(x, y, z));
	}
	
	protected int getBlockMetadata(int index)
	{
		return this.metadataValues[index];
	}
	
	public float getLightValue(int x, int y, int z)
	{
		x &= 15;
		z &= 15;
		float f = lightValues[index(x, y, z)];
		return canBlockSeeSky(x, y, z) ? 1F : f;
	}
	
	private boolean canBlockSeeSky(int x, int y, int z)
	{
		x &= 15;
		z &= 15;
		int index = x << 4 | z;
		if (maxY[index] == y)
			return true;
		for (int i = y; i < 64; i++)
		{
			if (getBlock(x, i, z) != null && !getBlock(x, i, z).isAir())
				return false;
		}
		maxY[index] = y;
		return true;
	}
	
	public void setLightValue(int x, int y, int z, float f)
	{
		x &= 15;
		z &= 15;
		lightValues[index(x, y, z)] = f;
	}
	
	protected static int index(int x, int y, int z)
	{
		return (x << 6 | y) << 4 | z;
	}
	
	protected int chunkPosToWorldPosX(int x)
	{
		return (chunkX * 16) + x;
	}
	
	protected int chunkPosToWorldPosZ(int z)
	{
		return (chunkZ * 16) + z;
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("ChunkX", this.chunkX);
		nbt.setInteger("ChunkZ", this.chunkZ);
		nbt.setTagList(NBTTagList.fromArray("BlockIDs", ArrayConverter.convertIntArray(blockIDs)));
		nbt.setTagList(NBTTagList.fromArray("BlockMs", ArrayConverter.convertIntArray(metadataValues)));
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.chunkX = nbt.getInteger("ChunkX");
		this.chunkZ = nbt.getInteger("ChunkZ");
		this.blockIDs = nbt.getTagList("BlockIDs").toIntArray();
		this.metadataValues = nbt.getTagList("BlockMs").toIntArray();
		this.dummy = false;
	}
}
