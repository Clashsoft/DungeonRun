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
	
	public static final String		VERSION	= "Alpha 0.1-PRE";
	
	public AppGameContainer			theGameContainer;
	public long						tick;
	
	public boolean					hasGameStarted;
	public World					theWorld;
	
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
		try
		{
			for (Block b : Block.blocksList)
			{
				if (b != null)
				{
					b.registerIcons();
				}
			}
		}
		catch (Exception ex)
		{
			this.handleException(ex, "Initializing Server");
		}
		catch (Error error)
		{
			this.handleError(error, "Initializing Server");
		}
	}
	
	public void shutdown() throws SlickException
	{
		try
		{
			this.saveWorld(this.theWorld);
			this.theGameContainer.exit();
		}
		catch (Exception ex)
		{
			this.handleException(ex, "Server Shutdown");
		}
		catch (Error error)
		{
			this.handleError(error, "Server Shutdown");
		}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		try
		{
			g.setColor(Color.white);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			
			g.setColor(Color.black);
			g.drawString("DungeonRun Server " + VERSION, 5, 5);
		}
		catch (Exception ex)
		{
			this.handleException(ex, "Server Screen Rendering");
		}
		catch (Error error)
		{
			this.handleError(error, "Server Screen Rendering");
		}
	}
	
	@Override
	public void update(GameContainer gc, int tick) throws SlickException
	{
		try
		{
			this.tick++;
			
			if (this.theWorld != null)
			{
				this.theWorld.updateWorld();
			}
			
		}
		catch (Exception ex)
		{
			this.handleException(ex, "World Update");
		}
		catch (Error error)
		{
			this.handleError(error, "World Update");
		}
	}
	
	public static File getSaveDataFolder()
	{
		File f = new File(getAppdataDirectory(), "dungeonrun");
		if (!f.exists())
		{
			f.mkdirs();
		}
		return f;
	}
	
	public static String getAppdataDirectory()
	{
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN"))
		{
			return System.getenv("APPDATA");
		}
		else if (OS.contains("MAC"))
		{
			return System.getProperty("user.home") + "/Library/Application Support";
		}
		else if (OS.contains("NUX"))
		{
			return System.getProperty("user.home");
		}
		return System.getProperty("user.dir");
	}
	
	public static String getDateTime()
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}
	
	public void startWorld() throws SlickException
	{
		this.hasGameStarted = true;
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					DungeonRunServer.this.loadWorld(DungeonRunServer.this.theWorld);
					
					DungeonRunServer.this.onStartGame();
				}
				catch (SlickException ex)
				{
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	public void handleException(Exception ex, String s) throws SlickException
	{
		ex.printStackTrace();
		System.exit(-1);
	}
	
	public void handleError(Error error, String s) throws SlickException
	{
		if (error instanceof OutOfMemoryError)
		{
			DungeonRunServer.this.saveWorld(DungeonRunServer.this.theWorld);
			this.onGameEnd();
			System.gc();
		}
		else
		{
			error.printStackTrace();
			System.exit(-2);
		}
	}
	
	public void onStartGame() throws SlickException
	{
		
	}
	
	public void stopWorld() throws SlickException
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					DungeonRunServer.this.saveWorld(DungeonRunServer.this.theWorld);
					DungeonRunServer.this.onGameEnd();
				}
				catch (SlickException ex)
				{
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	public void onGameEnd() throws SlickException
	{
	}
	
	public boolean saveWorld(World world) throws SlickException
	{
		if (world == null)
		{
			return false;
		}
		
		String worldFileName = world.worldInfo.getFileName();
		File saves = new File(getSaveDataFolder(), "saves");
		if (!saves.exists())
		{
			saves.mkdirs();
		}
		
		File worldDir = new File(saves, worldFileName);
		if (!worldDir.exists())
		{
			worldDir.mkdirs();
		}
		return world.save(worldDir);
	}
	
	public boolean loadWorld(World world) throws SlickException
	{
		if (world == null)
		{
			return false;
		}
		
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
