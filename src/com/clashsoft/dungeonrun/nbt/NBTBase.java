package com.clashsoft.dungeonrun.nbt;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import com.clashsoft.dungeonrun.util.FileCompressing;

public abstract class NBTBase
{
	public static final boolean DELETE_COMPRESSED_FILES = false;
	
	public static byte TYPE_COMPOUND = 0;
	public static byte TYPE_LIST = 1;
	public static byte TYPE_BOOLEAN = 2;
	public static byte TYPE_BYTE = 3;
	public static byte TYPE_SHORT = 4;
	public static byte TYPE_INT = 5;
	public static byte TYPE_FLOAT = 6;
	public static byte TYPE_DOUBLE = 7;
	public static byte TYPE_LONG = 8;
	public static byte TYPE_STRING = 9;
	
	public String name;
	public byte type;
	
	public NBTBase(byte type, String name)
	{
		this.type = type;
		this.name = name;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + this.type;
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NBTBase other = (NBTBase) obj;
		if (this.name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!this.name.equals(other.name))
			return false;
		if (this.type != other.type)
			return false;
		if (!valueEquals(other))
			return false;
		return true;
	}

	public abstract boolean valueEquals(NBTBase that);
	
	public boolean serialize(File out, boolean compressed)
	{
		try
		{
			if (!out.exists())
				out.createNewFile();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(out));
			writer.write(createString(""));
			writer.close();
			
			if (compressed)
			{
				FileCompressing.compressFile(out, new File(out.getAbsolutePath() + ".drc"));
				// out is just a temporary file used for compressing
				if (DELETE_COMPRESSED_FILES)
					out.delete();
			}
			
			return true;
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
			return false;
		}
	}
	
	public final String createString(String prefix)
	{
		return "{t:" + this.type + "n:[" + this.name + "]v:[" + this.writeValueString(prefix) + "]}";
	}
	
	public abstract String writeValueString(String prefix);
	
	public abstract void readValueString(String dataString);
	
	public static NBTBase deserialize(File in, boolean compressed)
	{
		File in1 = compressed ? new File(in.getAbsolutePath() + ".drc") : in;
		
		if (in1 == null || !in1.exists())
			return null;
		
		if (compressed)
			in = FileCompressing.decompressFile(in1, in);
		
		try
		{
			List<String> lines = Files.readAllLines(in.toPath(), Charset.defaultCharset());
			return NBTParser.parse(lines);
		}
		catch (IOException ioex)
		{
			return null;
		}
	}
	
	public static NBTBase createFromObject(String tagName, Object value)
	{
		if (value instanceof Boolean)
			return new NBTTagBoolean(tagName, (boolean)value);
		if (value instanceof Byte)
			return new NBTTagByte(tagName, (byte)value);
		if (value instanceof Short)
			return new NBTTagShort(tagName, (short)value);
		if (value instanceof Integer)
			return new NBTTagInteger(tagName, (int)value);
		if (value instanceof Float)
			return new NBTTagFloat(tagName, (float)value);
		if (value instanceof Double)
			return new NBTTagDouble(tagName, (double)value);
		if (value instanceof Long)
			return new NBTTagLong(tagName, (long)value);
		if (value instanceof String)
			return new NBTTagString(tagName, (String)value);
		if (value instanceof NBTTagCompound)
			return (NBTTagCompound)value;
		if (value instanceof NBTTagList)
			return (NBTTagList)value;
		return null;
	}
	
	public static NBTBase createFromType(String tagName, byte type)
	{
		if (type == TYPE_BOOLEAN)
			return new NBTTagBoolean(tagName, false);
		if (type == TYPE_BYTE)
			return new NBTTagByte(tagName, (byte) 0);
		if (type == TYPE_SHORT)
			return new NBTTagShort(tagName, (short) 0);
		if (type == TYPE_INT)
			return new NBTTagInteger(tagName, 0);
		if (type == TYPE_FLOAT)
			return new NBTTagFloat(tagName, 0F);
		if (type == TYPE_DOUBLE)
			return new NBTTagDouble(tagName, 0D);
		if (type == TYPE_LONG)
			return new NBTTagLong(tagName, 0L);
		if (type == TYPE_STRING)
			return new NBTTagString(tagName, "");
		if (type == TYPE_COMPOUND)
			return new NBTTagCompound(tagName);
		if (type == TYPE_LIST)
			return new NBTTagList(tagName);
		return null;
	}
}
