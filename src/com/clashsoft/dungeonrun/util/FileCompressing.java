package com.clashsoft.dungeonrun.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileCompressing
{
	public static File compressFile(File old, File newFile)
	{
		byte[] buffer = new byte[1024];
		
		try
		{
			if (!newFile.exists())
			{
				newFile.createNewFile();
			}
			
			GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(newFile));
			
			FileInputStream in = new FileInputStream(old);
			
			int len;
			while ((len = in.read(buffer)) > 0)
			{
				gzos.write(buffer, 0, len);
			}
			
			in.close();
			
			gzos.finish();
			gzos.close();
			
			//System.out.println("Successfully compressed " + old + " to " + newFile);
			return newFile;
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return old;
	}
	
	public static File decompressFile(File oldFile, File newFile)
	{
		byte[] buffer = new byte[1024];
		
		try
		{
			if (!newFile.exists())
			{
				newFile.createNewFile();
			}
			
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(oldFile));
			
			FileOutputStream out = new FileOutputStream(newFile);
			
			int len;
			while ((len = gzis.read(buffer)) > 0)
			{
				out.write(buffer, 0, len);
			}
			
			out.close();
			gzis.close();
			
			//System.out.println("Successfully decompressed " + oldFile + " to " + newFile);
			return newFile;
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return oldFile;
	}
}
