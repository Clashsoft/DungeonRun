package com.clashsoft.dungeonrun.block;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.item.IStackable;
import com.clashsoft.dungeonrun.item.ItemStack;

public class Block implements IStackable
{
	public static Block[]	blocksList		= new Block[256];
	
	public static Block		grass			= new BlockGrass(1).setBlockName("Grass");
	public static Block		dirt			= new Block(2).setBlockName("Dirt");
	public static Block		sand			= new Block(3).setBlockName("Sand");
	public static Block		log				= new Block(4).setBlockName("Wood");
	public static Block		leaves			= new Block(5).setBlockName("Leaves");
	public static Block		brick			= new Block(6).setBlockName("Brick");
	public static Block		stone			= new Block(7).setBlockName("Stone");
	public static Block		cobbleStone		= new Block(8).setBlockName("Cobblestone");
	public static Block		planks			= new Block(9).setBlockName("Planks");
	public static Block		water			= new Block(10).setBlockName("Water").setNoTop();
	
	public int				blockID;
	public boolean			canStepOnBlock	= true;
	public String			blockName;
	public Image			texture;
	
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
	
	public Block setNoTop()
	{
		this.canStepOnBlock = false;
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
			this.texture = this.getIcon(this.blockName);
		}
	}
	
	public Image getIcon(String name)
	{
		String s = name.replace(" ", "").toLowerCase();
		try
		{
			return new Image("resources/textures/blocks/" + s + ".png");
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
	
	public boolean canCollideVertically(int metadata, Entity entity)
	{
		return true;
	}
	
	public boolean canCollideHorizontally(int metadata, Entity entity)
	{
		return true;
	}
	
	public float getLightValue()
	{
		return 0F;
	}
}
