package com.clashsoft.dungeonrun.engine;

import java.io.IOException;
import java.util.Properties;

import org.newdawn.slick.util.ResourceLoader;

public class StringTranslate
{
	public String language;
	public Properties langMap = new Properties();
	
	public StringTranslate(String lang)
	{
		this.language = lang;
		loadLanguage(lang);
	}
	
	public String translate(String key)
	{
		String s = langMap.getProperty(key);
		return s == null ? key : s;
	}
	
	public String translate(String key, Object[] args)
	{
		return String.format(langMap.getProperty(key), args);
	}
	
	public void loadLanguage(String lang)
	{
		try
		{
			langMap.load(ResourceLoader.getResourceAsStream("/resources/lang/" + lang + ".lang"));
		}
		catch (IOException ex)
		{
			System.err.println("Unable to load language " + lang + ": " + ex.getMessage());
		}
	}
}
