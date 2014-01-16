package com.clashsoft.dungeonrun.nbt;

import java.util.Collection;

public class NBTParser
{
	public static NBTBase parse(Collection<String> lines)
	{
		String allLines = convert(lines);
		
		return parseTag(allLines);
	}
	
	public static NBTBase parseTag(String tag)
	{
		StringBuilder tagType = new StringBuilder();
		StringBuilder tagName = new StringBuilder();
		StringBuilder tagValue = new StringBuilder();
		
		int PABDEPTH = 0; // Depth of ( )
		int SQBDEPTH = 0; // Depth of [ ]
		int CUBDEPTH = 0; // Depth of { }
		boolean quote = false;
		
		int next = -1;
		boolean nextValid = false;
		
		int i0 = tag.length();
		for (int i = 0; i < i0; i++)
		{
			char c = tag.charAt(i);
			
			if (c == '"' && !(i > 0 && tag.charAt(i - 1) == '\\'))
			{
				quote = !quote;
			}
			
			if (!quote)
			{
				if (c == '(')
				{
					PABDEPTH++;
					continue;
				}
				if (c == '[')
				{
					SQBDEPTH++;
					continue;
				}
				if (c == '{')
				{
					CUBDEPTH++;
					continue;
				}
				if (c == ')')
				{
					PABDEPTH--;
				}
				if (c == ']')
				{
					SQBDEPTH--;
				}
				if (c == '}')
				{
					CUBDEPTH--;
				}
				
				if (CUBDEPTH == 1 && PABDEPTH == 0 && SQBDEPTH == 0)
				{
					if (c == ':')
					{
						nextValid = true;
						continue;
					}
					
					if (c == 't')
					{
						next = 0;
						nextValid = false;
					}
					else if (c == 'n')
					{
						next = 1;
						nextValid = false;
					}
					else if (c == 'v')
					{
						next = 2;
						nextValid = false;
					}
					
				}
			}
			
			if (nextValid || quote)
			{
				if (SQBDEPTH == 0 && next == 0)
				{
					tagType.append(c);
				}
				else if (SQBDEPTH == 1 && next == 1)
				{
					tagName.append(c);
				}
				else if (PABDEPTH == 0 && SQBDEPTH == 1 && CUBDEPTH == 1 && next == 2) // Value
				{
					int i1 = tag.lastIndexOf("}") - 1;
					tagValue.append(tag.substring(i, i1));
					break;
				}
			}
		}
		
		return parseTag2(tagType.toString(), tagName.toString(), tagValue.toString());
	}
	
	private static NBTBase parseTag2(String tagType, String tagName, String tagValue)
	{
		byte type = Byte.parseByte(tagType);
		NBTBase base = NBTBase.createFromType(tagName, type);
		if (base != null)
		{
			base.readValueString(tagValue);
		}
		return base;
	}
	
	private static String convert(Collection<String> lines)
	{
		StringBuilder sb = new StringBuilder(lines.size() * 10);
		for (String s : lines)
		{
			sb.append(s).append('\n');
		}
		return sb.toString();
	}
}
