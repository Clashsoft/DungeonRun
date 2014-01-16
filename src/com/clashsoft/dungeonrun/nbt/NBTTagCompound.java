package com.clashsoft.dungeonrun.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NBTTagCompound extends NBTBase
{
	private Map<String, NBTBase>	tags	= new HashMap<>();
	
	public NBTTagCompound(String name)
	{
		super(TYPE_COMPOUND, name, null);
	}
	
	@Override
	public Map<String, NBTBase> getValue()
	{
		return this.tags;
	}
	
	public boolean setTag(String name, NBTBase tag)
	{
		if (name.contains("[") || name.contains("]") || name.contains("{") || name.contains("}"))
		{
			throw new IllegalArgumentException("Name must not contain [ ] { } !");
		}
		boolean ret = this.tags.containsKey(name);
		this.tags.put(name, tag);
		return ret;
	}
	
	public boolean setTag(NBTBase tag)
	{
		return this.setTag(tag.name, tag);
	}
	
	public boolean hasTag(String name)
	{
		return this.tags.containsKey(name);
	}
	
	public NBTBase getTag(String name)
	{
		return this.tags.get(name);
	}
	
	public void setBoolean(String name, boolean value)
	{
		this.setTag(new NBTTagBoolean(name, value));
	}
	
	public void setByte(String name, byte value)
	{
		this.setTag(new NBTTagByte(name, value));
	}
	
	public void setShort(String name, short value)
	{
		this.setTag(new NBTTagShort(name, value));
	}
	
	public void setInteger(String name, int value)
	{
		this.setTag(new NBTTagInteger(name, value));
	}
	
	public void setFloat(String name, float value)
	{
		this.setTag(new NBTTagFloat(name, value));
	}
	
	public void setDouble(String name, double value)
	{
		this.setTag(new NBTTagDouble(name, value));
	}
	
	public void setLong(String name, long value)
	{
		this.setTag(new NBTTagLong(name, value));
	}
	
	public void setString(String name, String value)
	{
		this.setTag(new NBTTagString(name, value));
	}
	
	public void setTagList(NBTTagList list)
	{
		this.setTag(list);
	}
	
	public void setTagCompound(NBTTagCompound compound)
	{
		if (compound != this)
		{
			this.setTag(compound);
		}
	}
	
	public boolean getBoolean(String name)
	{
		return ((NBTTagBoolean) this.getTag(name)).value;
	}
	
	public byte getByte(String name)
	{
		return ((NBTTagByte) this.getTag(name)).value;
	}
	
	public short getShort(String name)
	{
		return ((NBTTagShort) this.getTag(name)).value;
	}
	
	public int getInteger(String name)
	{
		return ((NBTTagInteger) this.getTag(name)).value;
	}
	
	public float getFloat(String name)
	{
		return ((NBTTagFloat) this.getTag(name)).value;
	}
	
	public double getDouble(String name)
	{
		return ((NBTTagDouble) this.getTag(name)).value;
	}
	
	public long getLong(String name)
	{
		NBTTagLong tag = (NBTTagLong) this.getTag(name);
		return tag != null ? tag.value : 0L;
	}
	
	public String getString(String name)
	{
		NBTTagString tag = (NBTTagString) this.getTag(name);
		return tag != null ? tag.value : "";
	}
	
	public NBTTagList getTagList(String name)
	{
		return (NBTTagList) this.getTag(name);
	}
	
	public NBTTagCompound getTagCompound(String name)
	{
		return (NBTTagCompound) this.getTag(name);
	}
	
	public void clear()
	{
		this.tags.clear();
	}
	
	@Override
	public boolean valueEquals(NBTBase that)
	{
		return this.tags.equals(((NBTTagCompound) that).tags);
	}
	
	@Override
	public String writeValueString(String prefix)
	{
		StringBuilder sb = new StringBuilder(this.tags.size() * 100);
		
		sb.append("\n" + prefix + "{");
		
		for (String key : this.tags.keySet())
		{
			NBTBase value = this.tags.get(key);
			sb.append("\n").append(prefix).append(" (").append(key).append(':');
			sb.append(value.createString(prefix + " ")).append(')');
		}
		
		sb.append("\n" + prefix + "}");
		return sb.toString();
	}
	
	@Override
	public void readValueString(String dataString)
	{
		int pos1 = dataString.indexOf('{') + 1;
		int pos2 = dataString.lastIndexOf('}');
		if (pos1 < 0 || pos2 < 0)
		{
			return;
		}
		
		dataString = dataString.substring(pos1, pos2).trim();
		
		for (String sub : split(dataString))
		{
			int point = sub.indexOf(':');
			String tagName = sub.substring(0, point);
			String tag = sub.substring(point + 1, sub.length());
			NBTBase base = NBTParser.parseTag(tag);
			this.setTag(tagName, base);
		}
	}
	
	protected static List<String> split(String text)
	{
		List<String> result = new ArrayList<String>();
		
		int PABDEPTH = 0; // Depth of ( )
		int SQBDEPTH = 0; // Depth of [ ]
		int CUBDEPTH = 0; // Depth of { }
		boolean quote = false;
		
		String tag = "";
		int index = -1;
		for (int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			
			if (c == '"' && !(i > 0 && text.charAt(i - 1) == '\\'))
			{
				quote = !quote;
			}
			
			if (!quote)
			{
				if (c == '(')
				{
					if (PABDEPTH == 0)
					{
						index = i;
					}
					PABDEPTH++;
					continue;
				}
				else if (c == ')')
				{
					PABDEPTH--;
					
					if (PABDEPTH == 0 && index != -1)
					{
						tag = text.substring(index + 1, i);
						result.add(tag);
					}
				}
			}
			else
			{
				tag += c;
			}
		}
		return result;
	}
	
	@Override
	public void writeValue(DataOutput output) throws IOException
	{
		for (String key : this.tags.keySet())
		{
			NBTBase value = this.tags.get(key);
			value.write(output);
		}
		output.writeByte(0);
	}
	
	@Override
	public void readValue(DataInput input) throws IOException
	{
		while (true)
		{
			NBTBase nbt = NBTBase.createFromData(input);
			
			if (nbt == null)
			{
				break;
			}
			this.setTag(nbt);
		}
	}
}
