package com.clashsoft.dungeonrun.server;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.world.World;

public class DungeonRunServer extends DungeonRun implements IServer
{
	public World	theWorld;
	
	public DungeonRunServer() throws SlickException
	{
		super();
	}
	
	@Override
	public void initGame() throws SlickException
	{
		this.theGameContainer = new AppGameContainer(this);
		this.theGameContainer.setDisplayMode(800, 450, false);
		this.theGameContainer.setMinimumLogicUpdateInterval(50);
		this.theGameContainer.setMaximumLogicUpdateInterval(50);
		this.theGameContainer.setShowFPS(false);
		this.theGameContainer.setResizable(true);
		this.theGameContainer.start();
	}
	
	public static void main(String[] args)
	{
		try
		{
			instance = new DungeonRunServer();
			instance.initGame();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			DungeonRun.exit();
		}
	}
	
	@Override
	public void startWorld(World world) throws SlickException
	{
		this.theWorld = world;
		this.startGame();
	}
	
	@Override
	public boolean isGameRunning() throws SlickException
	{
		return true;
	}
	
	@Override
	public World getWorld()
	{
		return this.theWorld;
	}
	
	@Override
	public void pauseGame() throws SlickException
	{
	}
	
	@Override
	public void resumeGame() throws SlickException
	{
	}
}
