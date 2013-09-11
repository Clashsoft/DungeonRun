package com.clashsoft.dungeonrun.world;

import java.util.Date;

import com.clashsoft.dungeonrun.nbt.INBTSaveable;
import com.clashsoft.dungeonrun.nbt.NBTTagCompound;

public class WorldInfo implements INBTSaveable
{
	private String name;
	private String fileName;
	private long creationTime;
	
	public WorldInfo(String name)
	{
		this.name = this.fileName = name;
		this.creationTime = new Date().getTime();
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setCreationTime(long time)
	{
		this.creationTime = time;
	}
	
	public void setCreationDate(Date date)
	{
		this.creationTime = date.getTime();
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public long getCreationTime()
	{
		return this.creationTime;
	}

	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Name", this.name);
		nbt.setString("FileName", this.fileName);
		nbt.setLong("CreationTime", this.creationTime);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.name = nbt.getString("Name");
		this.fileName = nbt.getString("FileName");
		this.creationTime = nbt.getLong("CreationTime");
	}
}
