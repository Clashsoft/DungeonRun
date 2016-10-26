package com.clashsoft.dungeonrun.block;

public class Blocks
{
	public static Block air = new Block("air").setBackground();

	public static Block grass = new Block("grass");
	public static Block dirt  = new Block("dirt");
	public static Block sand  = new Block("sand");

	public static Block log    = new Block("wood").setBackground();
	public static Block leaves = new Block("leaves").setBackground();

	public static Block stone      = new Block("stone");
	public static Block stoneBrick = new Block("stone_brick");

	public static Block brick = new Block("brick");

	public static Block cobbleStone       = new Block("cobblestone");
	public static Block cobbleStoneWall   = new Block("cobblestone_wall").setBackground();
	public static Block cobbleStoneLadder = new BlockLadder("cobblestone_ladder").setBackground();

	public static Block planks      = new Block("planks");
	public static Block plankWall   = new Block("plank_wall").setBackground();
	public static Block plankLadder = new BlockLadder("plank_ladder").setBackground();

	public static Block torch = new Block("torch").setBackground();
	public static Block water = new BlockLadder("water").setBackground();

	public static void init()
	{
	}
}
