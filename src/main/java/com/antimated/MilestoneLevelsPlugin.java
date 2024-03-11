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
	tags = {"level", "skill", "notification", "notifier"}
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

		onLevelUp(skill, currentLevel);
	}

	private void onLevelUp(Skill skill, int level)
	{
		if (!displayNotificationForLevel(level))
		{
			log.debug("Cannot show notification for level '{}' as level is not in list of valid levels.", level);
			return;
		}

		if (!displayNotificationForSkill(skill))
		{
			log.debug("Cannot show notification for disabled skill '{}'.", skill.getName());
			return;
		}

		String title = replaceSkillAndLevel(config.notificationTitle(), skill, level);
		String text = replaceSkillAndLevel(config.notificationText(), skill, level);
		int color = getIntValue(config.notificationColor());

		notifications.addNotification(title, text, color);
	}

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
	 * Checks whether a notification should be displayed for a given level.
	 *
	 * @param level int
	 * @return boolean
	 */
	private boolean displayNotificationForLevel(int level)
	{
		// Convert our comma separated list to a list of integers (filter out non integer values and invalid levels)
		List<Integer> levels = Text.fromCSV(config.showOnLevels()).stream()
			.distinct()
			.filter(MilestoneLevelsPlugin::isInteger)
			.map(Integer::parseInt)
			.filter(MilestoneLevelsPlugin::isValidLevel)
			.collect(Collectors.toList());

		return levels.isEmpty() && isValidLevel(level) || levels.contains(level);
	}

	/**
	 * Checks whether a notification should be displayed for the given skill.
	 *
	 * @param skill Skill
	 * @return boolean
	 */
	private boolean displayNotificationForSkill(Skill skill)
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
						Skill skill = Skill.valueOf(args[0].toUpperCase());
						int currentLevel = Integer.parseInt(args[1]);

						onLevelUp(skill, currentLevel);
					}
					else
					{
						log.debug("Invalid number of arguments for ::level command. Expected 2 got {}.", args.length);
					}
					break;

				case "notify":
					for (int i = 1; i <= 500; i++)
					{
						notifications.addNotification("Notification", "Test notification number: <col=ffffff>" + i + "</col>");
					}

					break;
			}
		}
	}
}
