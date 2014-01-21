package com.clashsoft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;

public class NBTTagArray<T extends NBTBase> extends NBTBase
{
	public static byte	TYPE_OBJECT	= 0;
	
	private int			length;
	
	private byte		subtype;
	
	private NBTBase[]	objectArray;
	private boolean[]	booleanArray;
	private byte[]		byteArray;
	private short[]		shortArray;
	private int[]		intArray;
	private long[]		longArray;
	private float[]		floatArray;
	private double[]	doubleArray;
	private String[]	stringArray;
	
	public static <T extends NBTBase> NBTTagArray create(String name, Class<T> type, int length)
	{
		return new NBTTagArray<T>(name, type, length);
	}
	
	public static NBTTagArray createBoolean(String name, int length)
	{
		return createBoolean(name, new boolean[length]);
	}
	
	public static NBTTagArray createByte(String name, int length)
	{
		return createByte(name, new byte[length]);
	}
	
	public static NBTTagArray createShort(String name, int length)
	{
		return createShort(name, new short[length]);
	}
	
	public static NBTTagArray createInt(String name, int length)
	{
		return createInt(name, new int[length]);
	}
	
	public static NBTTagArray createLong(String name, int length)
	{
		return createLong(name, new long[length]);
	}
	
	public static NBTTagArray createFloat(String name, int length)
	{
		return createFloat(name, new float[length]);
	}
	
	public static NBTTagArray createDouble(String name, int length)
	{
		return createDouble(name, new double[length]);
	}
	
	public static NBTTagArray createString(String name, int length)
	{
		return createString(name, new String[length]);
	}
	
	public static <T extends NBTBase> NBTTagArray create(String name, T[] objectArray)
	{
		return new NBTTagArray<T>(name, TYPE_OBJECT).setObjectArray(objectArray);
	}
	
	public static NBTTagArray createBoolean(String name, boolean[] booleanArray)
	{
		return new NBTTagArray(name, TYPE_BOOLEAN).setBooleanArray(booleanArray);
	}
	
	public static NBTTagArray createByte(String name, byte[] byteArray)
	{
		return new NBTTagArray(name, TYPE_BYTE).setByteArray(byteArray);
	}
	
	public static NBTTagArray createShort(String name, short[] shortArray)
	{
		return new NBTTagArray(name, TYPE_SHORT).setShortArray(shortArray);
	}
	
	public static NBTTagArray createInt(String name, int[] intArray)
	{
		return new NBTTagArray(name, TYPE_INT).setIntArray(intArray);
	}
	
	public static NBTTagArray createLong(String name, long[] longArray)
	{
		return new NBTTagArray(name, TYPE_LONG).setLongArray(longArray);
	}
	
	public static NBTTagArray createFloat(String name, float[] floatArray)
	{
		return new NBTTagArray(name, TYPE_FLOAT).setFloatArray(floatArray);
	}
	
	public static NBTTagArray createDouble(String name, double[] doubleArray)
	{
		return new NBTTagArray(name, TYPE_DOUBLE).setDoubleArray(doubleArray);
	}
	
	public static NBTTagArray createString(String name, String[] array)
	{
		return new NBTTagArray(name, TYPE_STRING).setStringArray(array);
	}
	
	/**
	 * Constructor used for deserialization
	 * @param name
	 */
	public NBTTagArray(String name)
	{
		super(TYPE_ARRAY, name);
	}
	
	protected NBTTagArray(String name, byte type)
	{
		this(name);
		this.subtype = type;
		this.length = 0;
	}
	
	protected NBTTagArray(String name, Class<T> type, int length)
	{
		this(name, (byte) 0);
		this.objectArray = (NBTBase[]) Array.newInstance(type, length);
	}
	
	@Override
	public Object getValue()
	{
		return this.objectArray;
	}
	
	@Override
	public void writeValue(DataOutput output) throws IOException
	{
		output.writeByte(this.subtype);
		int len = this.length;
		output.writeInt(len);
		if (this.objectArray != null)
		{
			for (int i = 0; i < len; i++)
			{
				this.objectArray[i].write(output);
			}
		}
		else if (this.booleanArray != null)
		{
			for (int i = 0; i < len; i++)
			{
				output.writeBoolean(this.booleanArray[i]);
			}
		}
		else if (this.byteArray != null)
		{
			for (int i = 0; i < len; i++)
			{
				output.writeByte(this.byteArray[i]);
			}
		}
		else if (this.shortArray != null)
		{
			for (int i = 0; i < len; i++)
			{
				output.writeShort(this.shortArray[i]);
			}
		}
		else if (this.intArray != null)
		{
			for (int i = 0; i < len; i++)
			{
				output.writeInt(this.intArray[i]);
			}
		}
		else if (this.longArray != null)
		{
			for (int i = 0; i < len; i++)
			{
				output.writeLong(this.longArray[i]);
			}
		}
		else if (this.floatArray != null)
		{
			for (int i = 0; i < len; i++)
			{
				output.writeFloat(this.floatArray[i]);
			}
		}
		else if (this.doubleArray != null)
		{
			for (int i = 0; i < len; i++)
			{
				output.writeDouble(this.doubleArray[i]);
			}
		}
		else if (this.stringArray != null)
		{
			for (int i = 0; i < len; i++)
			{
				output.writeUTF(this.stringArray[i]);
			}
		}
	}
	
