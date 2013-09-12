package com.clashsoft.dungeonrun.world;

import java.io.File;
import java.util.*;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.entity.EntityList;
import com.clashsoft.dungeonrun.nbt.NBTBase;
import com.clashsoft.dungeonrun.nbt.NBTTagCompound;
import com.clashsoft.dungeonrun.nbt.NBTTagList;

public class World
{
	public static final int			WORLDSIZE_X = 1024;
	public static final int			WORLDSIZE_Z = 1024;
	public static final int			CHUNKS_X = WORLDSIZE_X / 16;
	public static final int			CHUNKS_Z = WORLDSIZE_Z / 16;
	
	public WorldInfo				worldInfo = null;
	
	private NBTTagCompound			worldNBT = new NBTTagCompound("World");
	
	private Chunk[][]				chunks			= new Chunk[CHUNKS_X * 2][CHUNKS_Z * 2];
	private Map<Integer, Entity>	entitys			= new HashMap<Integer, Entity>();
	private List<Integer>			entitysToRemove	= new LinkedList<Integer>();
	
	public World(WorldInfo info)
	{
		this.worldInfo = info;
	}
	
	public BlockInWorld getBlock(int x, int y, int z)
	{
		if (x >= -WORLDSIZE_X && x < WORLDSIZE_X && y >= 0 && y < 64 && z >= -WORLDSIZE_Z && z < WORLDSIZE_Z)
		{
			Chunk c = getChunkAtCoordinates(x, z);
			return c.getBlock(x, y, z);
		}
		return BlockInWorld.AIR;
	}
	
	public void setBlock(int block, int meta, int x, int y, int z)
	{
		setBlock(block, meta, x, y, z, 1);
	}
	
	public void setBlock(int block, int meta, int x, int y, int z, int flags)
	{
		Chunk c = getChunkAtCoordinates(x, z);
		c.setBlock(block, meta, x, y, z, flags);
	}
	
	public Chunk getChunkAtCoordinates(int x, int z)
	{
		// Should be -32 - 31
		int x1 = (int)(x / 16F);
		int z1 = (int)(z / 16F);
		int x2 = x1 + (CHUNKS_X);
		int z2 = z1 + (CHUNKS_Z);
		
		Chunk c = chunks[x2][z2];
		if (c == null)
		{
			c = chunks[x2][z2] = new Chunk(this, x1, z1).generate();
			System.out.println("Generating missing chunk " + c.toString());
		}
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
	
	public float getLightValue(int x, int y, int z)
	{
		if (x >= -WORLDSIZE_X && x < WORLDSIZE_X && y >= 0 && y < 64 && z >= -WORLDSIZE_Z && z < WORLDSIZE_Z)
		{
			Chunk c = getChunkAtCoordinates(x, z);
			if (c != null)
			{
				return c.getLightValue(x, y, z);
			}
		}
		return 1F;
	}
	
	public void setLightValue(int x, int y, int z, float f)
	{
		if (x >= -WORLDSIZE_X && x < WORLDSIZE_X && y >= 0 && y < 64 && z >= -WORLDSIZE_Z && z < WORLDSIZE_Z)
		{
			Chunk c = getChunkAtCoordinates(x, z);
			if (c != null)
			{
				c.setLightValue(x, y, z, f);
			}
		}
	}
	
	public boolean save(File file)
	{
		if (worldNBT == null)
			worldNBT = new NBTTagCompound("World");
		
		worldNBT.clear();
		
		NBTTagList chunkDataList = new NBTTagList("ChunkData", chunks.length * chunks.length);
		for (int i = 0; i < chunks.length; i++)
		{
			for (int j = 0; j < chunks[i].length; j++)
			{
				if (chunks[i][j] != null)
				{
					NBTTagCompound chunkNBT = new NBTTagCompound("Chunk:" + i + ";" + j);
					chunks[i][j].writeToNBT(chunkNBT);
					chunkDataList.addTagCompound(chunkNBT);
				}
			}
		}
		worldNBT.setTagList(chunkDataList);
		
		NBTTagCompound infoCompound = new NBTTagCompound("WorldInfo");
		worldInfo.writeToNBT(infoCompound);
		worldNBT.setTagCompound(infoCompound);
		
		NBTTagList entityDataList = new NBTTagList("EntityData");
		for (Integer i : this.entitys.keySet())
		{
			Entity entity = this.entitys.get(i);
			NBTTagCompound entityNBT = new NBTTagCompound("Entity#" + entity.entityId);
			entity.writeToNBT(entityNBT);
			entityDataList.addTagCompound(entityNBT);
		}
		worldNBT.setTagList(entityDataList);
		
		return worldNBT.serialize(file, true);
	}
	
	public boolean load(File file)
	{
		worldNBT = (NBTTagCompound) NBTBase.deserialize(file, true);
		if (worldNBT == null)
			return false;
		
		NBTTagList chunkDataList = worldNBT.getTagList("ChunkData");
		if (chunkDataList != null)
			for (NBTBase base : chunkDataList)
			{
				if (base instanceof NBTTagCompound)
				{
					Chunk chunk = new Chunk(this, 0, 0);
					chunk.readFromNBT((NBTTagCompound)base);
					chunks[chunk.chunkX + CHUNKS_X][chunk.chunkZ + CHUNKS_Z] = chunk;
				}
			}
		
		NBTTagCompound infoCompound = worldNBT.getTagCompound("WorldInfo");
		worldInfo.readFromNBT(infoCompound);
		
		NBTTagList entityDataList = worldNBT.getTagList("EntityData");
		if (entityDataList != null)
			for (NBTBase base : entityDataList)
			{
				if (base instanceof NBTTagCompound)
				{
					String entityType = ((NBTTagCompound)base).getString("EntityType");
					Entity entity = EntityList.constructFromType(entityType, this);
					this.spawnEntityInWorld(entity);
				}
			}
		return true;
	}
}
