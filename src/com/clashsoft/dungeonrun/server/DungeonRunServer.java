package com.clashsoft.dungeonrun.server;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.imageout.ImageOut;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.client.engine.FontRenderer;
import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.client.engine.RenderEngine;
import com.clashsoft.dungeonrun.client.engine.SoundEngine;
import com.clashsoft.dungeonrun.client.gui.*;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.world.World;

public class DungeonRunServer extends BasicGame
{
	public static DungeonRunServer	instance;
	
	public static final String	VERSION		= "v0.4";
	
	public AppGameContainer		theGameContainer;
	public long					tick;
	
	public RenderEngine			renderEngine;
	public SoundEngine			soundEngine;
	public FontRenderer			fontRenderer;
	public I18n					i18n;
	
	public int					mousePosX	= 0;
	public int					mousePosY	= 0;
	
	public GameSettings			gameSettings;
	
	public boolean				hasGameStarted;
	public GuiIngame			theIngameGui;
	
	public boolean				isPaused;
	public GuiScreen			currentGui;
	public World				theWorld;
	public EntityPlayer			thePlayer;
	
	public DungeonRunServer() throws SlickException
	{
		super("Dungeon Run");
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		this.renderEngine.graphics = g;
		
		if (this.theIngameGui != null && hasGameStarted)
			this.theIngameGui.render(gc.getWidth(), gc.getHeight());
		
		if (this.currentGui != null)
			this.currentGui.render(gc.getWidth(), gc.getHeight());
	}
	
	@Override
	public void init(GameContainer arg0) throws SlickException
	{
		Mouse.setClipMouseCoordinatesToWindow(true);
		
		this.gameSettings = new GameSettings();
		
		this.renderEngine = new RenderEngine(this);
		this.soundEngine = new SoundEngine(this);
		this.fontRenderer = new FontRenderer(this);
		this.i18n = I18n.instance = new I18n();
		
		this.gameSettings.updateGame();
		
		this.theGameContainer.getInput().addListener(this);
		
		for (Block b : Block.blocksList)
		{
			if (b != null)
				b.registerIcons();
		}
		
		this.displayGuiScreen(new GuiIntro());
	}
	
	public void shutdown() throws SlickException
	{
		this.saveWorld(theWorld);
		this.gameSettings.save();
		this.theGameContainer.exit();
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
		if (this.currentGui != null)
			try
			{
				this.currentGui.keyTyped(key, c);
			}
			catch (SlickException ex)
			{
				ex.printStackTrace();
			}
	}
	
	@Override
	public void update(GameContainer gc, int tick) throws SlickException
	{
		this.tick++;
		if (this.currentGui != null)
		{
			this.currentGui.update(this);
		}
		
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_F2))
		{
			Image i = new Image(gc.getWidth(), gc.getHeight());
			gc.getGraphics().copyArea(i, 0, 0);
			try
			{
				File file = new File(getSaveDataFolder(), "screenshots");
				if (!file.exists())
					file.mkdir();
				String path = file.getPath() + "/" + getDateTime() + ".png";
				ImageOut.write(i, path, false);
				System.out.println("Screenshot saved as " + path);
			}
			catch (Exception ex)
			{
				System.out.println("Failed to save screenshot: " + ex.getMessage());
			}
		}
		if (input.isKeyPressed(Input.KEY_F3))
			this.gameSettings.debugMode = !this.gameSettings.debugMode;
	}
	
	public GuiScreen displayGuiScreen(GuiScreen gui) throws SlickException
	{
		this.currentGui = gui;
		this.currentGui.init(this);
		return this.currentGui;
	}
	
	public static Input getInput()
	{
		return instance.theGameContainer.getInput();
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
	
	public void setFullScreen(boolean flag) throws SlickException
	{
		theGameContainer.setFullscreen(flag);
	}
	
	public void setVSync(boolean flag)
	{
		theGameContainer.setVSync(flag);
	}
	
	public void startGame() throws SlickException
	{
		this.displayGuiScreen(new GuiInfo("world.loading"));
		
		this.hasGameStarted = true;
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					loadWorld(theWorld);
					
					if (DungeonRunServer.this.theWorld.getPlayers().isEmpty())
					{
						DungeonRunServer.this.thePlayer = new EntityPlayer(DungeonRunServer.this.theWorld);
						thePlayer.posY = 34F;
						DungeonRunServer.this.theWorld.spawnEntityInWorld(DungeonRunServer.this.thePlayer);
					}
					
					DungeonRunServer.this.theIngameGui = (GuiIngame) DungeonRunServer.this.displayGuiScreen(new GuiIngame(DungeonRunServer.this.thePlayer));
				}
				catch (SlickException ex)
				{
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	public void endGame() throws SlickException
	{
		this.displayGuiScreen(new GuiInfo("world.saving"));
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					DungeonRunServer.this.saveWorld(DungeonRunServer.this.theWorld);
					
					DungeonRunServer.this.displayGuiScreen(new GuiMainMenu());
					
					DungeonRunServer.this.theIngameGui = null;
					DungeonRunServer.this.hasGameStarted = false;
					DungeonRunServer.this.theWorld = null;
					DungeonRunServer.this.thePlayer = null;
				}
				catch (SlickException ex)
				{
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	public void pauseGame() throws SlickException
	{
		this.isPaused = true;
		this.displayGuiScreen(new GuiPauseMenu());
	}
	
	public void unpauseGame() throws SlickException
	{
		this.isPaused = false;
		this.displayGuiScreen(theIngameGui);
	}
	
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
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy)
	{
		mousePosX = newx;
		mousePosY = newy;
	}
}
