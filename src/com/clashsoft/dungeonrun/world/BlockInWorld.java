package com.clashsoft.dungeonrun.world;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.block.Block;

public class BlockInWorld
{
	public static BlockInWorld	AIR	= new BlockInWorld(null, 0, 0);
	
	public World				world;
	public int					blockID;
	public int					metadata;
	public int					x, y, z;
	
	public BlockInWorld(World w, int id, int metadata)
	{
		this.blockID = id;
		this.metadata = metadata;
		this.world = w;
	}
	
	public BlockInWorld(World w, Block block, int metadata)
	{
		this(w, block.blockID, metadata);
	}
	
	public Block getBlock()
	{
		return Block.blocksList[this.blockID];
	}
	
	public int getMetadata()
	{
		return this.metadata;
	}
	
	public boolean isAir()
	{
		return this.blockID == 0 || this == AIR;
	}
	
	public Image getBlockTexture(int side) throws SlickException
	{
		return this.getBlock().getBlockTextureFromSideAndMetadata(side, this.metadata);
	}
	
	public float getLightValue()
	{
		float f = this.getBlock() != null ? this.getBlock().getLightValue() : 0.1F;
		float f1 = this.world.getLightValue(this.x + 1, this.y, this.z);
		float f2 = this.world.getLightValue(this.x - 1, this.y, this.z);
		float f3 = this.world.getLightValue(this.x, this.y + 1, this.z);
		float f4 = this.world.getLightValue(this.x, this.y - 1, this.z);
		float f5 = this.world.getLightValue(this.x, this.y, this.z + 1);
		float f6 = this.world.getLightValue(this.x, this.y, this.z - 1);
		float f7 = (f1 + f2 + f3 + f4 + f5 + f6) / 6F;
		return f7;
	}
}
