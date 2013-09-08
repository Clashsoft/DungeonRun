package com.clashsoft.dungeonrun.nbt;

import java.io.Serializable;

public abstract class NBTBase implements Serializable
{
	private static final long	serialVersionUID	= 6990668336582677789L;
	
	public static byte TYPE_COMPOUND = 0;
	public static byte TYPE_LIST = 1;
	public static byte TYPE_BOOLEAN = 2;
	public static byte TYPE_BYTE = 3;
	public static byte TYPE_SHORT = 4;
	public static byte TYPE_INT = 5;
	public static byte TYPE_FLOAT = 6;
	public static byte TYPE_DOUBLE = 7;
	public static byte TYPE_STRING = 8;
	
	public String name;
	public byte type;
	
	public NBTBase(byte type, String name)
	{
		this.type = type;
		this.name = name;
	}
}
