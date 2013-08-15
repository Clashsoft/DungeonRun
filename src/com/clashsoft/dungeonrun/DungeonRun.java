package com.clashsoft.dungeonrun;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;

import com.clashsoft.dungeonrun.block.Block;
import com.clashsoft.dungeonrun.engine.FontRenderer;
import com.clashsoft.dungeonrun.engine.RenderEngine;
import com.clashsoft.dungeonrun.engine.SoundEngine;
import com.clashsoft.dungeonrun.entity.EntityPlayer;
import com.clashsoft.dungeonrun.gui.GameSettings;
import com.clashsoft.dungeonrun.gui.GuiIngame;
import com.clashsoft.dungeonrun.gui.GuiIntro;
import com.clashsoft.dungeonrun.gui.GuiPauseMenu;
import com.clashsoft.dungeonrun.gui.GuiScreen;
import com.clashsoft.dungeonrun.world.World;

public class DungeonRun extends BasicGame
{	
	public static DungeonRun instance;
	
	public AppGameContainer theGameContainer;
	public int tick;
	
	public RenderEngine renderEngine;
	public SoundEngine soundEngine;
	public FontRenderer fontRenderer;
	
	public boolean debugMode = true;
	
	public int mousePosX = 0;
	public int mousePosY = 0;
	
	public GameSettings gameSettings;
	
	public boolean hasGameStarted;
	public GuiIngame theIngameGui;
	
	public boolean isPaused;
	private GuiScreen currentGui;
	public World theWorld;
	public EntityPlayer thePlayer;
	
	public DungeonRun()
	{
		super("Dungeon Run");
		this.renderEngine = new RenderEngine(this);
		this.soundEngine = new SoundEngine(this);
		this.fontRenderer = new FontRenderer(this);
		this.gameSettings = new GameSettings();
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		if (this.hasGameStarted)
			this.theIngameGui.render(gc.getWidth(), gc.getHeight());
		if (this.currentGui != null)
			this.currentGui.render(gc.getWidth(), gc.getHeight());
		if (!debugMode)
			g.drawString(gc.getFPS() + " FPS", 10, 10);
		else
		{
			getGraphics().setColor(Color.white);
			g.drawString("Dungeon Run v0.2 (" + gc.getFPS() + " FPS)", 10, 10);
			if (thePlayer != null)
			{
				g.drawString(String.format("PlayerPos: (%.2f;%.2f;%.2f)", thePlayer.posX, thePlayer.posY, thePlayer.posZ), 10, 30);
				g.drawString("PlayerRot: " + thePlayer.rot, 10, 50);
				g.drawString(String.format("PlayerVelocity: (%.2f;%.2f;%.2f)",thePlayer.velocityX, thePlayer.velocityY, thePlayer.velocityZ), 10, 70);
				g.drawString("PlayerWorld: " + thePlayer.worldObj.name, 10, 90);
			}
			
			if (theIngameGui != null)
			{
				g.drawString(String.format("HoverPos: (%d;%d;%d)", theIngameGui.mouseBlockX, theIngameGui.mouseBlockY, theIngameGui.mouseBlockZ), 10, 110);
			}
		}
	}

	@Override
	public void init(GameContainer arg0) throws SlickException
	{
		Mouse.setClipMouseCoordinatesToWindow(true);
		this.displayGuiScreen(new GuiIntro());
		for (Block b : Block.blocksList)
		{
			if (b != null)
				b.registerIcons();
		}
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException
	{
		tick++;
		if (this.currentGui != null)
			this.currentGui.update(this);
		Input input = arg0.getInput();
		if (input.isKeyPressed(Input.KEY_F2))
		{
			Image i = new Image(arg0.getWidth(), arg0.getHeight());
			arg0.getGraphics().copyArea(i, 0, 0);
			try
			{
				File file = new File(getSaveDataFolder(), "screenshots");
				if (!file.exists())
					file.mkdir();
				String path = file.getPath() + "/" + getDateTime() + ".png";
				ImageOut.write(i, path, false);
				System.out.println("Screenshot saved as 1" + path);
			}
			catch (Exception ex)
			{
				System.out.println("Failed to save screenshot: " + ex.getMessage());
			}
		}
		if (input.isKeyPressed(Input.KEY_F3))
			this.debugMode = !this.debugMode;
	}
	
	public GuiScreen displayGuiScreen(GuiScreen gui) throws SlickException
	{
		this.currentGui = gui;
		this.currentGui.init(this);
		return this.currentGui;
	}
	
	public static Graphics getGraphics()
	{
		return instance.theGameContainer.getGraphics();
	}
	
	public static Input getInput()
	{
		return instance.theGameContainer.getInput();
	}

	public static void main(String[] args) throws SlickException
	{
		instance = new DungeonRun();
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
		f.mkdir();
		return f;
	}
	
	public static String getAppdataDirectory()
	{
		String OS = System.getProperty("os.name").toUpperCase();
	    if (OS.contains("WIN"))
	        return System.getenv("APPDATA");
	    else if (OS.contains("MAC"))
	        return System.getProperty("user.home") + "/Library/Application "
	                + "Support";
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
	
	public void setResolution(int width, int heigth) throws SlickException
	{
		theGameContainer.setDisplayMode(width, heigth, theGameContainer.isFullscreen());
	}
	
	public void setVSync(boolean flag)
	{
		theGameContainer.setVSync(flag);
	}

	public void startGame() throws SlickException //TODO Proper loading
	{
		this.theWorld = new World("TestWorld");
		this.thePlayer = new EntityPlayer(this.theWorld);
		thePlayer.posY = 34F;
		this.theWorld.spawnEntityInWorld(this.thePlayer);
		this.theIngameGui = (GuiIngame) this.displayGuiScreen(new GuiIngame(this.thePlayer));
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

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy)
	{
		super.mouseMoved(oldx, oldy, newx, newy);
		mousePosX = newx;
		mousePosY = newy;
	}
}
