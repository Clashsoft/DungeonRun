package com.clashsoft.dungeonrun.world;

import com.clashsoft.nbt.NBTTagArray;
import com.clashsoft.nbt.NBTTagCompound;
import com.clashsoft.nbt.util.INBTSaveable;

public class Chunk implements INBTSaveable
{
	public final World	world;
	
	public int			chunkX;
	public int			chunkY;
	public int			chunkZ;
	
	private int[]		blockIDs;
	private int[]		metadataValues;
	
	private float[]		lightValues;
	
	public boolean		dummy;
	
	public Chunk(World w, int x, int y, int z)
	{
		this.world = w;
		this.chunkX = x;
		this.chunkY = y;
		this.chunkZ = z;
		
		int length = index(15, 15, 15) + 1;
		
		this.blockIDs = new int[length];
		this.metadataValues = new int[length];
		
		this.lightValues = new float[length];
		
		this.dummy = true;
	}
	
	protected void initializeLightValues(boolean flag)
	{
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 64; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					BlockInWorld block = this.world.getBlock(this.chunkPosToWorldPosX(i), j, this.chunkPosToWorldPosZ(k));
					this.lightValues[index(i, j, k)] = block != null ? block.getLightValue() : 0.1F;
				}
			}
		}
	}
	
	public void setBlock(int blockId, int metadata, int x, int y, int z, int flags)
	{
		x &= 15;
		z &= 15;
		int index = index(x, y, z);
		
		this.blockIDs[index] = blockId;
		this.metadataValues[index] = metadata;
		
		float f = this.getLightValue(x, y, z);
		if ((flags & 1) != 0)
		{
			this.updateLightValues(x, y, z, f);
		}
	}
	
	public void updateLightValues(int x, int y, int z, float f)
	{
		int var1 = 8;
		for (int i = x - 8; i <= x + 8; i++)
		{
			for (int j = y - var1 >= 0 ? y - var1 : 0; j <= (y + var1 < 64 ? y + var1 : 63); j++)
			{
				for (int k = z - 8; k <= z + 8; k++)
				{
					int x1 = i + this.chunkX * 16 - 512;
					int z1 = k + this.chunkZ * 16 - 512;
					
					int offX = Math.abs(i - x);
					int offY = Math.abs(j - y);
					int offZ = Math.abs(k - z);
					int offset = offX + offY + offZ;
					float f1 = offset * 0.1F;
					float f2 = f - f1 * 0.1F;
					float f3 = Math.max(this.world.getLightValue(x1, j, z1), f2);
					this.world.setLightValue(x1, j, z1, f3);
				}
			}
		}
	}
	
	public BlockInWorld getBlock(int x, int y, int z)
	{
		x &= 15;
		z &= 15;
		int index = index(x, y, z);
		return new BlockInWorld(this.world, this.getBlockID(index), this.getBlockMetadata(index));
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
		catch (Exception ex)
		{
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
		float f = this.lightValues[index(x, y, z)];
		return f;
	}
	
	public void setLightValue(int x, int y, int z, float f)
	{
		x &= 15;
		z &= 15;
		this.lightValues[index(x, y, z)] = f;
	}
	
	protected static int index(int x, int y, int z)
	{
		return x << 0 | y << 4 | z << 8;
	}
	
	protected int chunkPosToWorldPosX(int x)
	{
		return this.chunkX * 16 + x;
	}
	
	protected int chunkPosToWorldPosZ(int z)
	{
		return this.chunkZ * 16 + z;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("ChunkX", this.chunkX);
		nbt.setInteger("ChunkZ", this.chunkZ);
		nbt.setTagArray(new NBTTagArray("BlockIDs", this.blockIDs));
		nbt.setTagArray(new NBTTagArray("Metadata", this.metadataValues));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.chunkX = nbt.getInteger("ChunkX");
		this.chunkZ = nbt.getInteger("ChunkZ");
		this.blockIDs = nbt.getTagArray("BlockIDs").getIntArray();
		this.metadataValues = nbt.getTagArray("Metadata").getIntArray();
		this.dummy = false;
	}
	
	@Override
	public String toString()
	{
		return "Chunk[" + this.chunkX + ";" + this.chunkZ + "]";
	}
}
