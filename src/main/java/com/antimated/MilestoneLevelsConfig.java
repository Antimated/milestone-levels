package com.antimated;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.ui.JagexColors;

@ConfigGroup(MilestoneLevelsConfig.CONFIG_GROUP)
public interface MilestoneLevelsConfig extends Config
{
	String CONFIG_GROUP = "milestoneLevels";
	@ConfigSection(
		name = "Levels",
		description = "All level notification settings.",
		position = 100
	)
	String SECTION_LEVELS = "levels";

	@ConfigItem(
		keyName = "notificationTitle",
		name = "Title",
		description = "Can include $level and $skill variables.",
		section = SECTION_LEVELS,
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
		section = SECTION_LEVELS,
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
		section = SECTION_LEVELS,
		position = 2
	)
	default Color notificationColor()
	{
		return JagexColors.DARK_ORANGE_INTERFACE_TEXT;
	}

	@ConfigItem(
		keyName = "showOnLevels",
		name = "Levels",
		description = "Configures levels to display notifications on, comma separated.",
		section = SECTION_LEVELS,
		position = 3
	)
	default String showOnLevels()
	{
		return "10, 20, 30, 40, 50, 60, 70, 80, 90, 99";
	}

	@ConfigItem(
		keyName = "showVirtualLevels",
		name = "Notify for virtual levels",
		description = "Notify when leveling a virtual level. Ignores the list of levels and skills.",
		section = SECTION_LEVELS,
		position = 4
	)
	default boolean showVirtualLevels()
	{
		return true;
	}

	@ConfigSection(
		name = "Skills",
		description = "Settings for what skills we want to display notifications on",
		position = 200
	)
	String SECTION_SKILLS = "skills";

	@ConfigItem(
		keyName = "showAttackNotifications",
		name = "Attack",
		description = "Should we show Attack notifications?",
		section = SECTION_SKILLS
	)
	default boolean showAttackNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showDefenceNotifications",
		name = "Defence",
		description = "Should we show Defence notifications?",
		section = SECTION_SKILLS
	)
	default boolean showDefenceNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showStrengthNotifications",
		name = "Strength",
		description = "Should we show Strength notifications?",
		section = SECTION_SKILLS
	)
	default boolean showStrengthNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showHitpointsNotifications",
		name = "Hitpoints",
		description = "Should we show Hitpoints notifications?",
		section = SECTION_SKILLS
	)
	default boolean showHitpointsNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRangedNotifications",
		name = "Ranged",
		description = "Should we show Ranged notifications?",
		section = SECTION_SKILLS
	)
	default boolean showRangedNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showPrayerNotifications",
		name = "Prayer",
		description = "Should we show Prayer notifications?",
		section = SECTION_SKILLS
	)
	default boolean showPrayerNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showMagicNotifications",
		name = "Magic",
		description = "Should we show Magic notifications?",
		section = SECTION_SKILLS
	)
	default boolean showMagicNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showCookingNotifications",
		name = "Cooking",
		description = "Should we show Cooking notifications?",
		section = SECTION_SKILLS
	)
	default boolean showCookingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showWoodcuttingNotifications",
		name = "Woodcutting",
		description = "Should we show Woodcutting notifications?",
		section = SECTION_SKILLS
	)
	default boolean showWoodcuttingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFletchingNotifications",
		name = "Fletching",
		description = "Should we show Fletching notifications?",
		section = SECTION_SKILLS
	)
	default boolean showFletchingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFishingNotifications",
		name = "Fishing",
		description = "Should we show Fishing notifications?",
		section = SECTION_SKILLS
	)
	default boolean showFishingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFiremakingNotifications",
		name = "Firemaking",
		description = "Should we show Firemaking notifications?",
		section = SECTION_SKILLS
	)
	default boolean showFiremakingNotifications()
	{
		return true;
	}


	@ConfigItem(
		keyName = "showCraftingNotifications",
		name = "Crafting",
		description = "Should we show Crafting notifications?",
		section = SECTION_SKILLS
	)
	default boolean showCraftingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSmithingNotifications",
		name = "Smithing",
		description = "Should we show Smithing notifications?",
		section = SECTION_SKILLS
	)
	default boolean showSmithingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showMiningNotifications",
		name = "Mining",
		description = "Should we show Mining notifications?",
		section = SECTION_SKILLS
	)
	default boolean showMiningNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showHerbloreNotifications",
		name = "Herblore",
		description = "Should we show Herblore notifications?",
		section = SECTION_SKILLS
	)
	default boolean showHerbloreNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showAgilityNotifications",
		name = "Agility",
		description = "Should we show Agility notifications?",
		section = SECTION_SKILLS
	)
	default boolean showAgilityNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showThievingNotifications",
		name = "Thieving",
		description = "Should we show Thieving notifications?",
		section = SECTION_SKILLS
	)
	default boolean showThievingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSlayerNotifications",
		name = "Slayer",
		description = "Should we show Slayer notifications?",
		section = SECTION_SKILLS
	)
	default boolean showSlayerNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFarmingNotifications",
		name = "Farming",
		description = "Should we show Farming notifications?",
		section = SECTION_SKILLS
	)
	default boolean showFarmingNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRunecraftNotifications",
		name = "Runecraft",
		description = "Should we show Runecraft notifications?",
		section = SECTION_SKILLS
	)
	default boolean showRunecraftNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showHunterNotifications",
		name = "Hunter",
		description = "Should we show Hunter notifications?",
		section = SECTION_SKILLS
	)
	default boolean showHunterNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showConstructionNotifications",
		name = "Construction",
		description = "Should we show Construction notifications?",
		section = SECTION_SKILLS
	)
	default boolean showConstructionNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSailingNotifications",
		name = "Sailing",
		description = "Should we show Sailing notifications?",
		section = SECTION_SKILLS
	)
	default boolean showSailingNotifications()
	{
		return true;
	}
}
