package com.clashsoft.dungeonrun.nbt;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NBTTagList extends NBTBase implements Iterable<NBTBase>
{	
	private ArrayList<NBTBase> tags;
	
	public NBTTagList(String name)
	{
		this(name, 10);
	}

	public NBTTagList(String name, int capacity)
	{
		super(TYPE_LIST, name, null);
		tags = new ArrayList(capacity);
	}
	
	@Override
	public ArrayList<NBTBase> getValue()
	{
		return tags;
	}
	
	public void addTag(NBTBase tag)
	{
		tags.add(tag);
	}
	
	public void addTag(String name, NBTBase tag)
	{
		tag.name = name;
		addTag(tag);
	}
	
	public void setTag(int index, NBTBase tag)
	{
		ensureSize(index + 1);
		tags.set(index, tag);
	}
	
	private void ensureSize(int size)
	{
		tags.ensureCapacity(size);
		while(tags.size() < size)
			tags.add(null);
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
	
	public void addLong(String name, long value)
	{
		addTag(name, new NBTTagLong(name, value));
	}
	
	public void addString(String name, String value)
	{
		addTag(name, new NBTTagString(name, value));
	}
	
	public void addTagList(NBTTagList list)
	{
		if (list != this)
			addTag(name, list);
	}
	
	public void addTagCompound(NBTTagCompound compound)
	{
		addTag(compound);
	}
	
	public static NBTTagList fromList(String name, List args)
	{
		NBTTagList list = new NBTTagList(name, args.size());
		for (int i = 0; i < args.size(); i++)
		{
			String tagName = name + "@" + i;
			NBTBase base = NBTBase.createFromObject(tagName, args.get(i));
			if (base != null)
				list.addTag(base);
		}
		return list;
	}
	
	public static <T extends Serializable> NBTTagList fromArray(String name, T... args)
	{
		NBTTagList list = new NBTTagList(name, args.length);
		for (int i = 0; i < args.length; i++)
		{
			String tagName = name + "@" + i;
			T t = args[i];
			NBTBase tag = NBTBase.createFromObject(tagName, t);
			if (tag != null)
				list.addTag(tagName, tag);
		}
		return list;
	}

	public <T> T[] toArray(Class<T> arrayType)
	{
		T[] array = (T[]) Array.newInstance(arrayType, this.tags.size());
		for (int i = 0; i < this.tagCount(); i++)
		{
			array[i] = (T) this.tagAt(i).getValue();
		}
		return array;
	}

	public <T> T[] toArray()
	{
		return (T[]) tags.toArray();
	}
	
	@Override
	public boolean valueEquals(NBTBase that)
	{
		return tags.equals(((NBTTagList)that).tags);
	}
	
	@Override
	public String writeValueString(String prefix)
	{
		StringBuilder sb = new StringBuilder(tags.toString().length());
		
		sb.append("\n" + prefix + "[");
		
		for (int key = 0; key < tags.size(); key++)
		{
			NBTBase value = tags.get(key);
			sb.append("\n").append(prefix).append(" (").append(key).append(':');
			sb.append(value.createString(prefix + " ")).append(')');
		}
		
		sb.append("\n" + prefix + "]");
		return sb.toString();
	}

	@Override
	public void readValueString(String dataString)
	{
		int pos1 = dataString.indexOf('[') + 1;
		int pos2 = dataString.lastIndexOf(']');
		if (pos1 < 0 || pos2 < 0)
			return;
		dataString = dataString.substring(pos1, pos2).trim();
		for (String sub : NBTTagCompound.split(dataString))
		{	
			int point = sub.indexOf(':');
			String tagID = sub.substring(0, point);
			String tag = sub.substring(point + 1, sub.length());
			NBTBase base = NBTParser.parseTag(tag);
			this.setTag(Integer.parseInt(tagID), base);
		}
	}
}
