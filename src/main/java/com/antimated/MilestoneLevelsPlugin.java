package com.antimated;

import com.antimated.notifications.NotificationManager;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Milestone Levels",
	description = "Display milestone levels on a fancy league-like notification",
	tags = {"level", "skill", "notification", "notifier", "milestone"}
)
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

	private static final Set<Integer> LAST_MAN_STANDING_REGIONS = ImmutableSet.of(13658, 13659, 13660, 13914, 13915, 13916, 13918, 13919, 13920, 14174, 14175, 14176, 14430, 14431, 14432);

	@Provides
	MilestoneLevelsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MilestoneLevelsConfig.class);
	}

	@Override
	protected void startUp()
	{
		notifications.startUp();
	}

	@Override
	protected void shutDown()
	{
		notifications.shutDown();
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		// Must be on a regular game world
		if (RuneScapeProfileType.getCurrent(client) != RuneScapeProfileType.STANDARD)
		{
			return;
		}

		// Player must not be in LMS
		if (isPlayerWithinMapRegion(LAST_MAN_STANDING_REGIONS))
		{
			return;
		}

		// Current stat change level
		final Skill skill = statChanged.getSkill();
		final int currentLevel = statChanged.getLevel();
		final Integer previousLevel = skillLevel.put(skill, currentLevel);

		// Previous level has to be set, and if we have leveled up
		if (previousLevel == null || previousLevel >= currentLevel)
		{
			return;
		}

		// We have leveled up, now check for multi levels (imagine going from level 1 to 40 in one go)
		if (config.showMultiLevels())
		{
			for (int multiLevel = previousLevel + 1; multiLevel <= currentLevel; multiLevel++)
			{
				onLevelUp(skill, multiLevel);
			}

			return;
		}

		onLevelUp(skill, currentLevel);
	}

	/**
	 * Adds a level-up notification to the queue if certain requirements are met.
	 * @param skill Skill
	 * @param level int
	 */
	private void onLevelUp(Skill skill, int level)
	{
		if (!displayNotificationForLevel(level))
		{
			return;
		}

		if (!displayNotificationForSkill(skill))
		{
			return;
		}

		String title = replaceSkillAndLevel(config.notificationTitle(), skill, level);
		String text = replaceSkillAndLevel(config.notificationText(), skill, level);
		int color = getIntValue(config.notificationColor());

		log.debug("Leveled up {} to {}", skill.getName(), level);
		notifications.addNotification(title, text, color);
	}

	/**
	 * Gets the int value for a color.
	 *
	 * @param color color
	 * @return int
	 */
	private int getIntValue(Color color)
	{
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();

		// Combine RGB values into a single integer
		return (red << 16) | (green << 8) | blue;
	}

	/**
	 * Replaces the words $skill and $level from the text to the passed skill and level respectively
	 *
	 * @param text  String
	 * @param skill Skill
	 * @param level int
	 * @return String
	 */
	private String replaceSkillAndLevel(String text, Skill skill, int level)
	{
		return Text.removeTags(text
			.replaceAll("\\$skill", skill.getName())
			.replaceAll("\\$level", Integer.toString(level)));
	}

	/**
	 * Converts a list of comma separated levels to an integer list.
	 *
	 * @param levels A comma separated list of levels
	 * @return List<Integer>
	 */
	private List<Integer> convertToLevels(String levels)
	{
		return Text.fromCSV(levels).stream()
			.distinct()
			.filter(MilestoneLevelsPlugin::isInteger)
			.map(Integer::parseInt)
			.filter(MilestoneLevelsPlugin::isValidLevel)
			.collect(Collectors.toList());
	}

	/**
	 * Checks whether a notification should be displayed for a given level.
	 *
	 * @param level int
	 * @return boolean
	 */
	private boolean displayNotificationForLevel(int level)
	{
		// Convert our comma separated list to a list of integers (filter out non integer values and invalid levels)
		List<Integer> levels = convertToLevels(config.showOnLevels());

		return levels.isEmpty() && isValidLevel(level) || levels.contains(level);
	}

	/**
	 * Checks whether a notification should be displayed for the given skill.
	 *
	 * @param skill Skill
	 * @return boolean
	 */
	private boolean displayNotificationForSkill(@NonNull Skill skill)
	{
		switch (skill)
		{
			case ATTACK:
				return config.showAttackNotifications();
			case DEFENCE:
				return config.showDefenceNotifications();
			case STRENGTH:
				return config.showStrengthNotifications();
			case HITPOINTS:
				return config.showHitpointsNotifications();
			case RANGED:
				return config.showRangedNotifications();
			case PRAYER:
				return config.showPrayerNotifications();
			case MAGIC:
				return config.showMagicNotifications();
			case COOKING:
				return config.showCookingNotifications();
			case WOODCUTTING:
				return config.showWoodcuttingNotifications();
			case FLETCHING:
				return config.showFletchingNotifications();
			case FISHING:
				return config.showFishingNotifications();
			case FIREMAKING:
				return config.showFiremakingNotifications();
			case CRAFTING:
				return config.showCraftingNotifications();
			case SMITHING:
				return config.showSmithingNotifications();
			case MINING:
				return config.showMiningNotifications();
			case HERBLORE:
				return config.showHerbloreNotifications();
			case AGILITY:
				return config.showAgilityNotifications();
			case THIEVING:
				return config.showThievingNotifications();
			case SLAYER:
				return config.showSlayerNotifications();
			case FARMING:
				return config.showFarmingNotifications();
			case RUNECRAFT:
				return config.showRunecraftNotifications();
			case HUNTER:
				return config.showHunterNotifications();
			case CONSTRUCTION:
				return config.showConstructionNotifications();
		}

		return true;
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

	/**
	 * @param string String
	 * @return boolean
	 */
	private static boolean isInteger(String string)
	{
		try
		{
			Integer.parseInt(string);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	/**
	 * Checks if a passed level is a valid level (1 - 99)
	 *
	 * @param level Integer
	 * @return boolean
	 */
	private static boolean isValidLevel(Integer level)
	{
		return level >= 1 && level <= 99;
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted)
	{
		if (developerMode)
		{
			String[] args = commandExecuted.getArguments();
			switch (commandExecuted.getCommand())
			{
				case "level":
					if (args.length == 2)
					{
						try
						{
							Skill skill = Skill.valueOf(args[0].toUpperCase());
							int currentLevel = Integer.parseInt(args[1]);

							onLevelUp(skill, currentLevel);
						}
						catch (IllegalArgumentException e)
						{
							log.debug("Invalid arguments for ::level command. {}.", e.getMessage());
						}

					}
					else
					{
						for (Skill skill : Skill.values())
						{
							for (int currentLevel = 1; currentLevel <= 99; currentLevel++)
							{
								onLevelUp(skill, currentLevel);
							}
						}

						log.debug("Invalid number of arguments for ::level command. Expected 2 got {}.", args.length);
					}
					break;

				case "notify":
					for (int i = 1; i <= 500; i++)
					{
						notifications.addNotification("Notification", "Test notification number: <col=ffffff>" + i + "</col>");
					}

					break;
				case "clear":
					notifications.clearNotifications();
					break;
			}
		}
	}
}
