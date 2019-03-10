package com.clashsoft.dungeonrun.world;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChunkSaver extends Thread
{
	private volatile boolean isRunning = true;

	private Queue<Chunk> queue = new ConcurrentLinkedQueue<>();

	public ChunkSaver()
	{
		super("Chunk-Saver");
	}

	@Override
	public synchronized void start()
	{
		this.isRunning = true;

		super.start();
	}

	public void disable()
	{
		this.isRunning = false;
	}

	public void enqueue(Chunk chunk)
	{
		this.queue.add(chunk);
	}

	@Override
	public void run()
	{
		while (this.isRunning)
		{
			while (!this.queue.isEmpty())
			{
				final Chunk chunk = this.queue.poll();
				chunk.world.saveChunk(chunk);
			}

			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException ignored)
			{
			}
		}
	}
}
