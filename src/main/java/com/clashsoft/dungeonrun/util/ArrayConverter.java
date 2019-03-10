package com.clashsoft.dungeonrun.util;

public class ArrayConverter
{
	public static Integer[] convertIntArray(int[] array)
	{
		Integer[] array1 = new Integer[array.length];
		for (int i = 0; i < array.length; i++)
		{
			array1[i] = Integer.valueOf(array[i]);
		}
		return array1;
	}
}
