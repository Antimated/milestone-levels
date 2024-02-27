package com.antimated;

import com.google.inject.Provides;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(name = "Example")
public class TaskListPlugin extends Plugin
{
	private final Map<Skill, Integer> skillLevel = new HashMap<>();

	@Inject
	private Client client;

	@Inject
	private TaskListConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private NotificationManager notifications;

	@Override
	protected void startUp() throws Exception
	{
		eventBus.register(notifications);
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		eventBus.unregister(notifications);
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Subscribe
	public void onOverheadTextChanged(OverheadTextChanged e)
	{
		if (e.getActor().equals(client.getLocalPlayer()))
		{
			notifications.addNotification("Test", e.getOverheadText(), 0x00ff00);
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		onLevelUp(statChanged);
	}

	public void onLevelUp(StatChanged statChanged)
	{
		final Skill skill = statChanged.getSkill();
		final int currentLevel = statChanged.getLevel();
		final Integer previousLevel = skillLevel.put(skill, currentLevel);

		// Only process when a previousLevel is logged or if the level hasn't changed.
		if (previousLevel == null || previousLevel >= currentLevel)
		{
			return;
		}

		// Only notify when we reach levels 10, 20, 30, 40, 50, 60, 70, 80, 90 and 99
		if (currentLevel % 10 == 0 || currentLevel == 99)
		{
			notifications.addNotification("Task completed", "Reach level " + currentLevel + " in " + skill.getName());
		}
	}

	@Provides
	TaskListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskListConfig.class);
	}
}
