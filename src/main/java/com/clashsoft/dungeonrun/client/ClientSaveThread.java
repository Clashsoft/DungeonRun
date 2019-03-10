package com.clashsoft.dungeonrun.client;

public class ClientSaveThread extends Thread
{
	private final DungeonRunClient dungeonRun;

	ClientSaveThread(DungeonRunClient dungeonRunClient)
	{
		this.dungeonRun = dungeonRunClient;
	}

	@Override
	public void run()
	{
		this.dungeonRun.theIngameGui.setWorldSaving(true);
		this.dungeonRun.saveWorld(this.dungeonRun.theWorld);
		this.dungeonRun.theIngameGui.setWorldSaving(false);
	}
}
