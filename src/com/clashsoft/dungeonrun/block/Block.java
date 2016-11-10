package com.clashsoft.dungeonrun.block;

import com.clashsoft.dungeonrun.entity.Entity;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import com.clashsoft.dungeonrun.world.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Map;
import java.util.TreeMap;

public class Block
{
	public static final Map<String, Block> blocks = new TreeMap<>();

	protected final String blockName;
	protected       Image  texture;

	public Block(String id)
	{
		this.blockName = id;

		blocks.put(id, this);
	}

	public String getBlockName()
	{
		return this.blockName;
	}

	public boolean isSolid()
	{
		return true;
	}

	public boolean isAir()
	{
		return this == Blocks.air;
	}

	public boolean isClimbable()
	{
		return false;
	}

	public void registerIcons()
	{
		if (this.blockName != null && this != Blocks.air)
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

	public Image getTexture(int metadata)
	{
		return this.texture;
	}

	public boolean canCollide(World world, int x, int y, Entity entity)
	{
		return this.isSolid();
	}

	public float getLightValue()
	{
		return 0F;
	}
}
