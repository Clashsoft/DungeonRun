package com.clashsoft.dungeonrun.nbt;

public class NBTTagShort extends NBTTagNumber
{
	public short value;
	
	public NBTTagShort(String name, short value)
	{
		super(TYPE_SHORT, name, value);
		this.value = value;
	}

	@Override
	public char getPostfixChar()
	{
		return 0;
	}

	@Override
	public Number readNumber(String number)
	{
		return null;
	}
}
