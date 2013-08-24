package com.clashsoft.dungeonrun.world;

import java.util.*;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.entity.Entity;

public class World
{
	public String					name;
	private Chunk[][]				chunks			= new Chunk[64][64];
	private Map<Integer, Entity>	entitys			= new HashMap<Integer, Entity>();
	private List<Integer>			entitysToRemove	= new LinkedList<Integer>();
	
	public World(String name)
	{
		this.name = name;
		this.generateTerrain();
	}
	
	public BlockInWorld getBlock(int x, int y, int z)
	{
		if (x >= -1024 && x < 1024 && y >= 0 && y < 64 && z >= -1024 && z < 1024)
		{
			Chunk c = getChunkAtCoordinates(x, z);
			if (c != null)
			{
				return c.getBlock((x + 512), y, (z + 512));
			}
		}
		return new BlockInWorld(this, 0, 0);
	}
	
	public void setBlock(int block, int meta, int x, int y, int z)
	{
		setBlock(block, meta, x, y, z, 1);
	}
	
	public void setBlock(int block, int meta, int x, int y, int z, int flags)
	{
		Chunk c = getChunkAtCoordinates(x, z);
		if (c == null)
			c = setChunkAtCoordinates(x, z);
		c.setBlock(block, meta, (x + 512), y, (z + 512), flags);
	}
	
	public Chunk getChunkAtCoordinates(int x, int z)
	{
		return chunks[(int) ((x + 512) / 16F)][(int) ((z + 512) / 16F)];
	}
	
	public Chunk setChunkAtCoordinates(int x, int z)
	{
		Chunk c = new Chunk(this, (int) ((x + 512) / 16F), (int) ((z + 512) / 16F));
		chunks[c.chunkX][c.chunkZ] = c;
		return c;
	}
	
	public void spawnEntityInWorld(Entity e)
	{
		this.entitys.put(e.entityId, e);
	}
	
	public void removeEntity(int id)
	{
		this.entitys.remove(id);
	}
	
	public void updateWorld() throws SlickException
	{
		if (!DungeonRun.instance.isPaused)
		{
			for (Entity e : this.entitys.values())
			{
				e.updateEntity();
			}
			for (int i : this.entitysToRemove)
			{
				this.entitys.remove(i);
			}
			this.entitysToRemove.clear();
		}
	}
	
	public Collection<Entity> getEntitys()
	{
		return entitys.values();
	}
	
	public void generateTerrain()
	{
		for (int i = 0; i < 32; i++)
		{
			for (int j = -32; j <= 32; j++)
			{
				for (int k = -32; k <= 32; k++)
				{
					int block = i == 31 ? Block.grass.blockID : Block.dirt.blockID;
					this.setBlock(block, 0, j, i, k, 0);
				}
			}
		}
	}
	
	public float getLightValue(int x, int y, int z)
	{
		if (x >= -1024 && x < 1024 && y >= 0 && y < 64 && z >= -1024 && z < 1024)
		{
			Chunk c = getChunkAtCoordinates(x, z);
			if (c != null)
			{
				return c.getLightValue((x + 512), y, (z + 512));
			}
		}
		return 1F;
	}
	
	public void setLightValue(int x, int y, int z, float f)
	{
		if (x >= -1024 && x < 1024 && y >= 0 && y < 64 && z >= -1024 && z < 1024)
		{
			Chunk c = getChunkAtCoordinates(x, z);
			if (c != null)
			{
				c.setLightValue((x + 512), y, (z + 512), f);
			}
		}
	}
}
