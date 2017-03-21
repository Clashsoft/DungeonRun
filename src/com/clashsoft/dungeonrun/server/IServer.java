package com.clashsoft.dungeonrun.server;

import com.clashsoft.dungeonrun.world.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import java.io.File;

public interface IServer
{
	public File getSaveDataFolder();
	
	public boolean loadWorld(World world);
	
	public boolean saveWorld(World world);
	
	public void update(GameContainer gc, int tick);
	
	public void init(GameContainer gc) throws SlickException;
	
	public void shutdown();
	
	public void startWorld(World world);
	
	public void startGame();
	
	public void stopGame();
	
	public void pauseGame();
	
	public void resumeGame();
	
	public boolean isGameRunning();
}
