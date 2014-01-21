package com.clashsoft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd extends NBTBase
{
	public NBTTagEnd()
	{
		super(TYPE_END, "");
	}
	
	@Override
	public Object getValue()
	{
		return null;
	}

	@Override
	public boolean valueEquals(NBTBase that)
	{
		return false;
	}
	
	@Override
	public void writeValue(DataOutput output) throws IOException
	{
	}
	
	@Override
	public void readValue(DataInput input) throws IOException
	{
	}
	
	@Override
	public String writeValueString(String prefix)
	{
		return null;
	}
	
	@Override
	public void readValueString(String dataString)
	{
	}
}
