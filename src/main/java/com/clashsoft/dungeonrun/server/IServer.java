package com.clashsoft.dungeonrun.server;

import com.clashsoft.dungeonrun.world.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import java.io.File;

public interface IServer
{
	File getSaveDataFolder();

	boolean loadWorld(World world);

	boolean saveWorld(World world);

	void update(GameContainer gc, int tick);

	void init(GameContainer gc) throws SlickException;

	void shutdown();

	void startWorld(World world);

	void startGame();

	void stopGame();

	void pauseGame();

	void resumeGame();

	boolean isGameRunning();
}
