package com.clashsoft.dungeonrun.util;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class StringUtils
{
	public static String[] split(String text, char splitChar)
	{
		StringBuilder current = new StringBuilder();
		List<String> result = new ArrayList<String>();
		boolean quote = false;
		
		for (int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			
			if (c == splitChar && !quote)
			{
				result.add(current.toString());
				current.delete(0, current.length());
			}
			else
				current.append(c);
			
			if (c == '"' && i > 0 && text.charAt(i - 1) != '\\')
				quote = !quote;
		}
		result.add(current.toString());
		
		return result.toArray(new String[result.size()]);
	}
	
	public static Object parse(String type, String text)
	{
		switch (type)
		{
		case "s":
			return text;
		case "i":
			return Integer.valueOf(text);
		case "f":
			return Float.valueOf(text);
		case "d":
			return Double.valueOf(text);
		case "l":
			return Long.valueOf(text);
		case "b":
			return Boolean.valueOf(text);
		case "D":
			return Date.valueOf(text);
		}
		return null;
	}
}
