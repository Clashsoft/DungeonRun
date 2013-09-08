package com.clashsoft.dungeonrun.nbt;

public class NBTTagByte extends NBTBase
{
	private static final long	serialVersionUID	= -504922857878998412L;
	
	public byte value;
	
	public NBTTagByte(String name, byte value)
	{
		super(TYPE_BYTE, name);
		this.value = value;
	}
	
}
