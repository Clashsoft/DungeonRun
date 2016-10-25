package com.clashsoft.dungeonrun.block;

import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.item.IStackable;
import com.clashsoft.dungeonrun.item.ItemStack;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Block implements IStackable
{
	public static Block[] blocksList = new Block[256];

	public static Block grass       = new BlockGrass(1).setBlockName("grass");
	public static Block dirt        = new Block(2).setBlockName("dirt");
	public static Block sand        = new Block(3).setBlockName("sand");
	public static Block log         = new Block(4).setBlockName("wood").setBackground();
	public static Block leaves      = new Block(5).setBlockName("leaves").setBackground();
	public static Block brick       = new Block(6).setBlockName("brick");
	public static Block stone       = new Block(7).setBlockName("stone");
	public static Block cobbleStone = new Block(8).setBlockName("cobblestone").setBackground();
	public static Block planks      = new Block(9).setBlockName("planks");
	public static Block planksWall  = new Block(10).setBlockName("plank_wall").setBackground();
	public static Block water       = new Block(11).setBlockName("water").setBackground();

	public int blockID;
	public boolean isBackground = true;
	public String blockName;
	public Image  texture;

	public Block(int id)
	{
		this.blockID = id;

		blocksList[id] = this;
	}

	@Override
	public int getID()
	{
		return this.blockID;
	}

	@Override
	public boolean isBlock()
	{
		return false;
	}

	public Block setBackground()
	{
		this.isBackground = false;
		return this;
	}

	public Block setBlockName(String name)
	{
		this.blockName = name;
		return this;
	}

	public void registerIcons()
	{
		if (this.blockName != null)
		{
			this.texture = getIcon(this.blockName);
		}
	}

	public static Image getIcon(String name)
	{
		try
		{
			return new Image("resources/textures/blocks/" + name + ".png");
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public Image getTexture(int side, int meta) throws SlickException
	{
		return this.texture;
	}

	@Override
	public int getMaxStackSize(ItemStack stack)
	{
		return 64;
	}

	public boolean canCollide(int metadata, Entity entity)
	{
		return this.isBackground;
	}

	public float getLightValue()
	{
		return 0F;
	}
}
