package com.clashsoft.dungeonrun;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.block.Blocks;
import com.clashsoft.dungeonrun.item.Item;
import com.clashsoft.dungeonrun.item.Items;
import com.clashsoft.dungeonrun.server.IServer;
import com.clashsoft.dungeonrun.util.ResourceHelper;
import com.clashsoft.dungeonrun.world.ChunkSaver;
import com.clashsoft.dungeonrun.world.World;
import org.newdawn.slick.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Common DungeonRun game container.
 */
public abstract class DungeonRun extends BasicGame implements IServer
{
	public static final String VERSION = "Alpha 0.1-PRE";

	public static final File GAME_DIRECTORY  = new File(getAppdataDirectory(), "DungeonRun");
	public static final File SAVES_DIRECTORY = new File(GAME_DIRECTORY, "saves");

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

	protected static DungeonRun instance;

	protected AppGameContainer theGameContainer;
	protected ChunkSaver       chunkSaver;

	protected long tick;

	public DungeonRun()
	{
		super("DungeonRun " + VERSION);
	}

	public static DungeonRun getInstance()
	{
		return instance;
	}

	public static void exit()
	{
		AppGameContainer container = instance == null ? null : instance.theGameContainer;
		if (container != null)
		{
			container.destroy();
		}
		else
		{
			System.exit(0);
		}
	}

	/**
	 * Initialize the main slick library and app container
	 *
	 * @throws SlickException
	 */
	public abstract void initGame() throws SlickException;

	public abstract World getWorld();

	@Override
	public void init(GameContainer gc) throws SlickException
	{
		try
		{
			ResourceHelper.setupTextures();

			Items.init();
			Blocks.init();

			for (Block b : Block.blocks.values())
			{
				b.registerIcons();
			}

			for (Item i : Item.items.values())
			{
				i.registerIcons();
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

	@Override
	public void shutdown() throws SlickException
	{
		try
		{
			this.saveWorld(this.getWorld());
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
			World world = this.getWorld();
			if (world != null)
			{
				world.updateWorld();
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

	@Override
	public File getSaveDataFolder()
	{
		return GAME_DIRECTORY;
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
		return DATE_FORMAT.format(new Date());
	}

	public long getTick()
	{
		return this.tick;
	}

	@Override
	public void startGame() throws SlickException
	{
		final World world = this.getWorld();
		this.loadWorld(world);

		this.chunkSaver = new ChunkSaver();
		this.chunkSaver.start();
	}

	public void handleException(Throwable ex, String s) throws SlickException
	{
		ex.printStackTrace();
		exit();
	}

	public void handleError(Error error, String s) throws SlickException
	{
		if (error instanceof OutOfMemoryError)
		{
			this.stopGame();
			System.gc();
		}
		else
		{
			this.handleException(error, s);
		}
	}

	@Override
	public void stopGame() throws SlickException
	{
		this.saveWorld(this.getWorld());
		this.chunkSaver.disable();
		this.chunkSaver = null;
	}

	@Override
	public boolean saveWorld(World world) throws SlickException
	{
		return world != null && world.save(this.chunkSaver);
	}

	@Override
	public boolean loadWorld(World world) throws SlickException
	{
		return world != null && world.load();
	}
}
