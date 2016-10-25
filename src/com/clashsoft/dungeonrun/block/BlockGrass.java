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
		this.sideIcon = getIcon("grass_side");
	}
	
	@Override
	public Image getTexture(int side, int meta) throws SlickException
	{
		return side == 1 ? this.texture : this.sideIcon;
	}
}
