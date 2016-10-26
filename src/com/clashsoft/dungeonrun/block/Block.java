package com.clashsoft.dungeonrun.block;

import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.item.IStackable;
import com.clashsoft.dungeonrun.item.ItemStack;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import com.clashsoft.dungeonrun.world.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Block implements IStackable
{
	public static Block[] blocksList = new Block[256];

	public static Block air         = new Block(0).setBlockName("air").setBackground();
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
	public static Block ladder      = new BlockLadder(11).setBlockName("ladder").setBackground();
	public static Block water       = new BlockLadder(12).setBlockName("water").setBackground();

	protected final int blockID;
	protected boolean solid = true;
	protected String blockName;
	protected Image  texture;

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
		this.solid = false;
		return this;
	}

	public boolean isSolid()
	{
		return this.solid;
	}

	public boolean isAir()
	{
		return this == air;
	}

	public boolean isClimbable()
	{
		return false;
	}

	public Block setBlockName(String name)
	{
		this.blockName = name;
		return this;
	}

	public String getBlockName()
	{
		return this.blockName;
	}

	public void registerIcons()
	{
		if (this.blockName != null && this != air)
		{
			this.texture = getIcon(this.blockName);
		}
	}

	public static Image getIcon(String name)
	{
		try
		{
			return ResourceHelper.loadTexture("resources/textures/blocks/" + name + ".png");
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public Image getTexture(int metadata) throws SlickException
	{
		return this.texture;
	}

	@Override
	public int getMaxStackSize(ItemStack stack)
	{
		return 64;
	}

	public boolean canCollide(World world, int x, int y, Entity entity)
	{
		return this.solid;
	}

	public float getLightValue()
	{
		return 0F;
	}
}
