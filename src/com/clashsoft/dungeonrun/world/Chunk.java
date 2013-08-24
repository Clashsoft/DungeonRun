package com.clashsoft.dungeonrun.world;

public class Chunk
{
	public final World			world;
	public final int			chunkX;
	public final int			chunkZ;
	private BlockInWorld[][][]	blocks;
	private float[][][]			lightValues;
	private int[][]				maxY;
	
	public Chunk(World w, int x, int y)
	{
		this(w, x, y, new BlockInWorld[16][64][16]);
	}
	
	public Chunk(World w, int x, int y, BlockInWorld[][][] b)
	{
		this.blocks = b;
		this.world = w;
		this.chunkX = x;
		this.chunkZ = y;
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
		x %= 16;
		z %= 16;
		return this.blocks[x][y][z];
	}
	
	public float getLightValue(int x, int y, int z)
	{
		x %= 16;
		z %= 16;
		float f = lightValues[x][y][z];
		return canBlockSeeSky(x, y, z) ? 1F : f;
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
