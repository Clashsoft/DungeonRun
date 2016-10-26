package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.entity.EntityList;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.nbt.NamedBinaryTag;
import com.clashsoft.nbt.io.NBTSerializer;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;
import com.clashsoft.nbt.tags.collection.NBTTagList;
import org.newdawn.slick.SlickException;

import java.io.File;
import java.util.*;

public class World
{
	private static final String CHUNKS_DIRNAME  = "chunks";
	private static final String LEVEL_FILENAME  = "level.drf";
	private static final String CHUNK_EXTENSION = ".chk";

	public WorldInfo worldInfo = null;

	private NBTTagCompound worldNBT = new NBTTagCompound("World");

	private Map<Integer, Chunk>       chunks        = new HashMap<>();
	private Map<Integer, Entity>      entitys       = new HashMap<>();
	private Map<String, EntityPlayer> playerEntitys = new HashMap<>();

	private List<Entity> spawnList = new ArrayList<>();

	public int minChunkX = 0;
	public int maxChunkX = 0;

	public final Random random = new Random();

	public World(WorldInfo info)
	{
		this.worldInfo = info;
	}

	public Chunk getChunk(int x)
	{
		Chunk c = this.chunks.get(x);
		if (c == null)
		{
			c = this.newChunk(x);
		}
		return c;
	}

	private void setChunk(int x, Chunk chunk)
	{
		this.updateChunkBounds(x);
		this.chunks.put(x, chunk);
	}

	public Chunk getChunkAtCoordinates(int x)
	{
		return this.getChunk(x >> 4);
	}

	protected Chunk newChunk(int x)
	{
		System.out.println("Generating missing chunk at " + x);

		final Chunk chunk = new Chunk(this, x);
		this.setChunk(x, chunk);

		WorldGenerator.generateChunk(chunk, this.random);
		chunk.initLightAndHeightMap();
		WorldGenerator.generateStructures(this, this.random, x << 4);
		chunk.initLightAndHeightMap();

		return chunk;
	}

	protected void updateChunkBounds(int x)
	{
		if (x > this.maxChunkX)
		{
			this.maxChunkX = x;
		}
		if (x < this.minChunkX)
		{
			this.minChunkX = x;
		}
	}

	public boolean isChunkLoaded(int x, int y)
	{
		return this.getChunkAtCoordinates(x) != null;
	}

	public void spawnEntity(Entity entity)
	{
		this.spawnList.add(entity);
	}

	private void spawnEntity0(Entity entity)
	{
		if (entity instanceof EntityPlayer)
		{
			final EntityPlayer player = (EntityPlayer) entity;
			this.playerEntitys.put(player.username, player);
		}
		this.entitys.put(entity.entityId, entity);
	}

	public void removeEntity(int id)
	{
		final Entity entity = this.entitys.remove(id);
		if (entity instanceof EntityPlayer)
		{
			this.playerEntitys.remove(((EntityPlayer) entity).username);
		}
	}

	public void updateWorld() throws SlickException
	{
		for (Entity entity : this.spawnList)
		{
			this.spawnEntity0(entity);
		}
		this.spawnList.clear();

		final Iterator<Map.Entry<Integer, Entity>> iterator = this.entitys.entrySet().iterator();

		while (iterator.hasNext())
		{
			final Map.Entry<Integer, Entity> entry = iterator.next();

			final Entity entity = entry.getValue();
			entity.updateEntity(this.random);

			if (entity.isDead())
			{
				iterator.remove();
			}
		}
	}

	public Collection<Entity> getEntitys()
	{
		return this.entitys.values();
	}

	public Collection<EntityPlayer> getPlayers()
	{
		return this.playerEntitys.values();
	}

	public EntityPlayer getPlayer(String username) throws SlickException
	{
		return this.playerEntitys.get(username);
	}

	public Block getBlock(int x, int y)
	{
		final Chunk chunk = this.getChunkAtCoordinates(x);
		return chunk == null ? null : chunk.getBlock(x & 15, y);
	}

