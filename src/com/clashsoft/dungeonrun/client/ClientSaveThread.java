package com.clashsoft.dungeonrun.client;

import org.newdawn.slick.SlickException;

public class ClientSaveThread extends Thread
{
	private final DungeonRunClient	dungeonRun;

	ClientSaveThread(DungeonRunClient dungeonRunClient)
	{
		this.dungeonRun = dungeonRunClient;
	}

	@Override
	public void run()
	{
		try
		{
			this.dungeonRun.theIngameGui.setWorldSaving(true);
			this.dungeonRun.saveWorld(this.dungeonRun.theWorld);
			this.dungeonRun.theIngameGui.setWorldSaving(false);
		}
		catch (SlickException ex)
		{
			ex.printStackTrace();
		}
	}
}