package com.clashsoft.dungeonrun.world;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.block.Blocks;
import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.entity.EntityList;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import dyvil.tools.nbt.NamedBinaryTag;
import dyvil.tools.nbt.collection.NBTArray;
import dyvil.tools.nbt.collection.NBTList;
import dyvil.tools.nbt.collection.NBTMap;
import dyvil.tools.nbt.util.NBTSerializer;
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

	private NBTMap worldNBT = new NBTMap();

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
		return block == null ? 0 : this.blockToID.get(block.getBlockName());
	}

	protected Block getBlockByID(int id)
	{
		return id >= this.idToBlock.length ? null : this.idToBlock[id];
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

		final NBTMap nbt = (NBTMap) NBTSerializer.deserialize(chunkFile);
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

	public EntityPlayer getPlayer(String username)
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

	public void addForegroundBlock(ForegroundBlock block)
	{
		final Collection<ForegroundBlock> list = this.getChunkAtCoordinates(block.x).getForegroundBlocks();

		if (!list.contains(block))
		{
			list.add(block);
		}
	}

	public Collection<ForegroundBlock> getForegroundBlocks(int x1, int y1, int x2, int y2)
	{
		final Collection<ForegroundBlock> list = new ArrayList<>();

		for (int cx = x1 >> 4; cx <= x2 >> 4; cx++)
		{
			for (ForegroundBlock block : this.getChunk(cx).getForegroundBlocks())
			{
				if (block.x >= x1 && block.x <= x2 && block.y >= y1 && block.y <= y2)
				{
					list.add(block);
				}
			}
		}

		return list;
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

		NBTMap infoCompound = new NBTMap();
		this.worldInfo.writeToNBT(infoCompound);
		this.worldNBT.setTag("info", infoCompound);

		// Block IDs

		this.worldNBT.setTag("blockIDs", new NBTArray(this.getBlockIDs()));

		// Entities

		NBTList entityDataList = new NBTList(this.entitys.size());
		for (Entity entity : this.entitys.values())
		{
			final NBTMap nbt = new NBTMap();

			entity.writeToNBT(nbt);
			entityDataList.addTag(nbt);
		}
		this.worldNBT.setTag("entities", entityDataList);
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

		final NBTMap chunkCompound = new NBTMap();
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

		this.worldNBT = (NBTMap) NBTSerializer.deserialize(level);
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

		NBTMap infoCompound = this.worldNBT.getTagCompound("info");
		this.worldInfo.readFromNBT(infoCompound);

		NBTList entityDataList = this.worldNBT.getTagList("entities");
		if (entityDataList != null)
		{
			for (NamedBinaryTag base : entityDataList)
			{
				if (!(base instanceof NBTMap))
				{
					continue;
				}

				final NBTMap compound = (NBTMap) base;
				Entity entity = EntityList.constructFromNBT(compound, this);
				entity.readFromNBT(compound);
				this.spawnEntity0(entity);
			}
		}

		final NBTArray blockIDs = this.worldNBT.getTagArray("blockIDs");
		this.generateIDs(blockIDs == null ? null : blockIDs.getStringArray());
	}
}
