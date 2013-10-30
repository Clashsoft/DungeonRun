package com.clashsoft.dungeonrun.util;

import java.io.IOException;
import java.util.Properties;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;

public class StringTranslate
{
	public String language;
	public Properties langMap = new Properties();
	
	public StringTranslate(String lang) throws SlickException
	{
		this.language = lang;
		loadLanguage(lang);
	}
	
	public String translate(String key)
	{
		return langMap.getProperty(key);
	}
	
	public String translate(String key, Object[] args)
	{
		return String.format(langMap.getProperty(key), args);
	}
	
	public void loadLanguage(String lang) throws SlickException
	{
		try
		{
			langMap.load(ResourceLoader.getResourceAsStream("/resources/lang/" + lang + ".lang"));
		}
		catch (IOException ex)
		{
			throw new SlickException("Unable to load language " + lang + ": " + ex.getMessage(), ex);
		}
	}
}
