package com.clashsoft.dungeonrun.nbt;

public class NBTTagDouble extends NBTTagNumber
{	
	public double value;

	public NBTTagDouble(String name, double value)
	{
		super(TYPE_DOUBLE, name, value);
		this.value = value;
	}

	@Override
	public char getPostfixChar()
	{
		return 'D';
	}

	@Override
	public Number readNumber(String number)
	{
		return value = Double.parseDouble(number);
	}
}
