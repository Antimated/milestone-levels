package com.antimated.notifications;

public class NotificationItem
{
	private final String title;
	private final String text;
	private final int color;

	// Constructor with color
	public NotificationItem(String title, String text, int color)
	{
		this.title = title;
		this.text = text;
		this.color = color;
	}

	// Constructor without color
	public NotificationItem(String title, String text)
	{
		this.title = title;
		this.text = text;
		this.color = -1; // Default or sentinel value for no color
	}

	public String getTitle()
	{
		return title;
	}

	public String getText()
	{
		return text;
	}

	public int getColor()
	{
		return color;
	}
}
