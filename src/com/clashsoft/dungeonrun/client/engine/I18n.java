package com.clashsoft.dungeonrun.client.engine;

import java.util.HashMap;
import java.util.Map;

import com.clashsoft.dungeonrun.DungeonRun;

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
		try
		{
			return instance.translate(null, key, args);
		}
		catch (Exception ex)
		{
			return key;
		}
	}
	
	protected StringTranslate getLanguage(String lang)
	{
		if (lang == null)
			lang = DungeonRun.instance.gameSettings.language;
		
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
