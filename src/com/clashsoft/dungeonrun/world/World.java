package com.clashsoft.dungeonrun.world;

import java.io.File;
import java.util.*;

import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.entity.EntityList;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.nbt.NamedBinaryTag;
import com.clashsoft.nbt.io.NBTSerializer;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;
import com.clashsoft.nbt.tags.collection.NBTTagList;

public class World
{
	private static final String			CHUNKS_DIRNAME	= "chunks";
	private static final String			LEVEL_FILENAME	= "level.drf";
	private static final String			CHUNK_EXTENSION	= ".chk";
	
	public WorldInfo					worldInfo		= null;
	
	private NBTTagCompound				worldNBT		= new NBTTagCompound("World");
	
	private Map<ChunkPosition, Chunk>	chunks			= new HashMap();
	private Map<Integer, Entity>		entitys			= new HashMap<Integer, Entity>();
	private Map<String, EntityPlayer>	playerEntitys	= new HashMap<String, EntityPlayer>();
	
	public int							minChunkX		= 0;
	public int							minChunkY		= 0;
	public int							minChunkZ		= 0;
	public int							maxChunkX		= 0;
	public int							maxChunkY		= 0;
	public int							maxChunkZ		= 0;
	
	public World(WorldInfo info)
	{
		this.worldInfo = info;
	}
	
	public Chunk getChunk(ChunkPosition pos)
	{
		Chunk c = this.chunks.get(pos);
		if (c == null)
		{
			c = this.newChunk(pos);
		}
		return c;
	}
	
	public void setChunk(ChunkPosition pos, Chunk chunk)
	{
		this.updateChunkBounds(pos);
		this.chunks.put(pos, chunk);
	}
	
	public Chunk getChunkAtCoordinates(int x, int y, int z)
	{
		ChunkPosition pos = new ChunkPosition(x >> 4, y >> 4, z >> 4);
		return this.getChunk(pos);
	}
	
	public void setChunkAtCoordinates(int x, int y, int z, Chunk chunk)
	{
		ChunkPosition pos = new ChunkPosition(x >> 4, y >> 4, z >> 4);
		this.setChunk(pos, chunk);
	}
	
	protected Chunk newChunk(ChunkPosition pos)
	{
		System.out.println("Generating missing chunk at " + pos);
		
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		
		Chunk c = new Chunk(this, x, y, z);
		WorldGenerator.generateChunk(c);
		this.setChunk(pos, c);
		return c;
	}
	
	protected void updateChunkBounds(ChunkPosition c)
	{
		int x = c.getX();
		int y = c.getY();
		int z = c.getZ();
		
		if (x > this.maxChunkX)
		{
			this.maxChunkX = x;
		}
		if (y > this.maxChunkY)
		{
			this.maxChunkY = y;
		}
		if (z > this.maxChunkZ)
		{
			this.maxChunkZ = z;
		}
		if (x < this.minChunkX)
		{
			this.minChunkX = x;
		}
		if (y < this.minChunkY)
		{
			this.minChunkY = y;
		}
		if (z < this.maxChunkZ)
		{
			this.maxChunkZ = z;
		}
	}
	
	public boolean isChunkLoaded(int x, int y, int z)
	{
		return this.getChunkAtCoordinates(x, y, z) != null;
	}
	
	public void spawnEntityInWorld(Entity e)
	{
		if (e instanceof EntityPlayer)
		{
			this.playerEntitys.put(((EntityPlayer) e).username, (EntityPlayer) e);
		}
		this.entitys.put(e.entityId, e);
	}
	
	public void removeEntity(int id)
	{
		this.entitys.remove(id);
		
		if (this.entitys.get(id) instanceof EntityPlayer)
		{
			this.playerEntitys.remove(((EntityPlayer) this.entitys.get(id)).username);
		}
	}
	
	public void updateWorld() throws SlickException
	{
		Iterator<Map.Entry<Integer, Entity>> iterator = this.entitys.entrySet().iterator();
		Map.Entry<Integer, Entity> e = null;
		
		while (iterator.hasNext())
		{
			e = iterator.next();
			Entity entity = e.getValue();
			entity.updateEntity();
			if (entity.isDead())
			{
				iterator.remove();
			}
		}
	}
	
	public List<Entity> getEntitys()
	{
		return new ArrayList(this.entitys.values());
	}
	
	public Collection<EntityPlayer> getPlayers()
	{
		return this.playerEntitys.values();
	}
	
	public EntityPlayer getPlayer(String username) throws SlickException
	{
		return this.playerEntitys.get(username);
	}
	
	public BlockInWorld getBlock(int x, int y, int z)
	{
		Chunk c = this.getChunkAtCoordinates(x, y, z);
		if (c != null)
		{
			return c.getBlock(x & 15, y & 15, z & 15);
		}
		else
		{
			return BlockInWorld.AIR;
		}
	}
	
	public void setBlock(int block, int meta, int x, int y, int z)
	{
		this.setBlock(block, meta, x, y, z, 1);
	}
	
	public void setBlock(int block, int meta, int x, int y, int z, int flags)
	{
		Chunk c = this.getChunkAtCoordinates(x, y, z);
		if (c != null)
		{
			c.setBlock(block, meta, x & 15, y & 15, z & 15, flags);
		}
	}
	
