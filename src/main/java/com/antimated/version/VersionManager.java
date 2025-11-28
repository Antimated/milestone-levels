package com.antimated.version;

import com.antimated.MilestoneLevelsConfig;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

@Slf4j
@Singleton
public class VersionManager
{
	@Inject
	private EventBus eventBus;
	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private ConfigManager configManager;

	private final String LAST_UPDATE_MESSAGE_KEY = "lastupdatemessage";

	private final String UPDATE_MESSAGE = "Milestone Levels v1.1.0 is here. Enjoy the XP milestones!";

	public void startUp()
	{
		log.debug("VersionManager startUp()");
		eventBus.register(this);
	}

	public void shutDown()
	{
		log.debug("VersionManager shutDown()");
		eventBus.unregister(this);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged gameStateChanged)
	{
		switch (gameStateChanged.getGameState())
		{
			case LOGGED_IN:
				if (hasLastUpdateMessage())
				{
					log.debug("Has last update message {}", getLastUpdateMessage());
					return;
				}

				log.debug("Previous update message {}", getLastUpdateMessage());
				setLastUpdateMessage();
				log.debug("Last updated message {}", getLastUpdateMessage());

				chatMessageManager.queue(QueuedMessage.builder()
					.type(ChatMessageType.GAMEMESSAGE)
					.runeLiteFormattedMessage("<col=ff0000>" + getLastUpdateMessage() + "</col>")
					.build());


				break;
		}
	}

	private String getLastUpdateMessage()
	{
		return configManager.getConfiguration(MilestoneLevelsConfig.CONFIG_GROUP, LAST_UPDATE_MESSAGE_KEY);
	}

	private void setLastUpdateMessage()
	{
		configManager.setConfiguration(MilestoneLevelsConfig.CONFIG_GROUP, LAST_UPDATE_MESSAGE_KEY, UPDATE_MESSAGE);
	}

	public void clearLastUpdateMessage()
	{
		configManager.unsetConfiguration(MilestoneLevelsConfig.CONFIG_GROUP, LAST_UPDATE_MESSAGE_KEY);
	}

	private boolean hasLastUpdateMessage()
	{
		return getLastUpdateMessage() != null && getLastUpdateMessage().equals(UPDATE_MESSAGE);
	}

}
