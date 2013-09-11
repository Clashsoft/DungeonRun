package com.clashsoft.dungeonrun.nbt;

public class NBTTagLong extends NBTTagNumber
{	
	public long value;
	
	public NBTTagLong(String name, long value)
	{
		super(TYPE_LONG, name, value);
		this.value = value;
	}

	@Override
	public char getPostfixChar()
	{
		return 'L';
	}

	@Override
	public Number readNumber(String number)
	{
		return Long.parseLong(number);
	}
}
