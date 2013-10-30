package com.clashsoft.dungeonrun.nbt;

public class NBTTagFloat extends NBTTagNumber
{
	public float	value;
	
	public NBTTagFloat(String name, float value)
	{
		super(TYPE_FLOAT, name, value);
		this.value = value;
	}
	
	@Override
	public char getPostfixChar()
	{
		return 'F';
	}
	
	@Override
	public Number readNumber(String number)
	{
		return Float.parseFloat(number);
	}
}
