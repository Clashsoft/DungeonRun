package com.clashsoft.dungeonrun.entity;

import com.clashsoft.dungeonrun.world.World;
import com.clashsoft.nbt.tags.collection.NBTTagCompound;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class EntityList
{
	private static Map<String, Class>	nameToClassMap	= new HashMap<>();
	private static Map<Class, String>	classToNameMap	= new HashMap<>();
	
	static
	{
		registerMappings();
	}
	
	public static void registerMappings()
	{
		addMapping("Entity", Entity.class);
		addMapping("Damageable", EntityDamagable.class);
		addMapping("Living", EntityLiving.class);
		addMapping("Player", EntityPlayer.class);
		addMapping("Monster", EntityMonster.class);
	}
	
	public static void addMapping(String name, Class clazz)
	{
		nameToClassMap.put(name, clazz);
		classToNameMap.put(clazz, name);
	}
	
	public static Class getClassFromName(String name)
	{
		return nameToClassMap.get(name);
	}
	
	public static String getNameFromClass(Class clazz)
	{
		return classToNameMap.get(clazz);
	}
	
	public static Entity constructFromType(String entityType, World world)
	{
		Class clazz = nameToClassMap.get(entityType);
		
		try
		{
			Constructor c = clazz.getConstructor(World.class);
			Entity e = (Entity) c.newInstance(world);
			return e;
		}
		catch (Exception ex)
		{
			ex.printStackTrace(); // Impossible as long as there is a
									// Entity(World) constructor
		}
		return null;
	}
	
	public static Entity constructFromNBT(NBTTagCompound nbt, World world)
	{
		String entityType = nbt.getString("type");
		Entity entity = EntityList.constructFromType(entityType, world);
		return entity;
	}
}
