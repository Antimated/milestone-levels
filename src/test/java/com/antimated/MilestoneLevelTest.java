package com.antimated;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MilestoneLevelTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(MilestoneLevelsPlugin.class);
		RuneLite.main(args);
	}
}