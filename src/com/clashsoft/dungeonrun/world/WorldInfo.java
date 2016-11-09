package com.clashsoft.dungeonrun.world;

import com.clashsoft.nbt.tags.collection.NBTTagCompound;
import com.clashsoft.nbt.util.INBTSaveable;

import java.util.Date;

public class WorldInfo implements INBTSaveable
{
	private String	name;
	private String	fileName;
	private long	creationTime;
	
	public WorldInfo(String name)
	{
		this.name = this.fileName = name;
		this.creationTime = new Date().getTime();
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getCreationTime()
	{
		return this.creationTime;
	}

	public void setCreationTime(long time)
	{
		this.creationTime = time;
	}

	public String getFileName()
	{
		return this.fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Name", this.name);
		nbt.setLong("CreationTime", this.creationTime);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.name = nbt.getString("Name");
		this.creationTime = nbt.getLong("CreationTime");
	}
}
