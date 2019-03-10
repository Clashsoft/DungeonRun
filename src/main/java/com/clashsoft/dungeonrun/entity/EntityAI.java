package com.clashsoft.dungeonrun.entity;

import java.util.Random;

public interface EntityAI<T extends Entity>
{
	void update(T entity, Random random);
}
