package com.clashsoft.dungeonrun.item;

public class Items
{
	public static final Item wood_sword = new ItemTool("wood_sword", 3, ToolMaterial.WOOD).asDagger(true);
	public static final Item wood_hammer = new ItemTool("wood_hammer", 1, ToolMaterial.WOOD);

	public static final Item iron_sword = new ItemTool("iron_sword", 3, ToolMaterial.IRON);
	public static final Item iron_dagger = new ItemTool("iron_dagger", 2, ToolMaterial.IRON).asDagger(true);
	public static final Item iron_hammer = new ItemTool("iron_hammer", 1, ToolMaterial.IRON);

	public static void init()
	{
	}
}
