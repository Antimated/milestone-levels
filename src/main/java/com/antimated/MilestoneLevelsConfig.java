package com.antimated;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("milestoneLevels")
public interface MilestoneLevelsConfig extends Config
{
	@ConfigSection(
		name = "Notification",
		description = "Contents to be shown on the level-up notification.",
		position = 0
	)
	String notificationConfig = "notificationConfig";

	@ConfigItem(
		keyName = "notificationTitle",
		name = "Title",
		description = "Can include $level and $skill variables.",
		section = notificationConfig,
		position = 0
	)
	default String notificationTitle()
	{
		return "Level milestone";
	}

	@ConfigItem(
		keyName = "notificationText",
		name = "Text",
		description = "Can include $level and $skill variables.",
		section = notificationConfig,
		position = 1
	)
	default String notificationText()
	{
		return "Gained level $level in $skill!";
	}

	@ConfigSection(
		name = "Levels & skills",
		description = "Settings for when to display notifications",
		position = 1
	)
	String settingsConfig = "settingsConfig";

	@ConfigItem(
		keyName = "showOnLevels",
		name = "Levels",
		description = "Configures levels to display notifications on. Format: (Level), (Level). When empty it displays notifications for all levels.",
		section = settingsConfig,
		position = 1
	)
	default String showOnLevels()
	{
		return "10, 20, 30, 40, 50, 60, 70, 80, 90, 99";
	}

	@ConfigItem(
		keyName = "showAttackNotifications",
		name = "Attack",
		description = "Should we show Attack notifications?",
		section = settingsConfig,
		position = 2
	)
	default boolean showAttackNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showDefenceNotifications",
		name = "Defence",
		description = "Should we show Defence notifications?",
		section = settingsConfig,
		position = 3
	)
	default boolean showDefenceNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showStrengthNotifications",
		name = "Strength",
		description = "Should we show Strength notifications?",
		section = settingsConfig,
		position = 4
	)
	default boolean showStrengthNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showHitpointsNotifications",
		name = "Hitpoints",
		description = "Should we show Hitpoints notifications?",
		section = settingsConfig,
		position = 5
	)
	default boolean showHitpointsNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRangedNotifications",
		name = "Ranged",
		description = "Should we show Ranged notifications?",
		section = settingsConfig,
		position = 6
	)
	default boolean showRangedNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showPrayerNotifications",
		name = "Prayer",
		description = "Should we show Prayer notifications?",
		section = settingsConfig,
		position = 7
	)
	default boolean showPrayerNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showMagicNotifications",
		name = "Magic",
		description = "Should we show Magic notifications?",
		section = settingsConfig,
		position = 8
	)
	default boolean showMagicNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showCookingNotifications",
		name = "Cooking",
		description = "Should we show Cooking notifications?",
		section = settingsConfig,
		position = 9
	)
	default boolean showCookingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showWoodcuttingNotifications",
		name = "Woodcutting",
		description = "Should we show Woodcutting notifications?",
		section = settingsConfig,
		position = 10
	)
	default boolean showWoodcuttingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFletchingNotifications",
		name = "Fletching",
		description = "Should we show Fletching notifications?",
		section = settingsConfig,
		position = 11
	)
	default boolean showFletchingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFishingNotifications",
		name = "Fishing",
		description = "Should we show Fishing notifications?",
		section = settingsConfig,
		position = 12
	)
	default boolean showFishingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFiremakingNotifications",
		name = "Firemaking",
		description = "Should we show Firemaking notifications?",
		section = settingsConfig,
		position = 13
	)
	default boolean showFiremakingNotifications()
	{
		return true;
	}


	@ConfigItem(
		keyName = "showCraftingNotifications",
		name = "Crafting",
		description = "Should we show Crafting notifications?",
		section = settingsConfig,
		position = 14
	)
	default boolean showCraftingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSmithingNotifications",
		name = "Smithing",
		description = "Should we show Smithing notifications?",
		section = settingsConfig,
		position = 15
	)
	default boolean showSmithingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showMiningNotifications",
		name = "Mining",
		description = "Should we show Mining notifications?",
		section = settingsConfig,
		position = 16
	)
	default boolean showMiningNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showHerbloreNotifications",
		name = "Herblore",
		description = "Should we show Herblore notifications?",
		section = settingsConfig,
		position = 17
	)
	default boolean showHerbloreNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showAgilityNotifications",
		name = "Agility",
		description = "Should we show Agility notifications?",
		section = settingsConfig,
		position = 18
	)
	default boolean showAgilityNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showThievingNotifications",
		name = "Thieving",
		description = "Should we show Thieving notifications?",
		section = settingsConfig,
		position = 19
	)
	default boolean showThievingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSlayerNotifications",
		name = "Slayer",
		description = "Should we show Slayer notifications?",
		section = settingsConfig,
		position = 20
	)
	default boolean showSlayerNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFarmingNotifications",
		name = "Farming",
		description = "Should we show Farming notifications?",
		section = settingsConfig,
		position = 21
	)
	default boolean showFarmingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRunecraftNotifications",
		name = "Runecraft",
		description = "Should we show Runecraft notifications?",
		section = settingsConfig,
		position = 22
	)
	default boolean showRunecraftNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showHunterNotifications",
		name = "Hunter",
		description = "Should we show Hunter notifications?",
		section = settingsConfig,
		position = 23
	)
	default boolean showHunterNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showConstructionNotifications",
		name = "Construction",
		description = "Should we show Construction notifications?",
		section = settingsConfig,
		position = 24
	)
	default boolean showConstructionNotifications()
	{
		return true;
	}
}
