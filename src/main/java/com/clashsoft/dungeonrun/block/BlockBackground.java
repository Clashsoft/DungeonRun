package com.clashsoft.dungeonrun.block;

public class BlockBackground extends Block
{
	public BlockBackground(String id)
	{
		super(id);
	}

	@Override
	public boolean isSolid()
	{
		return false;
	}
}