	@Override
	public void readValue(DataInput input) throws IOException
	{
		byte type = input.readByte();
		int len = input.readInt();
		
		this.subtype = type;
		this.length = type;
		
		if (type == TYPE_END)
		{
			this.objectArray = new NBTBase[len];
			for (int i = 0; i < len; i++)
			{
				this.objectArray[i] = NBTBase.read(input);
			}
		}
		else if (type == TYPE_BOOLEAN)
		{
			this.booleanArray = new boolean[len];
			for (int i = 0; i < len; i++)
			{
				this.booleanArray[i] = input.readBoolean();
			}
		}
		else if (type == TYPE_BYTE)
		{
			this.byteArray = new byte[len];
			for (int i = 0; i < len; i++)
			{
				this.byteArray[i] = input.readByte();
			}
		}
		else if (type == TYPE_SHORT)
		{
			this.shortArray = new short[len];
			for (int i = 0; i < len; i++)
			{
				this.shortArray[i] = input.readShort();
			}
		}
		else if (type == TYPE_INT)
		{
			this.intArray = new int[len];
			for (int i = 0; i < len; i++)
			{
				this.intArray[i] = input.readInt();
			}
		}
		else if (type == TYPE_LONG)
		{
			this.longArray = new long[len];
			for (int i = 0; i < len; i++)
			{
				this.longArray[i] = input.readLong();
			}
		}
		else if (type == TYPE_FLOAT)
		{
			this.floatArray = new float[len];
			for (int i = 0; i < len; i++)
			{
				this.floatArray[i] = input.readFloat();
			}
		}
		else if (type == TYPE_DOUBLE)
		{
			this.doubleArray = new double[len];
			for (int i = 0; i < len; i++)
			{
				this.doubleArray[i] = input.readDouble();
			}
		}
		else if (type == TYPE_STRING)
		{
			this.stringArray = new String[len];
			for (int i = 0; i < len; i++)
			{
				this.stringArray[i] = input.readUTF();
			}
		}
	}
	
	@Override
	public String writeValueString(String prefix)
	{
		return null;
	}
	
	@Override
	public void readValueString(String dataString)
	{
	}
	
	public NBTBase[] getObjectArray()
	{
		return this.objectArray;
	}
	
	public boolean[] getBooleanArray()
	{
		return this.booleanArray;
	}
	
	public byte[] getByteArray()
	{
		return this.byteArray;
	}
	
	public short[] getShortArray()
	{
		return this.shortArray;
	}
	
	public int[] getIntArray()
	{
		return this.intArray;
	}
	
	public long[] getLongArray()
	{
		return this.longArray;
	}
	
	public float[] getFloatArray()
	{
		return this.floatArray;
	}
	
	public double[] getDoubleArray()
	{
		return this.doubleArray;
	}
	
	public String[] getStringArray()
	{
		return this.stringArray;
	}
	
	protected NBTTagArray setObjectArray(NBTBase[] objects)
	{
		this.objectArray = objects;
		this.length = objects.length;
		return this;
	}
	
	protected NBTTagArray setBooleanArray(boolean[] booleanArray)
	{
		this.booleanArray = booleanArray;
		this.length = booleanArray.length;
		return this;
	}
	
	protected NBTTagArray setByteArray(byte[] byteArray)
	{
		this.byteArray = byteArray;
		this.length = byteArray.length;
		return this;
	}
	
	protected NBTTagArray setShortArray(short[] shortArray)
	{
		this.shortArray = shortArray;
		this.length = shortArray.length;
		return this;
	}
	
	protected NBTTagArray setIntArray(int[] intArray)
	{
		this.intArray = intArray;
		this.length = intArray.length;
		return this;
	}
	
	protected NBTTagArray setLongArray(long[] longArray)
	{
		this.longArray = longArray;
		this.length = longArray.length;
		return this;
	}
	
	protected NBTTagArray setFloatArray(float[] floatArray)
	{
		this.floatArray = floatArray;
		this.length = floatArray.length;
		return this;
	}
	
	protected NBTTagArray setDoubleArray(double[] doubleArray)
	{
		this.doubleArray = doubleArray;
		this.length = doubleArray.length;
		return this;
	}
	
	protected NBTTagArray setStringArray(String[] stringArray)
	{
		this.stringArray = stringArray;
		this.length = stringArray.length;
		return this;
	}
}
