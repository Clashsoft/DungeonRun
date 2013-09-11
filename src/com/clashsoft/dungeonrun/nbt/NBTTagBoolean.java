package com.clashsoft.dungeonrun.nbt;

public class NBTTagBoolean extends NBTBase
{	
	public boolean value;
	
	public NBTTagBoolean(String name, boolean value)
	{
		super(TYPE_BOOLEAN, name, value);
		this.value = value;
	}

	@Override
	public String writeValueString(String prefix)
	{
		return (value ? "t" : "f");
	}

	@Override
	public void readValueString(String dataString)
	{
		this.value = dataString.equals("t");
	}

	@Override
	public boolean valueEquals(NBTBase that)
	{
		return this.value == ((NBTTagBoolean)that).value;
	}
}
