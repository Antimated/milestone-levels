package com.antimated;

import java.util.LinkedList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.WidgetNode;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetModalMode;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;

@Slf4j
@Singleton
public class NotificationManager
{
	private static final int SCRIPT_ID = 3343; // NOTIFICATION_DISPLAY_INIT

	private static final int COMPONENT_ID = ((303 << 16) | 2); // 303 group id, 2 child id

	private static final int INTERFACE_ID = 660;

	private final LinkedList<NotificationItem> notifications = new LinkedList<>();
	@Getter
	private boolean isProcessingNotification = false;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Subscribe
	public void onGameTick(GameTick event)
	{
		processNotifications();
	}

	public void addNotification(String title, String text)
	{
		addNotification(title, text, -1);
	}

	public void addNotification(String title, String text, int color)
	{
		NotificationItem notification = new NotificationItem(title, text, color);
		notifications.offer(notification);
		processNotifications();
	}

	private void processNotifications()
	{
		if (!notifications.isEmpty() && !isProcessingNotification)
		{
			// Dequeue the item
			NotificationItem notification = notifications.poll();

			// Display notification
			displayNotification(notification);
		}
	}

	private void displayNotification(NotificationItem notification)
	{
		isProcessingNotification = true;

		WidgetNode notificationNode = client.openInterface(COMPONENT_ID, INTERFACE_ID, WidgetModalMode.MODAL_CLICKTHROUGH);

		// Runs a client script to set the initial title, text and color values of the notifications
		client.runScript(SCRIPT_ID, notification.getTitle(), notification.getText(), notification.getColor());

		// Trigger invokeLater on the clientThread and check if the notification is fully closed before closing it
		clientThread.invokeLater(() -> {
			Widget notificationWidget = client.getWidget(INTERFACE_ID, 1);

			if (notificationWidget.getWidth() > 0)
			{
				return false;
			}

			// Close the interface
			client.closeInterface(notificationNode, true);

			isProcessingNotification = false;

			return true;
		});
	}
}
