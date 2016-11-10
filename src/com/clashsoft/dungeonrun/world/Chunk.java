package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;
import dyvil.tools.nbt.NamedBinaryTag;
import dyvil.tools.nbt.collection.NBTArray;
import dyvil.tools.nbt.collection.NBTList;
import dyvil.tools.nbt.collection.NBTMap;
import dyvil.tools.nbt.util.INBTSaveable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Chunk implements INBTSaveable
{
	public static final int UPDATE_NEIGHBORS = 1;
	public static final int UPDATE_LIGHT     = 2;
	public static final int UPDATE_HEIGHT    = 4;
	public static final int UPDATE           = UPDATE_NEIGHBORS | UPDATE_LIGHT | UPDATE_HEIGHT;

	public static final int WIDTH  = 16;
	public static final int HEIGHT = 256;

	public final World world;

	public int chunkX;

	private int[] blockIDs;
	private int[] metadataValues;

	private float[] lightValues;
	private int[]   heightMap;

	private List<ForegroundBlock> foregroundBlocks = new ArrayList<>();

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

	public Collection<ForegroundBlock> getForegroundBlocks()
	{
		return this.foregroundBlocks;
	}

	protected void initLightAndHeightMap()
	{
		for (int x = 0; x < WIDTH; x++)
		{
			for (int y = HEIGHT - 1; y >= 0; y--)
			{
				final Block block = this.getBlock(x, y);

				if (block != null && block.isSolid())
				{
					this.heightMap[x] = y;
					break;
				}
			}
		}
	}

	public void setBlock(Block block, int metadata, int x, int y, int flags)
	{
		int index = index(x, y);

		this.blockIDs[index] = this.world.getBlockID(block);
		this.metadataValues[index] = metadata;

		// Update Light
		if ((flags & UPDATE_LIGHT) != 0)
		{
			this.updateLightValues(x, y, this.getLightValue(x, y));
		}

		// Update Height Map
		if ((flags & UPDATE_HEIGHT) != 0)
		{
			this.updateHeightMap(block, x, y);
		}

		this.hasChanged = true;
	}

	private void updateHeightMap(Block block, int x, int y)
	{
		if (block != null && block.isSolid())
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
		final int startX = x - 12;
		final int endX = x + 12;
		final int startY = Math.max(0, y - 12);
		final int endY = Math.min(y + 12, HEIGHT - 1);

		for (int x1 = startX; x1 <= endX; x1++)
		{
			for (int y1 = startY; y1 <= endY; y1++)
			{
				final int offset = Math.abs(x1 - x) + Math.abs(y1 - y);

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
		return (this.chunkX << 4) + x;
	}

	public Block getBlock(int x, int y)
	{
		return this.world.getBlockByID(this.getBlockID(x, y));
	}

	public int getBlockID(int x, int y)
	{
		if (y < 0 || y >= HEIGHT)
		{
			return 0;
		}

		return this.blockIDs[index(x, y)];
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
		return x & 15 | y << 4;
	}

	@Override
	public void writeToNBT(NBTMap nbt)
	{
		nbt.setInteger("x", this.chunkX);
		nbt.setTag("ids", new NBTArray(this.blockIDs));
		nbt.setTag("data", new NBTArray(this.metadataValues));
		nbt.setTag("heightMap", new NBTArray(this.heightMap));
		nbt.setTag("lightValues", new NBTArray(this.lightValues));

		if (!this.foregroundBlocks.isEmpty())
		{
			final NBTList foregroundBlocks = new NBTList();

			for (ForegroundBlock block : this.foregroundBlocks)
			{
				final NBTMap blockNBT = new NBTMap();
				block.writeToNBT(blockNBT, this.world);
				foregroundBlocks.addTag(blockNBT);
			}

			nbt.setTag("foregroundBlocks", foregroundBlocks);
		}
	}

	@Override
	public void readFromNBT(NBTMap nbt)
	{
		this.chunkX = nbt.getInteger("x");
		this.blockIDs = nbt.getTagArray("ids").getIntArray();
		this.metadataValues = nbt.getTagArray("data").getIntArray();
		this.heightMap = nbt.getTagArray("heightMap").getIntArray();
		this.lightValues = nbt.getTagArray("lightValues").getFloatArray();

		final NBTList foregroundBlocks = nbt.getTagList("foregroundBlocks");

		if (foregroundBlocks != null)
		{
			for (int i = 0; i < foregroundBlocks.size(); i++)
			{
				final NamedBinaryTag tag = foregroundBlocks.getTag(i);
				if (tag instanceof NBTMap)
				{
					this.foregroundBlocks.add(ForegroundBlock.readFromNBT((NBTMap) tag, this.world));
				}
			}
		}
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
