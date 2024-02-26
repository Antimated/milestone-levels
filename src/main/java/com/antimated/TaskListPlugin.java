package com.antimated;

import com.google.inject.Provides;
import java.util.LinkedList;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.WidgetNode;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetModalMode;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Example"
)
public class TaskListPlugin extends Plugin
{
	private static final int NOTIFICATION_DISPLAY_INIT = 3343;
	private static final int COMPONENT_ID = ((303 << 16) | 2);
	private static final int INTERFACE_ID = 660;

	private final LinkedList<NotificationItem> notificationQueue = new LinkedList<>();

	private boolean isProcessingNotification = false;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private TaskListConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Subscribe
	public void onOverheadTextChanged(OverheadTextChanged e)
	{
		if (e.getActor().equals(client.getLocalPlayer()))
		{
			addNotification(new NotificationItem("Test", e.getOverheadText()));
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		// Check if an item is being processed, if not, process the queue
		if (!isProcessingNotification)
		{
			processNotificationsQueue();
		}
	}

	public void addNotification(NotificationItem item)
	{
		notificationQueue.offer(item);
		processNotificationsQueue();
	}

	private void processNotificationsQueue()
	{
		if (!notificationQueue.isEmpty() && !isProcessingNotification)
		{
			// Dequeue the item
			NotificationItem currentNotification = notificationQueue.poll();

			// Display notification
			displayNotification(currentNotification);
		}
	}

	private void displayNotification(NotificationItem currentNotification)
	{
		isProcessingNotification = true;

		WidgetNode widgetNode = client.openInterface(COMPONENT_ID, INTERFACE_ID, WidgetModalMode.MODAL_CLICKTHROUGH);

		// Runs a clientscript to set the initial title, text and color values of the notifications
		client.runScript(NOTIFICATION_DISPLAY_INIT, currentNotification.getTitle(), currentNotification.getText(), currentNotification.getColor());

		// Trigger invokeLater on the clientThread and check if the notification is fully closed before closing it
		clientThread.invokeLater(() -> {
			Widget w = client.getWidget(INTERFACE_ID, 1);

			if (w.getWidth() > 0)
			{
				return false;
			}

			// Close the interface
			client.closeInterface(widgetNode, true);

			isProcessingNotification = false;

			return true;
		});
	}

	@Provides
	TaskListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskListConfig.class);
	}


}
