package com.clashsoft.dungeonrun.block;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class BlockGrass extends Block
{
	private Image	sideIcon;
	
	public BlockGrass(int id)
	{
		super(id);
	}
	
	@Override
	public void registerIcons()
	{
		super.registerIcons();
		sideIcon = getIcon("grass_side");
	}
	
	@Override
	public Image getBlockTextureFromSideAndMetadata(int side, int meta) throws SlickException
	{
		return side == 4 ? sideIcon : this.texture;
	}
}
