package com.clashsoft.dungeonrun.util;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.SlickException;

public class I18n
{
	public static I18n instance;
	
	public String currentLanguage;
	public Map<String, StringTranslate> languages = new HashMap<String, StringTranslate>();
	
	public static String getString(String key)
	{
		try
		{
			return instance.translate(instance.currentLanguage, key);
		}
		catch (Exception ex)
		{
			return key;
		}
	}
	
	public static String getStringFormatted(String key, Object... args)
	{
		try
		{
			return instance.translate(instance.currentLanguage, key, args);
		}
		catch (Exception ex)
		{
			return key;
		}
	}
	
	protected StringTranslate getLanguage(String lang) throws SlickException
	{
		StringTranslate st = languages.get(lang == null ? currentLanguage : lang);
		if (st == null)
		{
			st = new StringTranslate(lang);
			languages.put(lang, st);
		}
		return st;
	}
	
	protected String translate(String lang, String key) throws SlickException
	{
		return getLanguage(lang).translate(key);
	}
	
	protected String translate(String lang, String key, Object[] args) throws SlickException
	{
		return getLanguage(lang).translate(key, args);
	}
}
