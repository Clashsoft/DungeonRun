package com.clashsoft.dungeonrun.world;

import java.util.Arrays;

import com.clashsoft.dungeonrun.nbt.INBTSaveable;
import com.clashsoft.dungeonrun.nbt.NBTTagCompound;
import com.clashsoft.dungeonrun.nbt.NBTTagList;

public class Chunk implements INBTSaveable
{
	public final World			world;
	
	public int					chunkX;
	public int					chunkZ;
	
	private BlockInWorld[]		blocks;
	private Integer[]				blockIDs;
	private Integer[]				metadataValues;
	
	private Float[]				lightValues;
	private Integer[]				maxY;
	
	public Chunk(World w, int x, int y)
	{
		this(w, x, y, new BlockInWorld[index(16, 64, 16)]);
	}
	
	public Chunk(World w, int x, int y, BlockInWorld[] b)
	{
		this.world = w;
		this.chunkX = x;
		this.chunkZ = y;
		
		this.blocks = b;
		this.blockIDs = new Integer[blocks.length];
		this.metadataValues = new Integer[blocks.length];
		
		this.lightValues = new Float[blocks.length];
		this.maxY = new Integer[16 * 16];
		initializeLightValues(false);
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
		int i = index(x, y, z);
		if (blocks[i] == null)
			this.blocks[index(x, y, z)] = new BlockInWorld(world, blockId, metadata);
		
		blocks[i].world = world;
		blocks[i].blockID = blockIDs[i] = blockId;
		blocks[i].metadata = metadataValues[i] = metadata;
		
		float f = this.blocks[index(x, y, z)].getLightValue();
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
		return this.blocks[index(x, y, z)];
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
		if (maxY[index] != null && maxY[index] == y)
			return true;
		for (int i = y; i < 64; i++)
		{
			if (getBlock(x, i, z) != null && !getBlock(x, i, z).isAir())
				return false;
		}
		maxY[x << 4 | z] = y;
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
		return x << 12 | y << 4 | z;
	}
	
	protected int chunkPosToWorldPosX(int x)
	{
		return ((chunkX - (World.CHUNKS_X / 2)) * 16) + x;
	}
	
	protected int chunkPosToWorldPosZ(int z)
	{
		return ((chunkZ - (World.CHUNKS_Z / 2)) * 16) + z;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("ChunkX", this.chunkX);
		nbt.setInteger("ChunkZ", this.chunkZ);
		nbt.setTagList(NBTTagList.fromList("BlockIDs", Arrays.asList(blockIDs)));
		nbt.setTagList(NBTTagList.fromList("BlockMs", Arrays.asList(metadataValues)));
		//nbt.setTagList(NBTTagList.fromList("LightValues", Arrays.asList(lightValues)));
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.chunkX = nbt.getInteger("ChunkX");
		this.chunkZ = nbt.getInteger("ChunkZ");
		this.blockIDs = nbt.getTagList("BlockIDs").toArray(Integer.class);
		this.metadataValues = nbt.getTagList("BlockMs").toArray(Integer.class);
		
		//this.lightValues = nbt.getTagList("LightValues").toArray();
	}
}
