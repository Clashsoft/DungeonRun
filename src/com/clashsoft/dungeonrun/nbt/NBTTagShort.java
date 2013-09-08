package com.clashsoft.dungeonrun.nbt;

public class NBTTagShort extends NBTBase
{
	private static final long	serialVersionUID	= -6536877672984666056L;

	public short value;
	
	public NBTTagShort(String name, short value)
	{
		super(TYPE_SHORT, name);
		this.value = value;
	}
}
