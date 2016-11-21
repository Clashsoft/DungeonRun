package com.clashsoft.dungeonrun.item;

import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Map;
import java.util.TreeMap;

public class Item
{
	public static final Map<String, Item> items = new TreeMap<>();

	public static final int ROTATE = 0;
	public static final int STILL = 1;

	public final String name;

	private Image icon;

	public Item(String id)
	{
		this.name = id;
		items.put(id, this);
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

	public float getDamage(ItemStack stack)
	{
		return 0F;
	}

	public float getKnockback(ItemStack stack)
	{
		return 0F;
	}

	public int getMaxUses(ItemStack stack)
	{
		return 0;
	}

	public int getMaxStackSize(ItemStack stack)
	{
		return 64;
	}

	public int getSwingType(ItemStack stack)
	{
		return ROTATE;
	}
}
