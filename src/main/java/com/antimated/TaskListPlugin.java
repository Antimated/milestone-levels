package com.antimated;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.CommandExecuted;
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

	private static final Set<Integer> LAST_MAN_STANDING_DESERTED_ISLAND_REGION = ImmutableSet.of(13658, 13659, 13914, 13915, 13916);

	private static final Set<Integer> LAST_MAN_STANDING_WILD_VARROCK_REGION = ImmutableSet.of(13918, 13919, 13920, 14174, 14175, 14176, 14430, 14431, 14432);

	private static final Set<Integer> LAST_MAN_STANDING_REGION = ImmutableSet.<Integer>builder().addAll(LAST_MAN_STANDING_DESERTED_ISLAND_REGION).addAll(LAST_MAN_STANDING_WILD_VARROCK_REGION).build();

	@Provides
	TaskListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskListConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		eventBus.register(notifications);
		notifications.startUp();
	}

	@Override
	protected void shutDown() throws Exception
	{
		eventBus.unregister(notifications);
		notifications.shutDown();
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		// Don't trigger within last man standing as you would get a boatload of levels
		if (isWithinMapRegions(LAST_MAN_STANDING_REGION)) {
			return;
		}

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
			notifications.addNotification("Milestone level", "Gained level " + currentLevel + " in " + skill.getName() + "!");

			// TODO: Add notifications when all skills are a min of 10, 20, 30, 40, 50, 60, 70, 80, 90 and 99
		}
	}

	private boolean isWithinMapRegions(Set<Integer> definedMapRegions)
	{
		final int[] mapRegions = client.getMapRegions();

		for (int region : mapRegions)
		{
			if (definedMapRegions.contains(region))
			{
				return true;
			}
		}

		return false;
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted)
	{
		if (developerMode && commandExecuted.getCommand().equals("level"))
		{
			// Don't trigger within last man standing as you would get a boatload of levels
			if (isWithinMapRegions(LAST_MAN_STANDING_REGION)) {
				return;
			}

			String text = Strings.join(commandExecuted.getArguments(), " ");

			if (!text.isEmpty())
			{
				int currentLevel = Integer.parseInt(text);

				if (currentLevel % 10 == 0 || currentLevel == 99)
				{
					notifications.addNotification("Milestone level", "Gained level " + currentLevel + " in " + Skill.AGILITY.getName() + "!");
				}
				else
				{
					log.debug("Invalid level given");
				}
			}

		}
	}
}
