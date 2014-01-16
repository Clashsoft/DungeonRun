package com.clashsoft.dungeonrun.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NBTTagList extends NBTBase implements Iterable<NBTBase>
{
	private ArrayList<NBTBase>	tags;
	
	public NBTTagList(String name)
	{
		this(name, 10);
	}
	
	public NBTTagList(String name, int capacity)
	{
		super(TYPE_LIST, name, null);
		this.tags = new ArrayList(capacity);
	}
	
	@Override
	public ArrayList<NBTBase> getValue()
	{
		return this.tags;
	}
	
	public void addTag(NBTBase tag)
	{
		this.tags.add(tag);
	}
	
	public void addTag(String name, NBTBase tag)
	{
		tag.name = name;
		this.addTag(tag);
	}
	
	public void setTag(int index, NBTBase tag)
	{
		this.ensureSize(index + 1);
		this.tags.set(index, tag);
	}
	
	private void ensureSize(int size)
	{
		this.tags.ensureCapacity(size);
		while (this.tags.size() < size)
		{
			this.tags.add(null);
		}
	}
	
	public int tagCount()
	{
		return this.tags.size();
	}
	
	public NBTBase tagAt(int index)
	{
		return this.tags.get(index);
	}
	
	@Override
	public Iterator iterator()
	{
		return this.tags.iterator();
	}
	
	public void addBoolean(String name, boolean value)
	{
		this.addTag(name, new NBTTagBoolean(name, value));
	}
	
	public void addByte(String name, byte value)
	{
		this.addTag(name, new NBTTagByte(name, value));
	}
	
	public void addShort(String name, short value)
	{
		this.addTag(name, new NBTTagShort(name, value));
	}
	
	public void addInteger(String name, int value)
	{
		this.addTag(name, new NBTTagInteger(name, value));
	}
	
	public void addFloat(String name, float value)
	{
		this.addTag(name, new NBTTagFloat(name, value));
	}
	
	public void addDouble(String name, double value)
	{
		this.addTag(name, new NBTTagDouble(name, value));
	}
	
	public void addLong(String name, long value)
	{
		this.addTag(name, new NBTTagLong(name, value));
	}
	
	public void addString(String name, String value)
	{
		this.addTag(name, new NBTTagString(name, value));
	}
	
	public void addTagList(NBTTagList list)
	{
		if (list != this)
		{
			this.addTag(this.name, list);
		}
	}
	
	public void addTagCompound(NBTTagCompound compound)
	{
		this.addTag(compound);
	}
	
	public static <T> NBTTagList fromArray(String name, T[] args)
	{
		NBTTagList list = new NBTTagList(name, args.length);
		for (int i = 0; i < args.length; i++)
		{
			String tagName = name + "@" + i;
			NBTBase base = NBTBase.createFromObject(tagName, args[i]);
			if (base != null)
			{
				list.addTag(base);
			}
		}
		return list;
	}
	
	public static NBTTagList fromList(String name, List args)
	{
		NBTTagList list = new NBTTagList(name, args.size());
		for (int i = 0; i < args.size(); i++)
		{
			String tagName = name + "@" + i;
			NBTBase base = NBTBase.createFromObject(tagName, args.get(i));
			if (base != null)
			{
				list.addTag(base);
			}
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
		return (T[]) this.tags.toArray();
	}
	
	public int[] toIntArray()
	{
		int[] array = new int[this.tagCount()];
		for (int i = 0; i < this.tagCount(); i++)
		{
			NBTBase base = this.tagAt(i);
			if (base instanceof NBTTagNumber)
			{
				array[i] = ((NBTTagInteger) base).value;
			}
		}
		return array;
	}
	
	public float[] toFloatArray()
	{
		float[] array = new float[this.tagCount()];
		for (int i = 0; i < this.tagCount(); i++)
		{
			NBTBase base = this.tagAt(i);
			if (base instanceof NBTTagNumber)
			{
				array[i] = ((NBTTagFloat) base).value;
			}
		}
		return array;
	}
	
	@Override
	public boolean valueEquals(NBTBase that)
	{
		return this.tags.equals(((NBTTagList) that).tags);
	}
	
	@Override
	public String writeValueString(String prefix)
	{
		StringBuilder sb = new StringBuilder(this.tags.toString().length() * 8);
		
		sb.append("\n" + prefix + "[");
		
		for (int key = 0; key < this.tags.size(); key++)
		{
			NBTBase value = this.tags.get(key);
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
		{
			return;
		}
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
	
	@Override
	public void writeValue(DataOutput output) throws IOException
	{
		for (int i = 0; i < this.tagCount(); i++)
		{
			NBTBase value = this.tagAt(i);
			value.write(output);
		}
		output.writeByte(0);
	}
	
	@Override
	public void readValue(DataInput input) throws IOException
	{
		while (true)
		{
			NBTBase nbt = NBTBase.createFromData(input);
			
			if (nbt == null)
			{
				break;
			}
			this.addTag(nbt);
		}
	}
}
