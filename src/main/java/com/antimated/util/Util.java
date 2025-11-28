package com.antimated.util;

import java.awt.Color;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Skill;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.util.Text;

@Slf4j
public class Util
{
	/**
	 * Checks if a level is a valid real level (>= 1 and <= 99)
	 *
	 * @param level int
	 * @return boolean
	 */
	public static boolean isValidRealLevel(int level)
	{
		return level >= 1 && level <= Experience.MAX_REAL_LEVEL;
	}


	/**
	 * Checks if a number is a valid XP target (>= 1 and <= 200M)
	 *
	 * @param xp int
	 * @return boolean
	 */
	public static boolean isValidExperience(int xp)
	{
		return xp > 0 && xp <= Experience.MAX_SKILL_XP;
	}


	/**
	 * @param string String
	 * @return boolean
	 */
	public static boolean isInteger(String string)
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
	 * Gets the int value for a color.
	 *
	 * @param color color
	 * @return int
	 */
	public static int getIntValue(Color color)
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
	public static String replaceSkillAndLevel(String text, Skill skill, int level)
	{
		return Text.escapeJagex(text
			.replaceAll("\\$skill", skill.getName())
			.replaceAll("\\$level", Integer.toString(level)));
	}

	/**
	 * Replaces the words $skill and $xp from the text to the passed skill and level respectively
	 *
	 * @param text       String
	 * @param skill      Skill
	 * @param xp int
	 * @return String
	 */
	public static String replaceSkillAndExperience(String text, Skill skill, int xp)
	{
		return Text.escapeJagex(text
			.replaceAll("\\$skill", skill.getName())
			.replaceAll("\\$xp", QuantityFormatter.formatNumber(xp)));
	}

	public static boolean isStandardWorld(Client client)
	{
		return RuneScapeProfileType.getCurrent(client) == RuneScapeProfileType.STANDARD;
	}

	/**
	 * Is player currently within the provided map regions
	 */
	public static boolean isPlayerWithinMapRegion(Client client, Set<Integer> definedMapRegions)
	{
		// final int[] mapRegions = client.getTopLevelWorldView().getMapRegions();
		final int[] mapRegions = client.getLocalPlayer().getWorldView().getMapRegions();

		for (int region : mapRegions)
		{
			if (definedMapRegions.contains(region))
			{
				return true;
			}
		}

		return false;
	}
}