	public float getLightValue(int x, int y, int z)
	{
		Chunk c = this.getChunkAtCoordinates(x, y, z);
		if (c != null)
		{
			return c.getLightValue(x & 15, y & 15, z & 15);
		}
		return 1F;
	}
	
	public void setLightValue(int x, int y, int z, float f)
	{
		
		Chunk c = this.getChunkAtCoordinates(x, y, z);
		if (c != null)
		{
			c.setLightValue(x & 15, y & 15, z & 15, f);
		}
	}
	
	public boolean save(File file)
	{
		boolean success = true;
		
		if (this.worldNBT == null)
		{
			this.worldNBT = new NBTTagCompound("World");
		}
		
		// -- Level --
		
		File level = new File(file, LEVEL_FILENAME);
		
		// World Info
		
		NBTTagCompound infoCompound = new NBTTagCompound("info");
		this.worldInfo.writeToNBT(infoCompound);
		this.worldNBT.setTagCompound(infoCompound);
		
		// Chunk bounds
		
		this.worldNBT.setInteger("MinChunkX", this.minChunkX);
		this.worldNBT.setInteger("MinChunkY", this.minChunkY);
		this.worldNBT.setInteger("MinChunkZ", this.minChunkZ);
		this.worldNBT.setInteger("MaxChunkX", this.maxChunkX);
		this.worldNBT.setInteger("MaxChunkY", this.maxChunkY);
		this.worldNBT.setInteger("MaxChunkZ", this.maxChunkZ);
		
		// Entities
		
		NBTTagList entityDataList = new NBTTagList("entities");
		for (Integer i : this.entitys.keySet())
		{
			Entity entity = this.entitys.get(i);
			NBTTagCompound entityNBT = new NBTTagCompound("" + entity.entityId);
			entity.writeToNBT(entityNBT);
			entityDataList.addTagCompound(entityNBT);
		}
		this.worldNBT.setTagList(entityDataList);
		
		// Save world NBT
		
		success = NBTSerializer.serialize(this.worldNBT, level);
		
		// -- Chunks --
		
		File regionDir = new File(file, CHUNKS_DIRNAME);
		if (!regionDir.exists())
		{
			regionDir.mkdirs();
		}
		
		for (int i = this.minChunkX; i < this.maxChunkX; i++)
		{
			for (int j = this.minChunkY; j < this.maxChunkY; j++)
			{
				for (int k = this.minChunkZ; k < this.maxChunkZ; k++)
				{
					ChunkPosition pos = new ChunkPosition(i, j, k);
					Chunk c = this.chunks.get(pos);
					if (c != null)
					{
						String s = "chunk." + i + "." + j + "." + k;
						File chunkFile = new File(regionDir, s + CHUNK_EXTENSION);
						NBTTagCompound chunkCompound = new NBTTagCompound(s);
						c.writeToNBT(chunkCompound);
						NBTSerializer.serialize(chunkCompound, chunkFile);
					}
				}
			}
		}
		
		return success;
	}
	
	public boolean load(File file)
	{
		// -- Level --
		
		File level = new File(file, LEVEL_FILENAME);
		if (!level.exists())
		{
			return false;
		}
		
		this.worldNBT = (NBTTagCompound) NBTSerializer.deserialize(level);
		if (this.worldNBT == null)
		{
			return false;
		}
		
		// Chunk bounds
		
		this.minChunkX = this.worldNBT.getInteger("MinChunkX");
		this.minChunkY = this.worldNBT.getInteger("MinChunkY");
		this.minChunkZ = this.worldNBT.getInteger("MinChunkZ");
		this.maxChunkX = this.worldNBT.getInteger("MaxChunkX");
		this.maxChunkY = this.worldNBT.getInteger("MaxChunkY");
		this.maxChunkZ = this.worldNBT.getInteger("MaxChunkZ");
		
		// World info
		
		NBTTagCompound infoCompound = this.worldNBT.getTagCompound("info");
		this.worldInfo.readFromNBT(infoCompound);
		
		NBTTagList entityDataList = this.worldNBT.getTagList("entities");
		if (entityDataList != null)
		{
			for (NamedBinaryTag base : entityDataList)
			{
				if (base instanceof NBTTagCompound)
				{
					NBTTagCompound compound = (NBTTagCompound) base;
					Entity entity = EntityList.constructFromNBT(compound, this);
					entity.readFromNBT(compound);
					this.spawnEntityInWorld(entity);
				}
			}
		}
		
		// -- Chunks --
		
		File regionDir = new File(file, CHUNKS_DIRNAME);
		if (!regionDir.exists())
		{
			return false;
		}
		
		for (int i = this.minChunkX; i < this.maxChunkX; i++)
		{
			for (int j = this.minChunkY; j < this.maxChunkY; j++)
			{
				for (int k = this.minChunkZ; k < this.maxChunkZ; k++)
				{
					ChunkPosition pos = new ChunkPosition(i, j, k);
					Chunk c = this.chunks.get(pos);
					if (c != null)
					{
						String s = "chunk." + i + "." + j + "." + k;
						File chunkFile = new File(regionDir, s + CHUNK_EXTENSION);
						
						if (chunkFile.exists())
						{
							NBTTagCompound nbt = (NBTTagCompound) NBTSerializer.deserialize(chunkFile);
							Chunk chunk = new Chunk(this, i, j, k);
						}
					}
				}
			}
		}
		
		return true;
	}
}
