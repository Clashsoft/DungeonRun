package com.clashsoft.dungeonrun.item;

@SuppressWarnings("unused")
public class Items
{
	public static final Item wood_sword  = new ItemTool("wood_sword", 3, ToolMaterial.WOOD).asDagger(true);
	public static final Item wood_hammer = new ItemTool("wood_hammer", 1, ToolMaterial.WOOD).withKnockback(0.7F);

	public static final Item iron_sword  = new ItemTool("iron_sword", 3, ToolMaterial.IRON);
	public static final Item iron_dagger = new ItemTool("iron_dagger", 2, ToolMaterial.IRON).asDagger(true);
	public static final Item iron_hammer = new ItemTool("iron_hammer", 1, ToolMaterial.IRON).withKnockback(0.7F);

	public static final Item copper_coin = new Item("copper_coin");
	public static final Item silver_coin = new Item("silver_coin");
	public static final Item gold_coin   = new Item("gold_coin");
	public static final Item green_gem   = new Item("green_gem");

	public static void init()
	{
	}
}
