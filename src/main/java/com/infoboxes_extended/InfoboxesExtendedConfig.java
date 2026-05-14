package com.infoboxes_extended;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(InfoboxesExtendedConfig.GROUP)
public interface InfoboxesExtendedConfig extends Config
{
	String GROUP = "infoboxes_extended";

	@ConfigItem(
		keyName = "greeting",
		name = "Welcome Greeting",
		description = "The message to show to the user when they login"
	)
	default String greeting()
	{
		return "Hello";
	}

	@ConfigSection(
		name = "Dev Helpers",
		description = "Tools for testing during development",
		position = 99,
		closedByDefault = true
	)
	String devSection = "devSection";

	@ConfigItem(
		keyName = "addTestInfobox",
		name = "+ Add Test Infobox",
		description = "Adds a generic 5-minute countdown infobox",
		section = devSection,
		position = 0
	)
	default boolean addTestInfobox()
	{
		return false;
	}

	@ConfigItem(
		keyName = "removeTestInfobox",
		name = "- Remove Oldest Infobox",
		description = "Removes the oldest active test infobox",
		section = devSection,
		position = 1
	)
	default boolean removeTestInfobox()
	{
		return false;
	}
}
