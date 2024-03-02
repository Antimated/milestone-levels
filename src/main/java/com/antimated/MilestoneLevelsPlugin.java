package com.antimated;

import com.antimated.notifications.NotificationsManager;
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
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(name = "Milestone levels")
public class MilestoneLevelsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MilestoneLevelsConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private NotificationsManager notifications;

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

		// Only show notifications if valid config
		if (!shouldDisplayNotificationForLevel(currentLevel) || !shouldDisplayNotificationForSkill(skill))
		{
			return;
		}

		String title = replaceSkillAndLevel(config.notificationTitle(), skill, currentLevel);
		String text = replaceSkillAndLevel(config.notificationText(), skill, currentLevel);

		notifications.addNotification(title, text);
	}

	private String replaceSkillAndLevel(String text, Skill skill, int level)
	{
		return Text.removeTags(text
			.replaceAll("\\$skill", skill.getName())
			.replaceAll("\\$level", Integer.toString(level)));
	}

	private boolean shouldDisplayNotificationForLevel(int level)
	{
		switch (config.showNotifications())
		{
			case ALWAYS:
				return level > 1 && level <= 99;
			case EVERY_10_LEVELS_AND_99:
			default:
				return (level > 10 && level % 10 == 0) || level == 99;
		}
	}

	private boolean shouldDisplayNotificationForSkill(Skill skill)
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

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted)
	{
		if (developerMode && commandExecuted.getCommand().equals("level"))
		{
			String level = Strings.join(commandExecuted.getArguments(), " ");

			if (!level.isEmpty())
			{
				int currentLevel = Integer.parseInt(level);
				Skill skill = Skill.AGILITY;

				// Don't trigger within last man standing as you would get a boatload of levels
				if (isPlayerWithinMapRegion(LAST_MAN_STANDING_REGIONS))
				{
					return;
				}

				if (!shouldDisplayNotificationForLevel(currentLevel)) {
					log.debug("Should not show notifications for level {} with showNotifications() set to {}", currentLevel, config.showNotifications());
					return;
				}

				if (!shouldDisplayNotificationForSkill(skill)) {
					log.debug("Should not show notifications for skill {} with showNotifications() set to {}", skill.getName(), config.showNotifications());
					return;
				}

				String title = replaceSkillAndLevel(config.notificationTitle(), skill, currentLevel);
				String text = replaceSkillAndLevel(config.notificationText(), skill, currentLevel);

				notifications.addNotification(title, text);
			}
		}
	}
}
