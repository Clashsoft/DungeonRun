package com.clashsoft.dungeonrun.nbt;

public class NBTTagFloat extends NBTBase
{
	private static final long	serialVersionUID	= -2659524157210463666L;
	
	public float value;
	
	public NBTTagFloat(String name, float value)
	{
		super(TYPE_FLOAT, name);
		this.value = value;
	}
	
}
