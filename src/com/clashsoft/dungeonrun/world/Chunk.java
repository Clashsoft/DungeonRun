package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.nbt.tags.collection.NBTTagArray;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;
import com.clashsoft.nbt.util.INBTSaveable;

public class Chunk implements INBTSaveable
{
	public static final int UPDATE_NEIGHBORS = 1;
	public static final int UPDATE_LIGHT     = 2;
	public static final int UPDATE           = UPDATE_NEIGHBORS | UPDATE_LIGHT;

	public static final int WIDTH = 16;
	public static final int HEIGHT = 256;

	public final World world;

	public int chunkX;

	private int[] blockIDs;
	private int[] metadataValues;

	private float[] lightValues;
	private int[] heightMap;

	private boolean hasChanged;

	public Chunk(World w, int x)
	{
		this.world = w;
		this.chunkX = x;

		this.blockIDs = new int[WIDTH * HEIGHT];
		this.metadataValues = new int[WIDTH * HEIGHT];

		this.lightValues = new float[WIDTH * HEIGHT];
		this.heightMap = new int[WIDTH];
	}

	protected void initializeLightValues()
	{
		for (int x = 0; x < 16; x++)
		{
			for (int y = 0; y < 16; y++)
			{
				for (int z = 0; z < 16; z++)
				{
					BlockInWorld block = this.getBlock(x, y);
					this.lightValues[index(x, y)] = block != null ? block.getLightValue() : 0.1F;
				}
			}
		}
	}

	public void setBlock(Block block, int metadata, int x, int y, int flags)
	{
		int index = index(x, y);

		this.blockIDs[index] = block == null ? 0 : block.blockID;
		this.metadataValues[index] = metadata;

		// Update Light
		float f = this.getLightValue(x, y);
		if ((flags & UPDATE_LIGHT) != 0)
		{
			this.updateLightValues(x, y, f);
		}

		// Update Height Map
		this.updateHeightMap(block, x, y);

		this.hasChanged = true;
	}

	private void updateHeightMap(Block block, int x, int y)
	{
		if (block != null)
		{
			if (y > this.heightMap[x])
			{
				this.heightMap[x] = y;
			}
			return;
		}

		if (this.heightMap[x] != y)
		{
			return;
		}

		// Find the highest non-air block
		for (int y1 = y - 1; y1 >= 0; y1--)
		{
			if (this.blockIDs[index(x, y1)] != 0)
			{
				this.heightMap[x] = y;
				break;
			}
		}
	}

	public void setLightValue(int x, int y, float f)
	{
		this.lightValues[index(x, y)] = f;

		this.hasChanged = true;
	}

	public void updateLightValues(int x, int y, float newValue)
	{
		for (int dx = -12; dx <= +12; dx++)
		{
			for (int dy = -12; dy <= 12; dy++)
			{
				final int x1 = this.worldPosX(x + dx);
				final int y1 = y + dy;

				final int offset = Math.abs(dx) + Math.abs(dy);

				float localLight = this.world.getLightValue(x1, y1);
				float scaledOffset = offset / 16F;
				float offsetLight = newValue - scaledOffset;
				float maxLight = Math.max(localLight, offsetLight);

				this.world.setLightValue(x1, y1, maxLight);
			}
		}
	}

	public int worldPosX(int x)
	{
		return this.chunkX << 4 | x;
	}

	public BlockInWorld getBlock(int x, int y)
	{
		int index = index(x, y);
		return new BlockInWorld(this.world, this.getBlockID(index), this.getBlockMetadata(index),
		                        this.getLightValue(index));
	}

	public int getBlockID(int x, int y)
	{
		return this.getBlockID(index(x, y));
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

	public int getBlockMetadata(int x, int y)
	{
		return this.getBlockMetadata(index(x, y));
	}

	protected int getBlockMetadata(int index)
	{
		return this.metadataValues[index];
	}

	public float getLightValue(int x, int y)
	{
		return this.getLightValue(index(x, y));
	}

	protected float getLightValue(int index)
	{
		return this.lightValues[index];
	}

	public int getHeight(int x)
	{
		return this.heightMap[x];
	}

	protected static int index(int x, int y)
	{
		return x << 0 | y << 4;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("x", this.chunkX);
		nbt.setTagArray(new NBTTagArray("ids", this.blockIDs));
		nbt.setTagArray(new NBTTagArray("data", this.metadataValues));
		nbt.setTagArray(new NBTTagArray("heightMap", this.heightMap));
		nbt.setTagArray(new NBTTagArray("lightValues", this.lightValues));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.chunkX = nbt.getInteger("x");
		this.blockIDs = nbt.getTagArray("ids").getIntArray();
		this.metadataValues = nbt.getTagArray("data").getIntArray();
		this.heightMap = nbt.getTagArray("heightMap").getIntArray();
		this.lightValues = nbt.getTagArray("lightValues").getFloatArray();
	}

	@Override
	public String toString()
	{
		return "Chunk[" + this.chunkX + "]";
	}

	public boolean isDirty()
	{
		return this.hasChanged;
	}

	public void markDirty()
	{
		this.hasChanged = true;
	}

	public void markClean()
	{
		this.hasChanged = false;
	}
}
