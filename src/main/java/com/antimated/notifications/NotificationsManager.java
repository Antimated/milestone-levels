package com.antimated.notifications;

import java.util.LinkedList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.JagexColor;
import net.runelite.api.WidgetNode;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetModalMode;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.JagexColors;

@Slf4j
@Singleton
public class NotificationsManager
{
	private static final int SCRIPT_ID = 3343; // NOTIFICATION_DISPLAY_INIT

	private static final int COMPONENT_ID = ((303 << 16) | 2); // 303 group id, 2 child id

	private static final int INTERFACE_ID = 660;

	private final LinkedList<NotificationItem> notifications = new LinkedList<>();

	@Getter
	private boolean isProcessingNotification = false;

	@Getter
	private boolean canProcessNotifications = false;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (canProcessNotifications)
		{
			processNotification();
		}
	}

	public void startUp()
	{
		canProcessNotifications = true;
	}

	public void shutDown()
	{
		canProcessNotifications = false;
		notifications.clear();
	}

	public void addNotification(String title, String text)
	{
		addNotification(title, text, -1);
	}

	public void addNotification(String title, String text, int color)
	{
		NotificationItem notification = new NotificationItem(title, text, color);
		notifications.offer(notification);
	}

	/**
	 * Processes a notification
	 */
	private void processNotification()
	{
		// Only process notifications if the queue is not empty AND the queue is not processing any notifications.
		if (!notifications.isEmpty() && !isProcessingNotification)
		{
			// Get and remove the first element in the notifications queue.
			NotificationItem notification = notifications.poll();

			// Display notification
			displayNotification(notification);
		}
	}

	/**
	 * Display a notification and close it afterwards.
	 * @param notification NotificationItem
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 */
	private void displayNotification(NotificationItem notification) throws IllegalStateException, IllegalArgumentException
	{
		isProcessingNotification = true;

		WidgetNode notificationNode = client.openInterface(COMPONENT_ID, INTERFACE_ID, WidgetModalMode.MODAL_CLICKTHROUGH);
		Widget notificationWidget = client.getWidget(INTERFACE_ID, 1);

		// Runs a client script to set the initial title, text and color values of the notifications
		client.runScript(SCRIPT_ID, notification.getTitle(), notification.getText(), notification.getColor());

		// Only remove notification when widget is fully closed.
		clientThread.invokeLater(() -> {
			assert notificationWidget != null;

			if (notificationWidget.getWidth() > 0)
			{
				return false;
			}

			// Close the interface
			client.closeInterface(notificationNode, true);

			// We can now start processing notifications again.
			isProcessingNotification = false;

			// Invoke done
			return true;
		});
	}
}
