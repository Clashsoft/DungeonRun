package com.clashsoft.dungeonrun.nbt;

public class NBTTagInteger extends NBTTagNumber
{
	public int	value;
	
	public NBTTagInteger(String name, int value)
	{
		super(TYPE_INT, name, value);
		this.value = value;
	}
	
	@Override
	public char getPostfixChar()
	{
		return 'I';
	}
	
	@Override
	public Number readNumber(String number)
	{
		return value = Integer.parseInt(number);
	}
}
