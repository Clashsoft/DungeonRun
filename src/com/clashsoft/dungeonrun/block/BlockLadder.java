package com.clashsoft.dungeonrun.block;

public class BlockLadder extends Block
{
	public BlockLadder(String id)
	{
		super(id);
	}

	@Override
	public boolean isClimbable()
	{
		return true;
	}
}
