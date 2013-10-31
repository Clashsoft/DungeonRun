package com.clashsoft.dungeonrun.server;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.newdawn.slick.*;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.world.World;

public class DungeonRunServer extends BasicGame
{
	public static DungeonRunServer	instance;
	
	public static final String	VERSION		= "v0.4";
	
	public AppGameContainer		theGameContainer;
	public long					tick;
	
	public boolean				hasGameStarted;
	public World				theWorld;
	
	public DungeonRunServer() throws SlickException
	{
		this("Dungeon Run Server");
		
	}
	
	public DungeonRunServer(String title)
	{
		super(title);
		instance = this;
	}
	
	public static void main(String[] args) throws SlickException
	{
		instance = new DungeonRunServer();
		instance.theGameContainer = new AppGameContainer(instance);
		instance.theGameContainer.setDisplayMode(800, 450, false);
		instance.theGameContainer.setMinimumLogicUpdateInterval(50);
		instance.theGameContainer.setMaximumLogicUpdateInterval(50);
		instance.theGameContainer.setShowFPS(false);
		instance.theGameContainer.setResizable(true);
		instance.theGameContainer.start();
	}
	
	@Override
	public void init(GameContainer arg0) throws SlickException
	{	
		for (Block b : Block.blocksList)
		{
			if (b != null)
				b.registerIcons();
		}
	}
	
	public void shutdown() throws SlickException
	{
		this.saveWorld(theWorld);
		this.theGameContainer.exit();
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		
		g.setColor(Color.black);
		g.drawString("DungeonRun Server " + VERSION, 5, 5);
	}
	
	@Override
	public void update(GameContainer gc, int tick) throws SlickException
	{
		this.tick++;
		
		if (this.theWorld != null)
			this.theWorld.updateWorld();
	}
	
	public static File getSaveDataFolder()
	{
		File f = new File(getAppdataDirectory(), "dungeonrun");
		if (!f.exists())
			f.mkdirs();
		return f;
	}
	
	public static String getAppdataDirectory()
	{
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN"))
			return System.getenv("APPDATA");
		else if (OS.contains("MAC"))
			return System.getProperty("user.home") + "/Library/Application Support";
		else if (OS.contains("NUX"))
			return System.getProperty("user.home");
		return System.getProperty("user.dir");
	}
	
	public static String getDateTime()
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}
	
	public void startGame() throws SlickException
	{	
		this.hasGameStarted = true;
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					loadWorld(theWorld);
					
					onStartGame();
				}
				catch (SlickException ex)
				{
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	public void onStartGame() throws SlickException
	{
		
	}
	
	public void endGame() throws SlickException
	{	
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					DungeonRunServer.this.saveWorld(DungeonRunServer.this.theWorld);
					onGameEnd();
				}
				catch (SlickException ex)
				{
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	public void onGameEnd() throws SlickException
	{}
	
	public boolean saveWorld(World world) throws SlickException
	{	
		if (world == null)
			return false;
		
		String worldFileName = world.worldInfo.getFileName();
		File saves = new File(getSaveDataFolder(), "saves");
		if (!saves.exists())
			saves.mkdirs();
		
		File worldDir = new File(saves, worldFileName);
		if (!worldDir.exists())
			worldDir.mkdirs();
		return world.save(worldDir);
	}
	
	public boolean loadWorld(World world) throws SlickException
	{	
		if (world == null)
			return false;
		
		String worldFileName = world.worldInfo.getFileName();
		File saves = new File(getSaveDataFolder(), "saves");
		if (!saves.exists())
		{
			saves.mkdirs();
			return false;
		}
		
		File worldFile = new File(saves, worldFileName);
		return world.load(worldFile);
	}
}
