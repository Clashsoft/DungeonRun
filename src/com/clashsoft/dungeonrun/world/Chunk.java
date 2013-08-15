package com.clashsoft.dungeonrun.world;

public class Chunk
{
	public final World world;
	private final int chunkX;
	private final int chunkY;
	private BlockInWorld[][][] blocks;
	private float[][][] lightValues;
	private int[][] maxY;
	
	public Chunk(World w, int x, int y)
	{
		this(w, x, y, new BlockInWorld[16][64][16]);
	}
	
	public Chunk(World w, int x, int y, BlockInWorld[][][] b)
	{
		this.blocks = b;
		this.world = w;
		this.chunkX = x;
		this.chunkY = y;
		this.lightValues = new float[16][64][16];
		this.maxY = new int[16][16];
		initializeLightValues(false);
	}
	
	protected void initializeLightValues(boolean flag)
	{
		for (int i = 0; i < blocks.length; i++)
		{
			for (int j = 0; j < blocks[i].length; j++)
			{
				for (int k = 0; k < blocks[i][j].length; k++)
				{
					BlockInWorld block = world.getBlock(i, j, k);
					lightValues[i][j][k] = (block != null ? block.getLightValue() : 0.1F);
				}
			}
		}
	}
	
	public void setBlock(int blockId, int meta, int x, int y, int z, int flags)
	{
		x %= 16;
		z %= 16;
		this.blocks[x][y][z] = new BlockInWorld(world, blockId, meta);
		float f = this.blocks[x][y][z].getLightValue();
		
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
					world.setLightValue(i, j, k, f);
				}
			}
		}
	}
	
	public BlockInWorld getBlock(int x, int y, int z)
	{
		x %= 16;
		z %= 16;
		return this.blocks[x][y][z];
	}

	public float getLightValue(int x, int y, int z)
	{
		x %= 16;
		z %= 16;
		return canBlockSeeSky(x, y, z) ? 1F : lightValues[x][y][z];
	}

	private boolean canBlockSeeSky(int x, int y, int z)
	{
		x %= 16;
		z %= 16;
		if (maxY[x][z] == y)
			return true;
		for (int i = y; i < 64; i++)
		{
			if (getBlock(x, i, z) != null && !getBlock(x, i, z).isAir())
				return false;
		}
		maxY[x][z] = y;
		return true;
	}

	public void setLightValue(int x, int y, int z, float f)
	{
		x %= 16;
		z %= 16;
		lightValues[x][y][z] = f;
	}
}
