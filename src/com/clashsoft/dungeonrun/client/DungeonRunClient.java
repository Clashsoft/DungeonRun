package com.clashsoft.dungeonrun.client;

import com.clashsoft.dungeonrun.DungeonRun;
import com.clashsoft.dungeonrun.client.engine.FontRenderer;
import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.client.engine.RenderEngine;
import com.clashsoft.dungeonrun.client.engine.SoundEngine;
import com.clashsoft.dungeonrun.client.gui.*;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.world.World;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.imageout.ImageOut;

import java.io.File;

public class DungeonRunClient extends DungeonRun
{
	public static DungeonRunClient instance;

	public String username;

	public RenderEngine renderEngine;
	public SoundEngine  soundEngine;
	public FontRenderer fontRenderer;
	public FontRenderer smallFontRenderer;
	public I18n         i18n;

	public int mousePosX = 0;
	public int mousePosY = 0;

	public GameSettings gameSettings;

	public    GuiScreen    currentGui;
	public    GuiIngame    theIngameGui;
	public    EntityPlayer thePlayer;
	protected World        theWorld;
	protected boolean      isGameRunning;

	public boolean isPaused;

	protected int screenWidth;
	protected int screenHeight;

	public DungeonRunClient(String username) throws SlickException
	{
		super();
		this.username = username;
	}

	public static void main(String[] args)
	{
		String username = "Clashsoft";
		try
		{
			instance = new DungeonRunClient(username);
			instance.initGame();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			DungeonRun.exit();
		}
	}

	@Override
	public void initGame() throws SlickException
	{
		this.theGameContainer = new AppGameContainer(this);
		this.screenWidth = this.theGameContainer.getScreenWidth();
		this.screenHeight = this.theGameContainer.getScreenHeight();
		this.setFullScreen(false);
		this.theGameContainer.setMinimumLogicUpdateInterval(50);
		this.theGameContainer.setMaximumLogicUpdateInterval(50);
		this.theGameContainer.setShowFPS(false);
		this.theGameContainer.start();
	}

	@Override
	public void init(GameContainer arg0) throws SlickException
	{
		try
		{
			Mouse.setClipMouseCoordinatesToWindow(true);
			this.getInput().addListener(this);

			this.gameSettings = new GameSettings(this);

			this.renderEngine = new RenderEngine(this);
			this.soundEngine = new SoundEngine(this);
			this.fontRenderer = new FontRenderer(this, "default");
			this.smallFontRenderer = new FontRenderer(this, "small");
			this.i18n = new I18n();

			this.gameSettings.updateGame();

			this.displayGuiScreen(new GuiIntro());

			super.init(arg0);
		}
		catch (Exception ex)
		{
			this.handleException(ex, "Initializing");
		}
		catch (Error error)
		{
			this.handleError(error, "Initializing");
		}
	}

	@Override
	public void shutdown() throws SlickException
	{
		try
		{
			this.gameSettings.save();
			super.shutdown();
		}
		catch (Exception ex)
		{
			this.handleException(ex, "World Shutdown");
		}
		catch (Error error)
		{
			this.handleError(error, "World Shutdown");
		}
	}

	@Override
	public void update(GameContainer gc, int tick) throws SlickException
	{
		try
		{
			if (this.currentGui != null)
			{
				this.currentGui.updateScreen();
			}

			super.update(gc, tick);

			if (this.theWorld != null && this.tick % 200 == 0)
			{
				new ClientSaveThread(this).start();
			}

			Input input = gc.getInput();
			if (input.isKeyPressed(Input.KEY_F2))
			{
				Image i = new Image(gc.getWidth(), gc.getHeight());
				gc.getGraphics().copyArea(i, 0, 0);
				try
				{
					File file = new File(this.getSaveDataFolder(), "screenshots");
					if (!file.exists())
					{
						file.mkdir();
					}
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
			{
				this.gameSettings.debugMode = !this.gameSettings.debugMode;
			}
		}
		catch (Exception ex)
		{
			this.handleException(ex, "Client Update");
		}
		catch (Error error)
		{
			this.handleError(error, "Client Update");
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		try
		{
			this.renderEngine.graphics = g;

			if (this.theIngameGui != null && this.isGameRunning())
			{
				this.theIngameGui.render(g, gc.getWidth(), gc.getHeight());
			}

			if (this.currentGui != null)
			{
				this.currentGui.render(g, gc.getWidth(), gc.getHeight());
			}
		}
		catch (Exception ex)
		{
			this.handleException(ex, "Rendering Screen");
		}
		catch (Error error)
		{
			this.handleError(error, "Rendering Screen");
		}
	}

	public Input getInput()
	{
		return this.theGameContainer.getInput();
	}

	public int getFPS()
	{
		return this.theGameContainer.getFPS();
	}

	public Graphics getGraphics()
	{
		return this.theGameContainer.getGraphics();
	}

	@Override
	public void startGame() throws SlickException
	{
		this.displayGuiScreen(new GuiInfo("world.loading"));

		this.soundEngine.stopAllMusics();
		super.startGame();

		this.thePlayer = this.theWorld.getPlayer(this.username);

		if (this.thePlayer == null)
		{
			this.thePlayer = new EntityPlayer(this.theWorld, this.username);
			this.thePlayer.setLocation(0.5D, this.theWorld.getHeight(0));
			this.theWorld.spawnEntity(this.thePlayer);
		}

		this.isGameRunning = true;
		this.theIngameGui = new GuiIngame(this.thePlayer);
		this.displayGuiScreen(this.theIngameGui);
	}

	@Override
	public void stopGame() throws SlickException
	{
		this.displayGuiScreen(new GuiInfo("world.saving"));

		super.stopGame();

		this.theIngameGui = null;
		this.isGameRunning = false;
		this.theWorld = null;
		this.thePlayer = null;

		this.displayGuiScreen(new GuiMainMenu());
	}

	@Override
	public void pauseGame() throws SlickException
	{
		this.isPaused = true;
		new ClientSaveThread(this).start();
		this.displayGuiScreen(new GuiPauseMenu());
	}

	@Override
	public void resumeGame() throws SlickException
	{
		this.isPaused = false;
		this.displayGuiScreen(this.theIngameGui);
	}

	@Override
	public World getWorld()
	{
		return this.theWorld;
	}

	public GuiScreen displayGuiScreen(GuiScreen gui) throws SlickException
	{
		this.currentGui = gui;
		this.currentGui.init(this, this.theGameContainer.getWidth(), this.theGameContainer.getHeight());
		return gui;
	}

	public void setFullScreen(boolean flag) throws SlickException
	{
		if (flag)
		{
			this.theGameContainer.setDisplayMode(this.screenWidth, this.screenHeight, true);
		}
		else
		{
			this.theGameContainer.setDisplayMode(800, 480, false);
		}
	}

	public void setVSync(boolean flag)
	{
		this.theGameContainer.setVSync(flag);
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
		return this.isGameRunning;
	}

	@Override
	public void keyPressed(int key, char c)
	{
		if (this.currentGui != null)
		{
			try
			{
				this.currentGui.keyTyped(key, c);
			}
			catch (SlickException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy)
	{
		this.mousePosX = newx;
		this.mousePosY = newy;
	}
}
