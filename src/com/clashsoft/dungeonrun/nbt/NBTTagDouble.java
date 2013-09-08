package com.clashsoft.dungeonrun.nbt;

public class NBTTagDouble extends NBTBase
{
	private static final long	serialVersionUID	= -7468255372720922451L;
	
	public double value;

	public NBTTagDouble(String name, double value)
	{
		super(TYPE_DOUBLE, name);
		this.value = value;
	}
	
}
