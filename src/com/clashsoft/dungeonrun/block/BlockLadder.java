package com.clashsoft.dungeonrun.block;

public class BlockLadder extends Block
{
	public BlockLadder(int id)
	{
		super(id);
	}

	@Override
	public boolean isClimbable()
	{
		return true;
	}
}
