package com.clashsoft.dungeonrun;

import java.io.File;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.imageout.ImageOut;

import com.clashsoft.dungeonrun.client.engine.FontRenderer;
import com.clashsoft.dungeonrun.client.engine.I18n;
import com.clashsoft.dungeonrun.client.engine.RenderEngine;
import com.clashsoft.dungeonrun.client.engine.SoundEngine;
import com.clashsoft.dungeonrun.client.gui.*;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.server.DungeonRunServer;

public class DungeonRun extends DungeonRunServer
{
	public static DungeonRun	instance;
	
	public String				username;
	
	public RenderEngine			renderEngine;
	public SoundEngine			soundEngine;
	public FontRenderer			fontRenderer;
	public I18n					i18n;
	
	public int					mousePosX	= 0;
	public int					mousePosY	= 0;
	
	public GameSettings			gameSettings;
	
	public GuiScreen			currentGui;
	public GuiIngame			theIngameGui;
	public EntityPlayer			thePlayer;
	
	public boolean				isPaused;
	
	public DungeonRun(String username) throws SlickException
	{
		super("Dungeon Run");
		this.username = username;
	}
	
	public static void main(String[] args) throws SlickException
	{
		String username = args[0];
		
		instance = new DungeonRun(username);
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
			Mouse.setClipMouseCoordinatesToWindow(true);
			this.theGameContainer.getInput().addListener(this);
			
			this.gameSettings = new GameSettings();
			
			this.renderEngine = new RenderEngine(this);
			this.soundEngine = new SoundEngine(this);
			this.fontRenderer = new FontRenderer(this, "font1");
			this.i18n = I18n.instance = new I18n();
			
			this.gameSettings.updateGame();
			
			this.displayGuiScreen(new GuiIntro());
			
			super.init(arg0);
		}
		catch (Exception ex)
		{
			this.handleException(ex, "Initializing Game");
		}
		catch (Error error)
		{
			this.handleError(error, "Initializing Game");
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
			this.handleException(ex, "Client Shutdown");
		}
		catch (Error error)
		{
			this.handleError(error, "Server Shutdown");
		}
	}
	
	@Override
	public void update(GameContainer gc, int tick) throws SlickException
	{
		try
		{
			if (this.currentGui != null)
			{
				this.currentGui.update(this);
			}
			
			super.update(gc, tick);
			
			Input input = gc.getInput();
			if (input.isKeyPressed(Input.KEY_F2))
			{
				Image i = new Image(gc.getWidth(), gc.getHeight());
				gc.getGraphics().copyArea(i, 0, 0);
				try
				{
					File file = new File(getSaveDataFolder(), "screenshots");
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
			
			if (this.theIngameGui != null && this.hasGameStarted)
			{
				this.theIngameGui.render(gc.getWidth(), gc.getHeight());
			}
			
			if (this.currentGui != null)
			{
				this.currentGui.render(gc.getWidth(), gc.getHeight());
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
	
	@Override
	public void handleException(Exception ex, String s) throws SlickException
	{
		super.handleException(ex, s);
	}
	
	@Override
	public void startWorld() throws SlickException
	{
		this.displayGuiScreen(new GuiInfo("world.loading"));
		
		super.startWorld();
	}
	
	@Override
	public void onStartGame() throws SlickException
	{
		this.thePlayer = this.theWorld.getPlayer(this.username);
		
		this.theIngameGui = new GuiIngame(this.thePlayer);
		this.displayGuiScreen(this.theIngameGui);
	}
	
	@Override
	public void stopWorld() throws SlickException
	{
		this.displayGuiScreen(new GuiInfo("world.saving"));
		
		super.stopWorld();
	}
	
	@Override
	public void onGameEnd() throws SlickException
	{
		this.displayGuiScreen(new GuiMainMenu());
		
		this.theIngameGui = null;
		this.hasGameStarted = false;
		this.theWorld = null;
		this.thePlayer = null;
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
	
	public GuiScreen displayGuiScreen(GuiScreen gui) throws SlickException
	{
		this.currentGui = gui;
		this.currentGui.init(this);
		return gui;
	}
	
	public static Input getInput()
	{
		return instance.theGameContainer.getInput();
	}
	
	public void setFullScreen(boolean flag) throws SlickException
	{
		this.theGameContainer.setFullscreen(flag);
	}
	
	public void setVSync(boolean flag)
	{
		this.theGameContainer.setVSync(flag);
	}
	
	public void pauseGame() throws SlickException
	{
		this.isPaused = true;
		this.displayGuiScreen(new GuiPauseMenu());
	}
	
	public void unpauseGame() throws SlickException
	{
		this.isPaused = false;
		this.displayGuiScreen(this.theIngameGui);
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy)
	{
		this.mousePosX = newx;
		this.mousePosY = newy;
	}
}
