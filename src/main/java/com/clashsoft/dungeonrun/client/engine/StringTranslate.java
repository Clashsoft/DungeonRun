package com.clashsoft.dungeonrun.client.engine;

import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.Properties;

public class StringTranslate
{
	public String     language;
	public Properties langMap = new Properties();

	public StringTranslate(String lang)
	{
		this.language = lang;
		this.loadLanguage(lang);
	}

	public String translate(String key)
	{
		String s = this.langMap.getProperty(key);
		return s == null ? key : s;
	}

	public String translate(String key, Object[] args)
	{
		return String.format(this.langMap.getProperty(key), args);
	}

	public void loadLanguage(String lang)
	{
		try
		{
			this.langMap.load(ResourceLoader.getResourceAsStream("/resources/lang/" + lang + ".lang"));
		}
		catch (IOException ex)
		{
			System.err.println("Unable to load language " + lang + ": " + ex.getMessage());
		}
	}
}
