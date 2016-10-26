package com.clashsoft.dungeonrun.block;

public class BlockGrass extends Block
{
	public BlockGrass(int id)
	{
		super(id);
	}
	
	@Override
	public void registerIcons()
	{
		this.texture = getIcon("grass_side");
	}
}
