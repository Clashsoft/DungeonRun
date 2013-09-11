package com.clashsoft.dungeonrun.nbt;

public class NBTTagByte extends NBTTagNumber
{	
	public byte value;
	
	public NBTTagByte(String name, byte value)
	{
		super(TYPE_BYTE, name, value);
		this.value = value;
	}

	@Override
	public char getPostfixChar()
	{
		return 'B';
	}

	@Override
	public Number readNumber(String number)
	{
		return value = Byte.parseByte(number);
	}
}