	public int getBlockMetadata(int x, int y)
	{
		final Chunk chunk = this.getChunkAtCoordinates(x);
		return chunk == null ? 0 : chunk.getBlockMetadata(x & 15, y);
	}

	public void setBlock(Block block, int meta, int x, int y)
	{
		this.setBlock(block, meta, x, y, Chunk.UPDATE);
	}

	public void setBlock(Block block, int meta, int x, int y, int flags)
	{
		Chunk c = this.getChunkAtCoordinates(x);
		if (c != null)
		{
			c.setBlock(block, meta, x & 15, y, flags);
		}
	}

	public int getHeight(int x)
	{
		final Chunk chunk = this.getChunkAtCoordinates(x);
		return chunk != null ? chunk.getHeight(x & 15) : 0;
	}

	public float getLightValue(int x, int y)
	{
		final Chunk chunk = this.getChunkAtCoordinates(x);
		return chunk != null ? chunk.getLightValue(x & 15, y) : 1F;
	}

	public void setLightValue(int x, int y, float f)
	{
		Chunk c = this.getChunkAtCoordinates(x);
		if (c != null)
		{
			c.setLightValue(x & 15, y, f);
		}
	}

	public boolean save(File file)
	{
		boolean success = true;
		long now = System.currentTimeMillis();

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
		this.worldNBT.setInteger("MaxChunkX", this.maxChunkX);

		// Entities

		NBTTagList entityDataList = new NBTTagList("entities");
		for (Integer i : this.entitys.keySet())
		{
			Entity entity = this.entitys.get(i);
			NBTTagCompound entityNBT = new NBTTagCompound(Integer.toString(entity.entityId));
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

		int chunks = 0;

		for (int x = this.minChunkX; x <= this.maxChunkX; x++)
		{
			final Chunk chunk = this.chunks.get(x);
			if (chunk == null || !chunk.isDirty())
			{
				continue;
			}

			String fileName = "chunk_" + x;
			File chunkFile = new File(regionDir, fileName + CHUNK_EXTENSION);
			NBTTagCompound chunkCompound = new NBTTagCompound(fileName);

			chunks++;
			chunk.writeToNBT(chunkCompound);
			NBTSerializer.serialize(chunkCompound, chunkFile);

			chunk.markClean();
		}

		now = System.currentTimeMillis() - now;

		System.out.println("World Saved (" + now + " ms).");
		System.out.println("Saved " + chunks + " Chunks.");

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
		this.maxChunkX = this.worldNBT.getInteger("MaxChunkX");

		// World info

		NBTTagCompound infoCompound = this.worldNBT.getTagCompound("info");
		this.worldInfo.readFromNBT(infoCompound);

		NBTTagList entityDataList = this.worldNBT.getTagList("entities");
		if (entityDataList != null)
		{
			for (NamedBinaryTag base : entityDataList)
			{
				if (!(base instanceof NBTTagCompound))
				{
					continue;
				}

				final NBTTagCompound compound = (NBTTagCompound) base;
				Entity entity = EntityList.constructFromNBT(compound, this);
				entity.readFromNBT(compound);
				this.spawnEntity0(entity);
			}
		}

		// -- Chunks --

		File regionDir = new File(file, CHUNKS_DIRNAME);
		if (!regionDir.exists())
		{
			return false;
		}

		for (int x = this.minChunkX; x < this.maxChunkX; x++)
		{
			Chunk chunk = this.chunks.get(x);
			if (chunk != null)
			{
				continue;
			}

			final File chunkFile = new File(regionDir, "chunk_" + x + CHUNK_EXTENSION);
			if (!chunkFile.exists())
			{
				continue;
			}

			NBTTagCompound nbt = (NBTTagCompound) NBTSerializer.deserialize(chunkFile);
			chunk = new Chunk(this, x);
			chunk.readFromNBT(nbt);
			this.chunks.put(x, chunk);
		}

		return true;
	}
}
