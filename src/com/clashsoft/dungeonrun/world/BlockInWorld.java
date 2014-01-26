package com.clashsoft.dungeonrun.world;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.block.Block;

public class BlockInWorld
{
	public static BlockInWorld	AIR	= new BlockInWorld(null, 0, 0, 1F);
	
	public World				world;
	public int					blockID;
	public int					metadata;
	public float lightValue;
	
	public BlockInWorld(World w, int id, int metadata, float lightValue)
	{
		this.blockID = id;
		this.metadata = metadata;
		this.world = w;
	}
	
	public BlockInWorld(World w, Block block, int metadata, float lightValue)
	{
		this(w, block.blockID, metadata, lightValue);
	}
	
	public Block getBlock()
	{
		return Block.blocksList[this.blockID];
	}
	
	public int getMetadata()
	{
		return this.metadata;
	}
	
	public float getLightValue()
	{
		return this.lightValue;
	}
	
	public boolean isAir()
	{
		return this.blockID == 0 || this == AIR;
	}
	
	public Image getBlockTexture(int side) throws SlickException
	{
		Block block = this.getBlock();
		return block == null ? null : block.getTexture(side, this.metadata);
	}
}
