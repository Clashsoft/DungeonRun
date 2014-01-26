package com.clashsoft.dungeonrun.server;

import java.io.File;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import com.clashsoft.dungeonrun.world.World;

public interface IServer
{
	public File getSaveDataFolder();
	
	public boolean loadWorld(World world) throws SlickException;
	public boolean saveWorld(World world) throws SlickException;
	
	public void update(GameContainer gc, int tick) throws SlickException;
	public void init(GameContainer gc) throws SlickException;
	public void shutdown() throws SlickException;
 	
	public void startWorld(World world) throws SlickException;
	
	public void startGame() throws SlickException;
	public void stopGame() throws SlickException;
	
	public void pauseGame() throws SlickException;
	public void resumeGame() throws SlickException;
	
	public boolean isGameRunning() throws SlickException;
}
