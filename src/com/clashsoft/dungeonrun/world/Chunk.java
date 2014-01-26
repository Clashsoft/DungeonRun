package com.clashsoft.dungeonrun.world;

import com.clashsoft.nbt.NBTTagArray;
import com.clashsoft.nbt.NBTTagCompound;
import com.clashsoft.nbt.util.INBTSaveable;

public class Chunk implements INBTSaveable
{
	public static int	NO_UPDATE			= 0;
	public static int	UPDATE_NEIGHBORS	= 1;
	public static int	UPDATE_LIGHT		= 2;
	public static int	UPDATE				= UPDATE_NEIGHBORS | UPDATE_LIGHT;
	
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
		
		this.blockIDs = new int[4096];
		this.metadataValues = new int[4096];
		
		this.lightValues = new float[4096];
		
		this.dummy = true;
	}
	
	protected void initializeLightValues()
	{
		for (int x = 0; x < 16; x++)
		{
			for (int y = 0; y < 16; y++)
			{
				for (int z = 0; z < 16; z++)
				{
					BlockInWorld block = this.getBlock(x, y, z);
					this.lightValues[index(x, y, z)] = block != null ? block.getLightValue() : 0.1F;
				}
			}
		}
	}
	
	/**
	 * Sets the block at the position.
	 * <p>
	 * Flags:
	 * <p>
	 * 1: update neighbor blocks
	 * <p>
	 * 2: update light values
	 * <p>
	 * 
	 * @param blockID
	 *            the block id
	 * @param metadata
	 *            the block metadata
	 * @param x
	 *            x coord
	 * @param y
	 *            y coord
	 * @param z
	 *            z coord
	 * @param flags
	 *            placement args
	 */
	public void setBlock(int blockID, int metadata, int x, int y, int z, int flags)
	{
		int index = index(x, y, z);
		
		this.blockIDs[index] = blockID;
		this.metadataValues[index] = metadata;
		
		float f = this.getLightValue(x, y, z);
		if ((flags & 2) != 0)
		{
			this.updateLightValues(x, y, z, f);
		}
	}
	
	public void updateLightValues(int x, int y, int z, float f)
	{
		int var1 = 8;
		for (int i = x - 12; i <= x + 12; i++)
		{
			for (int j = y - 12; j <= y + 12; j++)
			{
				for (int k = z - 12; k <= z + 12; k++)
				{
					int x1 = this.worldPosX(i);
					int y1 = this.worldPosY(j);
					int z1 = this.worldPosZ(k);
					
					int offX = Math.abs(i - x);
					int offY = Math.abs(j - y);
					int offZ = Math.abs(k - z);
					int offset = offX + offY + offZ;
					
					float f0 = this.world.getLightValue(x1, y1, z1);
					float f1 = offset / 16F;
					float f2 = f - f1;
					float f3 = Math.max(f0, f2);
					this.world.setLightValue(x1, y1, z1, f3);
				}
			}
		}
	}
	
	public int worldPosX(int x)
	{
		return this.chunkX << 4 | x;
	}
	
	public int worldPosY(int y)
	{
		return this.chunkY << 4 | y;
	}
	
	public int worldPosZ(int z)
	{
		return this.chunkZ << 4 | z;
	}
	
	public BlockInWorld getBlock(int x, int y, int z)
	{
		int index = index(x, y, z);
		return new BlockInWorld(this.world, this.getBlockID(index), this.getBlockMetadata(index), this.getLightValue(index));
	}
	
	public int getBlockID(int x, int y, int z)
	{
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
		return this.getBlockMetadata(index(x, y, z));
	}
	
	protected int getBlockMetadata(int index)
	{
		return this.metadataValues[index];
	}
	
	public float getLightValue(int x, int y, int z)
	{
		return this.getLightValue(index(x, y, z));
	}
	
	protected float getLightValue(int index)
	{
		return 1F; // this.lightValues[index];
	}
	
	public void setLightValue(int x, int y, int z, float f)
	{
		this.lightValues[index(x, y, z)] = f;
	}
	
	protected static int index(int x, int y, int z)
	{
		return x << 0 | y << 4 | z << 8;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("x", this.chunkX);
		nbt.setInteger("y", this.chunkY);
		nbt.setInteger("z", this.chunkZ);
		nbt.setTagArray(new NBTTagArray("ids", this.blockIDs));
		nbt.setTagArray(new NBTTagArray("data", this.metadataValues));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.chunkX = nbt.getInteger("x");
		this.chunkY = nbt.getInteger("y");
		this.chunkZ = nbt.getInteger("z");
		this.blockIDs = nbt.getTagArray("ids").getIntArray();
		this.metadataValues = nbt.getTagArray("data").getIntArray();
		this.dummy = false;
	}
	
	@Override
	public String toString()
	{
		return "Chunk[" + this.chunkX + ";" + this.chunkY + ";" + this.chunkZ + "]";
	}
}
