package com.clashsoft.nbt.loader;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import com.clashsoft.nbt.NBTBase;

public class NBTSerializer
{
	public static boolean	DELETE_COMPRESSED_FILES	= false;
	public static boolean	BYTE_STORAGE			= true;
	
	public static boolean useString()
	{
		return !BYTE_STORAGE;
	}
	
	public static NBTBase deserialize(File in, boolean compressed)
	{
		if (in == null || !in.exists())
		{
			return null;
		}
		
		try
		{
			if (useString())
			{
				if (compressed)
				{
					File in1 = new File(in.getAbsolutePath() + ".drc");
					
					if (in1 == null || !in1.exists())
					{
						return null;
					}
					
					in = FileCompressing.decompressFile(in1, in);
				}
				
				List<String> lines = Files.readAllLines(in.toPath(), Charset.defaultCharset());
				return NBTParser.parse(lines);
			}
			else
			{
				DataInputStream input = FileCompressing.inputStream(in, compressed);
				NBTBase nbt = NBTBase.createFromData(input);
				input.close();
				return nbt;
			}
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
			return null;
		}
	}
	
	public static boolean serialize(NBTBase nbt, File out, boolean compressed)
	{
		try
		{
			if (!out.exists())
			{
				out.createNewFile();
			}
			
			if (useString())
			{
				BufferedWriter writer = new BufferedWriter(new FileWriter(out));
				writer.write(nbt.createString(""));
				writer.close();
				
				if (compressed)
				{
					FileCompressing.compressFile(out, new File(out.getAbsolutePath() + ".drc"));
					// out is just a temporary file used for compressing
					if (DELETE_COMPRESSED_FILES)
					{
						out.delete();
					}
				}
			}
			else
			{
				DataOutputStream output = FileCompressing.outputStream(out, compressed);
				nbt.write(output);
				output.close();
			}
			
			return true;
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
			return false;
		}
	}
}
