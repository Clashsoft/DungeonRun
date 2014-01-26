package com.clashsoft.dungeonrun.client.engine;

import java.util.HashMap;
import java.util.Map;

import com.clashsoft.dungeonrun.client.DungeonRunClient;
import com.clashsoft.dungeonrun.util.StringUtils;

public class I18n
{
	public static I18n					instance;
	
	public Map<String, StringTranslate>	languages	= new HashMap<String, StringTranslate>();
	
	public static String getString(String key)
	{
		return instance.translate(null, key);
	}
	
	public static String getStringFormatted(String key, Object... args)
	{
		return instance.translate(null, key, args);
	}
	
	public static String getStringFormatted(String key)
	{
		String key1 = key.replace('_', '.');
		
		int i1 = key.indexOf('[');
		int i2 = key.indexOf(']');
		
		if (i1 == -1 && i2 == -1)
			return getString(key);
		else if (i1 != -1 && i2 == -1)
			return getString(key.substring(0, i1));
		else if (i1 == -1 && i2 != -1)
			return getString(key.substring(0, i2));
		else
		{
			key1 = key1.substring(0, i1);
			String argsText = key.substring(i1 + 1, i2);
			
			String[] args = StringUtils.split(argsText, ',');
			Object[] parsedArgs = new Object[args.length];
			
			for (int i = 0; i < args.length; i++)
			{
				String arg = args[i];
				
				int i3 = arg.indexOf(':');
				String type = arg.substring(0, i3);
				String value = arg.substring(i3 + 1);
				parsedArgs[i] = StringUtils.parse(type, value);
			}
			
			return getStringFormatted(key1, parsedArgs);
		}
	}
	
	protected StringTranslate getLanguage(String lang)
	{
		if (lang == null)
			lang = DungeonRunClient.instance.gameSettings.language;
		
		StringTranslate st = languages.get(lang);
		if (st == null)
		{
			st = new StringTranslate(lang);
			languages.put(lang, st);
		}
		return st;
	}
	
	protected String translate(String lang, String key)
	{
		return getLanguage(lang).translate(key);
	}
	
	protected String translate(String lang, String key, Object[] args)
	{
		return getLanguage(lang).translate(key, args);
	}
}
