package com.clashsoft.dungeonrun.entity.render;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import com.clashsoft.dungeonrun.entity.Entity;

public abstract class RenderEntity<T extends Entity>
{
	public abstract Rectangle render(T entity, int width, int height) throws SlickException;
}
