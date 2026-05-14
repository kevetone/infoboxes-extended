package com.infoboxes_extended;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@Slf4j
@PluginDescriptor(
	name = "Infoboxes Extended"
)
public class InfoboxesExtendedPlugin extends Plugin
{
	private Integer startCount = 0;
	private final Deque<TestInfoBox> testInfoBoxes = new ArrayDeque<>();

	@Inject
	private Client client;

	@Inject
	private InfoboxesExtendedConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private InfoBoxManager infoBoxManager;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Infoboxes Extended started {} times!", ++startCount);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Infoboxes Extended stopped!");
		infoBoxManager.removeIf(b -> b instanceof TestInfoBox);
		testInfoBoxes.clear();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals(InfoboxesExtendedConfig.GROUP))
		{
			return;
		}

		if (event.getKey().equals("addTestInfobox") && Boolean.parseBoolean(event.getNewValue()))
		{
			configManager.setConfiguration(InfoboxesExtendedConfig.GROUP, "addTestInfobox", false);
			addTestInfobox();
		}
		else if (event.getKey().equals("removeTestInfobox") && Boolean.parseBoolean(event.getNewValue()))
		{
			configManager.setConfiguration(InfoboxesExtendedConfig.GROUP, "removeTestInfobox", false);
			removeOldestTestInfobox();
		}
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted)
	{
		if (commandExecuted.getCommand().equals("test"))
		{
			log.debug("hotswap test triggered");
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Infoboxes Extended is alive!", null);
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Provides
	InfoboxesExtendedConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(InfoboxesExtendedConfig.class);
	}

	private void addTestInfobox()
	{
		BufferedImage image = new BufferedImage(36, 36, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setColor(new Color(100, 149, 237));
		g.fillRect(0, 0, 36, 36);
		g.dispose();

		TestInfoBox box = new TestInfoBox(image, this, Duration.ofMinutes(30));
		testInfoBoxes.addLast(box);
		infoBoxManager.addInfoBox(box);
		log.debug("test infobox added ({} active)", testInfoBoxes.size());
	}

	private void removeOldestTestInfobox()
	{
		// skip any that already expired and were culled
		TestInfoBox box;
		do
		{
			box = testInfoBoxes.pollFirst();
		}
		while (box != null && box.cull());

		if (box == null)
		{
			log.debug("no active test infoboxes to remove");
			return;
		}

		infoBoxManager.removeInfoBox(box);
		log.debug("oldest test infobox removed ({} remaining)", testInfoBoxes.size());
	}
}
