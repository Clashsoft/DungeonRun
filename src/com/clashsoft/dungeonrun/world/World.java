package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.block.Blocks;
import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.entity.EntityList;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.nbt.NamedBinaryTag;
import com.clashsoft.nbt.io.NBTSerializer;
import com.clashsoft.nbt.tags.collection.NBTTagArray;
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

	// World Info
	private WorldInfo worldInfo;

	private File worldDir;
	private File chunksDir;

	private NBTTagCompound worldNBT = new NBTTagCompound("World");

	// Entities
	private final Map<Integer, Entity>      entitys       = new HashMap<>();
	private final Map<String, EntityPlayer> playerEntitys = new HashMap<>();
	private final List<Entity>              spawnList     = new ArrayList<>();

	// Block IDs
	private final Map<String, Integer> blockToID = new TreeMap<>();
	private Block[] idToBlock;

	// Chunk Info
	private final Map<Integer, Chunk> chunks = new HashMap<>();

	public final Random random = new Random();

	public World(WorldInfo info, File worldDir)
	{
		this.worldInfo = info;
		this.worldDir = worldDir;
		this.chunksDir = new File(worldDir, CHUNKS_DIRNAME);
	}

	public WorldInfo getWorldInfo()
	{
		return this.worldInfo;
	}

	public void generateIDs(String[] from)
	{
		// Fill Block ID map
		final int count = Block.blocks.size();
		this.idToBlock = new Block[count];

		int index = 0;
		if (from != null)
		{
			// Copy/map existing data
			for (String key : from)
			{
				final Block block = Block.blocks.get(key);
				if (block == null)
				{
					continue;
				}

				this.blockToID.put(key, index);
				this.idToBlock[index] = block;
				index++;
			}
		}

		for (Map.Entry<String, Block> entry : Block.blocks.entrySet())
		{
			if (this.blockToID.putIfAbsent(entry.getKey(), index) == null)
			{
				this.idToBlock[index] = entry.getValue();
				index++;
			}
		}
	}

	protected int getBlockID(Block block)
	{
		return this.blockToID.get(block.getBlockName());
	}

	protected Block getBlockByID(int id)
	{
		return this.idToBlock[id];
	}

	public Chunk getChunk(int x)
	{
		Chunk chunk = this.chunks.get(x);
		if (chunk != null)
		{
			return chunk;
		}

		final File chunkFile = new File(this.chunksDir, "chunk_" + x + CHUNK_EXTENSION);
		if (!chunkFile.exists())
		{
			return this.generateChunk(x);
		}

		final NBTTagCompound nbt = (NBTTagCompound) NBTSerializer.deserialize(chunkFile);
		chunk = new Chunk(this, x);
		chunk.readFromNBT(nbt);
		this.chunks.put(x, chunk);

		return chunk;
	}

	private void setChunk(int x, Chunk chunk)
	{
		this.chunks.put(x, chunk);
	}

	public Chunk getChunkAtCoordinates(int x)
	{
		return this.getChunk(x >> 4);
	}

	private Chunk generateChunk(int x)
	{
		final Chunk chunk = new Chunk(this, x);
		this.setChunk(x, chunk);

		WorldGenerator.generateChunk(chunk, this.random);
		chunk.initLightAndHeightMap();
		WorldGenerator.generateStructures(this, this.random, x << 4);
		chunk.initLightAndHeightMap();

		return chunk;
	}

	public boolean isChunkLoaded(int x)
	{
		return this.chunks.containsKey(x >> 4);
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
			this.playerEntitys.put(player.getUsername(), player);
		}
		this.entitys.put(entity.id, entity);
	}

	public void removeEntity(int id)
	{
		final Entity entity = this.entitys.remove(id);
		if (entity instanceof EntityPlayer)
		{
			this.playerEntitys.remove(((EntityPlayer) entity).getUsername());
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
		return chunk == null ? Blocks.air : chunk.getBlock(x & 15, y);
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

	public boolean save(ChunkSaver chunkSaver)
	{
		long now = System.currentTimeMillis();

		if (!this.worldDir.exists() && !this.worldDir.mkdirs())
		{
			return false;
		}

		if (this.worldNBT == null)
		{
			this.worldNBT = new NBTTagCompound("World");
		}

		// -- Level --

		File level = new File(this.worldDir, LEVEL_FILENAME);

		this.saveWorldInfo();

		// Save world NBT

		boolean success = NBTSerializer.serialize(this.worldNBT, level);

		// -- Chunks --

		if (!this.chunksDir.exists() && !this.chunksDir.mkdirs())
		{
			return false;
		}

		final int chunks = this.saveChunks(chunkSaver);

		now = System.currentTimeMillis() - now;

		System.out.println("World Saved (" + now + " ms).");
		System.out.println("Saved " + chunks + " Chunks.");

		return success;
	}

	private void saveWorldInfo()
	{
		// World Info

		NBTTagCompound infoCompound = new NBTTagCompound("info");
		this.worldInfo.writeToNBT(infoCompound);
		this.worldNBT.setTagCompound(infoCompound);

		// Block IDs

		this.worldNBT.setTagArray(new NBTTagArray("BlockIDs", this.getBlockIDs()));

		// Entities

		NBTTagList entityDataList = new NBTTagList("entities");
		for (Integer i : this.entitys.keySet())
		{
			Entity entity = this.entitys.get(i);
			NBTTagCompound entityNBT = new NBTTagCompound(Integer.toString(entity.id));
			entity.writeToNBT(entityNBT);
			entityDataList.addTagCompound(entityNBT);
		}
		this.worldNBT.setTagList(entityDataList);
	}

	private int saveChunks(ChunkSaver chunkSaver)
	{
		int chunks = 0;

		synchronized (this.chunks)
		{
			for (Chunk chunk : this.chunks.values())
			{
				if (chunk.isDirty())
				{
					chunks++;
					chunkSaver.enqueue(chunk);
				}
			}
		}

		return chunks;
	}

	protected void saveChunk(Chunk chunk)
	{
		String fileName = "chunk_" + chunk.chunkX;
		File chunkFile = new File(this.chunksDir, fileName + CHUNK_EXTENSION);

		NBTTagCompound chunkCompound = new NBTTagCompound(fileName);

		chunk.writeToNBT(chunkCompound);
		NBTSerializer.serialize(chunkCompound, chunkFile);

		chunk.markClean();
	}

	private String[] getBlockIDs()
	{
		final int length = this.idToBlock.length;
		final String[] blockIDs = new String[length];
		for (int i = 0; i < length; i++)
		{
			blockIDs[i] = this.idToBlock[i].getBlockName();
		}
		return blockIDs;
	}

	public boolean load()
	{
		// -- Level --

		File level = new File(this.worldDir, LEVEL_FILENAME);
		if (!level.exists())
		{
			this.generateIDs(null);
			return false;
		}

		this.worldNBT = (NBTTagCompound) NBTSerializer.deserialize(level);
		if (this.worldNBT == null)
		{
			this.generateIDs(null);
			return false;
		}

		this.loadWorldInfo();

		return true;
	}

	private void loadWorldInfo()
	{
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

		final NBTTagArray blockIDs = this.worldNBT.getTagArray("BlockIDs");
		this.generateIDs(blockIDs == null ? null : blockIDs.getStringArray());
	}
}
