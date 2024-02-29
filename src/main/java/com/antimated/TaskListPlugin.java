package com.antimated;

import com.google.inject.Provides;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(name = "Task List")
public class TaskListPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private TaskListConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private NotificationManager notifications;

	@Inject
	@Named("developerMode")
	boolean developerMode;


	private final Map<Skill, Integer> skillLevel = new HashMap<>();

	@Override
	protected void startUp() throws Exception
	{
		eventBus.register(notifications);
	}

	@Override
	protected void shutDown() throws Exception
	{
		eventBus.unregister(notifications);
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

			// TODO: Add notifications when all skills are a min of 10, 20, 30, 40, 50, 60, 70, 80, 90 and 99
		}
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted)
	{
		if (developerMode && commandExecuted.getCommand().equals("level"))
		{
			for (int i = 0; i < 500; i++) {
				notifications.addNotification("Test notification", "Test notification number " + i);
			}

//			String text = Strings.join(commandExecuted.getArguments(), " ");
//
//			if (text.isEmpty())
//			{
//				//resetClue(true);
//			}
//			else
//			{
//				ClueScroll clueScroll = findClueScroll(text);
//				log.debug("Found clue scroll for '{}': {}", text, clueScroll);
//				updateClue(clueScroll);
//			}
		}
	}

	@Provides
	TaskListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskListConfig.class);
	}
}
