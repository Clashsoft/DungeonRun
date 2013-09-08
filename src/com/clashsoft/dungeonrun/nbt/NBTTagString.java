package com.clashsoft.dungeonrun.nbt;

public class NBTTagString extends NBTBase
{
	private static final long	serialVersionUID	= -4178290220113527124L;

	public String value;
	
	public NBTTagString(String name, String value)
	{
		super(TYPE_STRING, name);
		this.value = value;
	}
	
}
