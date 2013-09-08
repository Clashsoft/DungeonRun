package com.clashsoft.dungeonrun.nbt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NBTTagList extends NBTBase implements Iterable
{
	private static final long	serialVersionUID	= 9188374575161487548L;
	
	private List<NBTBase> tags;
	
	public NBTTagList(String name)
	{
		this(name, 10);
	}

	public NBTTagList(String name, int capacity)
	{
		super(TYPE_LIST, name);
		tags = new ArrayList(capacity);
	}
	
	public boolean addTag(String name, NBTBase tag)
	{
		boolean ret = tags.contains(tag);
		tag.name = name;
		tags.add(tag);
		return ret;
	}
	
	public int tagCount()
	{
		return tags.size();
	}
	
	public NBTBase tagAt(int index)
	{
		return (index >= 0 && index < tagCount()) ? tags.get(index) : null;
	}
	
	@Override
	public Iterator iterator()
	{
		return tags.iterator();
	}
	
	public void addBoolean(String name, boolean value)
	{
		addTag(name, new NBTTagBoolean(name, value));
	}
	
	public void addByte(String name, byte value)
	{
		addTag(name, new NBTTagByte(name, value));
	}
	
	public void addShort(String name, short value)
	{
		addTag(name, new NBTTagShort(name, value));
	}
	
	public void addInteger(String name, int value)
	{
		addTag(name, new NBTTagInteger(name, value));
	}
	
	public void addFloat(String name, float value)
	{
		addTag(name, new NBTTagFloat(name, value));
	}
	
	public void addDouble(String name, double value)
	{
		addTag(name, new NBTTagDouble(name, value));
	}
	
	public void addString(String name, String value)
	{
		addTag(name, new NBTTagString(name, value));
	}
	
	public void addTagList(String name, NBTTagList list)
	{
		addTag(name, list);
	}
	
	public void addTagCompound(String name, NBTTagCompound compound)
	{
		addTag(name, compound);
	}
}
