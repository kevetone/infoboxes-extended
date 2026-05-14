package com.infoboxes_extended;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

class TestInfoBox extends InfoBox
{
	private final Instant endTime;

	TestInfoBox(BufferedImage image, Plugin plugin, Duration duration)
	{
		super(image, plugin);
		this.endTime = Instant.now().plus(duration);
		setTooltip("Test infobox");
	}

	@Override
	public String getText()
	{
		Duration remaining = Duration.between(Instant.now(), endTime);
		if (remaining.isNegative())
		{
			return "0:00";
		}
		long minutes = remaining.toMinutes();
		long seconds = remaining.minusMinutes(minutes).getSeconds();
		return String.format("%d:%02d", minutes, seconds);
	}

	@Override
	public Color getTextColor()
	{
		return Color.WHITE;
	}

	@Override
	public boolean cull()
	{
		return Instant.now().isAfter(endTime);
	}
}
