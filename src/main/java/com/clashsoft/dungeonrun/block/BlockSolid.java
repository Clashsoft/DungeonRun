package com.clashsoft.dungeonrun.block;

public class BlockSolid extends Block
{
	protected Block wallBlock;

	public BlockSolid(String id)
	{
		super(id);
	}

	public Block getWallBlock()
	{
		return this.wallBlock;
	}

	public Block setWallBlock(Block wall)
	{
		this.wallBlock = wall;
		return this;
	}
}
