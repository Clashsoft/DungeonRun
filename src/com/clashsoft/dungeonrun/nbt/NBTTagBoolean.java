package com.clashsoft.dungeonrun.nbt;

public class NBTTagBoolean extends NBTBase
{
	private static final long	serialVersionUID	= -6331780646733355611L;
	
	public boolean value;
	
	public NBTTagBoolean(String name, boolean value)
	{
		super(TYPE_BOOLEAN, name);
		this.value = value;
	}
	
}
