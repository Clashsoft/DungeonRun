package com.clashsoft.dungeonrun.nbt;

public class NBTTagString extends NBTBase
{
	public String	value;
	
	public NBTTagString(String name, String value)
	{
		super(TYPE_STRING, name, value);
		this.value = value;
	}
	
	@Override
	public boolean valueEquals(NBTBase that)
	{
		return value.equals(((NBTTagString) that).value);
	}
	
	@Override
	public String writeValueString(String prefix)
	{
		return "\"" + value.replace("\"", "\\\"") + "\"";
	}
	
	@Override
	public void readValueString(String dataString)
	{
		this.value = dataString.substring(1, dataString.length() - 1).replace("\\\"", "\"");
	}
}
