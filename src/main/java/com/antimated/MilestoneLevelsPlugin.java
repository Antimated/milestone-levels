package com.antimated;

import com.antimated.notifications.NotificationManager;
import com.antimated.util.Util;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.StatChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
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
	private ClientThread clientThread;

	@Inject
	private MilestoneLevelsConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private NotificationManager notifications;

	@Inject
	@Named("developerMode")
	boolean developerMode;

	private final Map<Skill, Integer> previousXpMap = new EnumMap<>(Skill.class);

	private static final Set<Integer> LAST_MAN_STANDING_REGIONS = ImmutableSet.of(13658, 13659, 13660, 13914, 13915, 13916, 13918, 13919, 13920, 14174, 14175, 14176, 14430, 14431, 14432);

	@Provides
	MilestoneLevelsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MilestoneLevelsConfig.class);
	}

	@Override
	protected void startUp()
	{
		clientThread.invoke(this::initializePreviousXpMap);
		notifications.startUp();
	}

	@Override
	protected void shutDown()
	{
		previousXpMap.clear();
		notifications.shutDown();
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		final Skill skill = statChanged.getSkill();

		final int currentXp = statChanged.getXp();
		final int currentLevel = Experience.getLevelForXp(currentXp);

		final int previousXp = previousXpMap.getOrDefault(skill, -1);
		final int previousLevel = previousXp == -1 ? -1 : Experience.getLevelForXp(previousXp);

		previousXpMap.put(skill, currentXp);

		// Previous level has to be set, and if we have leveled up
		if (previousLevel == -1 || previousLevel >= currentLevel)
		{
			return;
		}

		// Only standard worlds are allowed, and player shouldn't be in LMS
		if (Util.isStandardWorld(client) || Util.isPlayerWithinMapRegion(client, LAST_MAN_STANDING_REGIONS))
		{
			log.debug("Not on a standard world or in a LMS game.");
			return;
		}

		// Check for multi-leveling
		for (int level = previousLevel + 1; level <= currentLevel; level++)
		{
			if (shouldNotifyForRealLevel(level) && shouldNotifyForSkill(skill))
			{
				notify(skill, level);
				continue;
			}

			// Valid virtual levels with the showVirtualLevels setting should always display
			// regardless of the level list or enabled skills.
			if (shouldNotifyForVirtualLevel(level))
			{
				notify(skill, level);
			}
		}
	}

	/**
	 * Gets list of valid real levels from config
	 *
	 * @return List<Integer>
	 */
	private List<Integer> getLevelList()
	{
		return Text.fromCSV(config.showOnLevels()).stream()
			.distinct()
			.filter(Util::isInteger)
			.map(Integer::parseInt)
			.filter(Util::isValidRealLevel)
			.collect(Collectors.toList());
	}

	/**
	 * Populate initial xp per skill.
	 */
	private void initializePreviousXpMap()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			previousXpMap.clear();
		}
		else
		{
			for (final Skill skill : Skill.values())
			{
				previousXpMap.put(skill, client.getSkillExperience(skill));
			}
		}
	}

	/**
	 * Adds a level-up notification to the queue if certain requirements are met.
	 *
	 * @param skill Skill
	 * @param level int
	 */
	private void notify(Skill skill, int level)
	{
		String title = Util.replaceSkillAndLevel(config.notificationTitle(), skill, level);
		String text = Util.replaceSkillAndLevel(config.notificationText(), skill, level);
		int color = Util.getIntValue(config.notificationColor());

		log.debug("Notify level up for {} to level {}", skill.getName(), level);
		notifications.addNotification(title, text, color);
	}

	/**
	 * Check if we should notify for the given potential real level
	 * @param level int
	 * @return boolean
	 */
	private boolean shouldNotifyForRealLevel(int level)
	{
		return Util.isValidRealLevel(level) && (getLevelList().contains(level) || getLevelList().isEmpty());
	}

	/**
	 * Check if we should notify for the given potential virtual level
	 * @param level int
	 * @return boolean
	 */
	private boolean shouldNotifyForVirtualLevel(int level)
	{
		return Util.isValidVirtualLevel(level) && config.showVirtualLevels();
	}

	/**
	 * Check if we should notify for the given skill based off of our config settings.
	 * @param skill Skill
	 * @return boolean
	 */
	private boolean shouldNotifyForSkill(Skill skill)
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

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted)
	{
		if (developerMode)
		{
			String[] args = commandExecuted.getArguments();
			switch (commandExecuted.getCommand())
			{
				case "clear":
					notifications.clearNotifications();
					break;
			}
		}
	}
}
