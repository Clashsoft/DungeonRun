package com.clashsoft.dungeonrun.block;

import org.newdawn.slick.Image;

public class BlockChest extends BlockBackground
{
	private Image openTexture;

	public BlockChest(String name)
	{
		super(name);
	}

	@Override
	public void registerIcons()
	{
		super.registerIcons();

		this.openTexture = getIcon(this.blockName + "_open");
	}

	@Override
	public Image getTexture(int metadata)
	{
		return metadata == 1 ? this.openTexture : this.texture;
	}
}
