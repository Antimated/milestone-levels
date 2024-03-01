package com.antimated;

import com.antimated.notification.NotificationManager;
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
public class MilestoneLevelsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MilestoneLevelsConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private NotificationManager notifications;

	@Inject
	@Named("developerMode")
	boolean developerMode;

	private final Map<Skill, Integer> skillLevel = new HashMap<>();

	// Last man standing map regions
	private static final Set<Integer> LAST_MAN_STANDING_REGIONS = ImmutableSet.of(13658, 13659, 13660, 13914, 13915, 13916, 13918, 13919, 13920, 14174, 14175, 14176, 14430, 14431, 14432);

	@Provides
	MilestoneLevelsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MilestoneLevelsConfig.class);
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
		// Ignore Last Man Standing
		if (isPlayerWithinMapRegion(LAST_MAN_STANDING_REGIONS))
		{
			return;
		}

		final Skill skill = statChanged.getSkill();
		final int currentLevel = statChanged.getLevel();
		final Integer previousLevel = skillLevel.put(skill, currentLevel);

		// Previous level has to be set, and the previous level can not be larger or equal to the current level.
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

	/**
	 * Is player currently within the provided map regions
	 */
	private boolean isPlayerWithinMapRegion(Set<Integer> definedMapRegions)
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
			if (isPlayerWithinMapRegion(LAST_MAN_STANDING_REGIONS))
			{
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
