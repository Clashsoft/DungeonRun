package com.clashsoft.dungeonrun.nbt;

public class NBTTagInteger extends NBTBase
{
	private static final long	serialVersionUID	= -7242077016744146107L;
	
	public int value;
	
	public NBTTagInteger(String name, int value)
	{
		super(TYPE_INT, name);
		this.value = value;
	}	
}
