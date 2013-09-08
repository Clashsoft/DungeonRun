package com.clashsoft.dungeonrun.nbt;

import java.util.HashMap;
import java.util.Map;

public class NBTTagCompound extends NBTBase
{
	private static final long	serialVersionUID	= 1089587733286065216L;
	
	private Map<String, NBTBase> tags = new HashMap<>();
	
	public NBTTagCompound(String name)
	{
		super(TYPE_COMPOUND, name);
	}
	
	public boolean setTag(String name, NBTBase tag)
	{
		boolean ret = tags.containsKey(name);
		tag.name = name;
		tags.put(name, tag);
		return ret;
	}
	
	public boolean hasTag(String name)
	{
		return tags.containsKey(name);
	}
	
	public NBTBase getTag(String name)
	{
		return tags.get(name);
	}
	
	public void setBoolean(String name, boolean value)
	{
		setTag(name, new NBTTagBoolean(name, value));
	}
	
	public void setByte(String name, byte value)
	{
		setTag(name, new NBTTagByte(name, value));
	}
	
	public void setShort(String name, short value)
	{
		setTag(name, new NBTTagShort(name, value));
	}
	
	public void setInteger(String name, int value)
	{
		setTag(name, new NBTTagInteger(name, value));
	}
	
	public void setFloat(String name, float value)
	{
		setTag(name, new NBTTagFloat(name, value));
	}
	
	public void setDouble(String name, double value)
	{
		setTag(name, new NBTTagDouble(name, value));
	}
	
	public void setString(String name, String value)
	{
		setTag(name, new NBTTagString(name, value));
	}
	
	public void setTagList(String name, NBTTagList list)
	{
		setTag(name, list);
	}
	
	public void setTagCompound(String name, NBTTagCompound compound)
	{
		setTag(name, compound);
	}
	
	public boolean getBoolean(String name)
	{
		return ((NBTTagBoolean)getTag(name)).value;
	}
	
	public byte getByte(String name)
	{
		return ((NBTTagByte)getTag(name)).value;
	}
	
	public short getShort(String name)
	{
		return ((NBTTagShort)getTag(name)).value;
	}
	
	public int getInteger(String name)
	{
		return ((NBTTagInteger)getTag(name)).value;
	}
	
	public float getFloat(String name)
	{
		return ((NBTTagFloat)getTag(name)).value;
	}
	
	public double getDouble(String name)
	{
		return ((NBTTagDouble)getTag(name)).value;
	}
	
	public String getString(String name)
	{
		return ((NBTTagString)getTag(name)).value;
	}
	
	public NBTTagList getTagList(String name)
	{
		return (NBTTagList)getTag(name);
	}
	
	public NBTTagCompound getTagCompound(String name)
	{
		return (NBTTagCompound)getTag(name);
	}
}
