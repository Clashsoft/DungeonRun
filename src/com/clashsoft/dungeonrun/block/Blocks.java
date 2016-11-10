package com.clashsoft.dungeonrun.block;

public class Blocks
{
	public static BlockBackground air = new BlockBackground("air");

	public static Block grass = new Block("grass");
	public static Block dirt  = new Block("dirt");
	public static Block sand  = new Block("sand");

	public static BlockBackground log    = new BlockBackground("wood");
	public static BlockBackground leaves = new BlockBackground("leaves");

	public static Block stone      = new Block("stone");
	public static Block stoneBrick = new Block("stone_brick");

	public static Block brick = new Block("brick");

	public static BlockSolid      cobbleStone     = new BlockSolid("cobblestone");
	public static BlockBackground cobbleStoneWall = new BlockBackground("cobblestone_wall");

	public static BlockSolid      planks    = new BlockSolid("planks");
	public static BlockBackground plankWall = new BlockBackground("plank_wall");

	public static BlockLadder ladder = new BlockLadder("ladder");

	public static BlockBackground torch = new BlockBackground("torch");
	public static BlockLadder     water = new BlockLadder("water");

	public static BlockChest woodChest  = new BlockChest("wood_chest");
	public static BlockChest stoneChest = new BlockChest("stone_chest");

	static
	{
		cobbleStone.setWallBlock(cobbleStoneWall);
		planks.setWallBlock(plankWall);
	}

	public static void init()
	{

	}
}
