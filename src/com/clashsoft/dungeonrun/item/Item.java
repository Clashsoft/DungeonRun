package com.clashsoft.dungeonrun.item;

import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Map;
import java.util.TreeMap;

public class Item
{
	public static Map<String, Item> items = new TreeMap<>();

	public final String name;

	private int maxUses;
	private Image icon;

	public Item(String id)
	{
		this.name = id;
		items.put(id, this);
	}

	public Item setMaxUses(int maxUses)
	{
		this.maxUses = maxUses;
		return this;
	}

	public String getLocalizedName(ItemStack stack)
	{
		return I18n.getString("item." + this.name + ".name");
	}

	public Image getIcon(ItemStack stack)
	{
		return this.icon;
	}

	public void registerIcons() throws SlickException
	{
		this.icon = ResourceHelper.loadTexture("resources/textures/items/" + this.name + ".png");
	}

	public float getDamageVsEntity(ItemStack stack)
	{
		return 0F;
	}

	public int getMaxUses(ItemStack stack)
	{
		return this.maxUses;
	}
}
