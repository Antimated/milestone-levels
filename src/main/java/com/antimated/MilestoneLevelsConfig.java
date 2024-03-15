package com.antimated;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.ui.JagexColors;

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

	@ConfigItem(
		keyName = "notificationColor",
		name = "Color",
		description = "Changes the color of the notification title and text.",
		section = notificationConfig,
		position = 2
	)
	default Color notificationColor()
	{
		return JagexColors.DARK_ORANGE_INTERFACE_TEXT;
	}

	@ConfigSection(
		name = "Levels",
		description = "Settings for what levels we want to display notifications on",
		position = 1
	)
	String levelsConfig = "levelsConfig";

	@ConfigItem(
		keyName = "showOnLevels",
		name = "Levels",
		description = "Configures levels to display notifications on. Format: (Level), (Level). When empty it displays notifications for all levels.",
		section = levelsConfig,
		position = 0
	)
	default String showOnLevels()
	{
		return "10, 20, 30, 40, 50, 60, 70, 80, 90, 99";
	}

	@ConfigItem(
		keyName = "showVirtualLevels",
		name = "Notify for virtual levels",
		description = "Notify when leveling a virtual level. Ignores the list of levels and skills.",
		section = levelsConfig,
		position = 1
	)
	default boolean showVirtualLevels()
	{
		return true;
	}

	@ConfigSection(
		name = "Skills",
		description = "Settings for what skills we want to display notifications on",
		position = 2
	)
	String skillsConfig = "skillsConfig";

	@ConfigItem(
		keyName = "showAttackNotifications",
		name = "Attack",
		description = "Should we show Attack notifications?",
		section = skillsConfig
	)
	default boolean showAttackNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showDefenceNotifications",
		name = "Defence",
		description = "Should we show Defence notifications?",
		section = skillsConfig
	)
	default boolean showDefenceNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showStrengthNotifications",
		name = "Strength",
		description = "Should we show Strength notifications?",
		section = skillsConfig
	)
	default boolean showStrengthNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showHitpointsNotifications",
		name = "Hitpoints",
		description = "Should we show Hitpoints notifications?",
		section = skillsConfig
	)
	default boolean showHitpointsNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRangedNotifications",
		name = "Ranged",
		description = "Should we show Ranged notifications?",
		section = skillsConfig
	)
	default boolean showRangedNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showPrayerNotifications",
		name = "Prayer",
		description = "Should we show Prayer notifications?",
		section = skillsConfig
	)
	default boolean showPrayerNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showMagicNotifications",
		name = "Magic",
		description = "Should we show Magic notifications?",
		section = skillsConfig
	)
	default boolean showMagicNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showCookingNotifications",
		name = "Cooking",
		description = "Should we show Cooking notifications?",
		section = skillsConfig
	)
	default boolean showCookingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showWoodcuttingNotifications",
		name = "Woodcutting",
		description = "Should we show Woodcutting notifications?",
		section = skillsConfig
	)
	default boolean showWoodcuttingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFletchingNotifications",
		name = "Fletching",
		description = "Should we show Fletching notifications?",
		section = skillsConfig
	)
	default boolean showFletchingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFishingNotifications",
		name = "Fishing",
		description = "Should we show Fishing notifications?",
		section = skillsConfig
	)
	default boolean showFishingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFiremakingNotifications",
		name = "Firemaking",
		description = "Should we show Firemaking notifications?",
		section = skillsConfig
	)
	default boolean showFiremakingNotifications()
	{
		return true;
	}


	@ConfigItem(
		keyName = "showCraftingNotifications",
		name = "Crafting",
		description = "Should we show Crafting notifications?",
		section = skillsConfig
	)
	default boolean showCraftingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSmithingNotifications",
		name = "Smithing",
		description = "Should we show Smithing notifications?",
		section = skillsConfig
	)
	default boolean showSmithingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showMiningNotifications",
		name = "Mining",
		description = "Should we show Mining notifications?",
		section = skillsConfig
	)
	default boolean showMiningNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showHerbloreNotifications",
		name = "Herblore",
		description = "Should we show Herblore notifications?",
		section = skillsConfig
	)
	default boolean showHerbloreNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showAgilityNotifications",
		name = "Agility",
		description = "Should we show Agility notifications?",
		section = skillsConfig
	)
	default boolean showAgilityNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showThievingNotifications",
		name = "Thieving",
		description = "Should we show Thieving notifications?",
		section = skillsConfig
	)
	default boolean showThievingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSlayerNotifications",
		name = "Slayer",
		description = "Should we show Slayer notifications?",
		section = skillsConfig
	)
	default boolean showSlayerNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFarmingNotifications",
		name = "Farming",
		description = "Should we show Farming notifications?",
		section = skillsConfig
	)
	default boolean showFarmingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRunecraftNotifications",
		name = "Runecraft",
		description = "Should we show Runecraft notifications?",
		section = skillsConfig
	)
	default boolean showRunecraftNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showHunterNotifications",
		name = "Hunter",
		description = "Should we show Hunter notifications?",
		section = skillsConfig
	)
	default boolean showHunterNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showConstructionNotifications",
		name = "Construction",
		description = "Should we show Construction notifications?",
		section = skillsConfig
	)
	default boolean showConstructionNotifications()
	{
		return true;
	}
}
