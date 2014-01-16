package com.clashsoft.dungeonrun.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagBoolean extends NBTBase
{
	public boolean	value;
	
	public NBTTagBoolean(String name, boolean value)
	{
		super(TYPE_BOOLEAN, name, value);
		this.value = value;
	}
	
	@Override
	public String writeValueString(String prefix)
	{
		return this.value ? "t" : "f";
	}
	
	@Override
	public void readValueString(String dataString)
	{
		this.value = "t".equals(dataString);
	}
	
	@Override
	public boolean valueEquals(NBTBase that)
	{
		return this.value == ((NBTTagBoolean) that).value;
	}

	@Override
	public void writeValue(DataOutput output) throws IOException
	{
		output.writeBoolean(this.value);
	}

	@Override
	public void readValue(DataInput input) throws IOException
	{
		this.value = input.readBoolean();
	}
}
