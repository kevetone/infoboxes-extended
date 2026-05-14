package com.infoboxes_extended;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class InfoboxesExtendedTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(InfoboxesExtendedPlugin.class);
		RuneLite.main(args);
	}
}