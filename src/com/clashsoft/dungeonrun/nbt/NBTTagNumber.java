package com.clashsoft.dungeonrun.nbt;

public abstract class NBTTagNumber extends NBTBase
{
	private Number	value;
	
	public NBTTagNumber(byte type, String name, Number value)
	{
		super(type, name, value);
		this.value = value;
	}
	
	@Override
	public boolean valueEquals(NBTBase that)
	{
		return value.equals(((NBTTagNumber) that).value);
	}
	
	@Override
	public final String writeValueString(String prefix)
	{
		return value.toString() + getPostfixChar();
	}
	
	@Override
	public void readValueString(String dataString)
	{
		value = readNumber(dataString.substring(0, dataString.indexOf(getPostfixChar())));
	}
	
	public abstract char getPostfixChar();
	public abstract Number readNumber(String number);
}
