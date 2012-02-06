package com.gmi.gwaswebapp.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.EventBus;


public class DisplayNotificationEvent extends GwtEvent<DisplayNotificationEvent.DisplayNotificationHandler>  {

	  
	public interface DisplayNotificationHandler extends EventHandler {
		  void onDisplayNotifcation(DisplayNotificationEvent event);
	}
	
	public static final int LEVEL_MESSAGE = 0;
	public static final int LEVEL_WARNING = 1;
	public static final int LEVEL_ERROR = 2;
	public static final int LEVEL_SEVERE = 3;

	public static final int DURATION_PERMANENT = 0;
	public static final int DURATION_SHORT = 5000;
	public static final int DURATION_NORMAL = 10000;
	public static final int DURATION_LONG = 20000;

	private static final Type<DisplayNotificationHandler> TYPE = new Type<DisplayNotificationHandler>();

	public static Type<DisplayNotificationHandler> getType() {
		return TYPE;
	}

	/**
	 * Fires a new event to display short messages.
	 * 
	 * @param source The source of this event (See {@link HasEventBus}).
	 * @param message Any {@link Widget} containing the message to display. Pass {@code null} to clear displayed messages.
	 * @param dismissable {@code true} if the message should be dismissable 
	 *                    by the user (i.e. a "close" button should be available.)
	 * @param level The level of the message should be one of: {@code LEVEL_MESSAGE,
	 * LEVEL_WARNING, LEVEL_ERROR, LEVEL_SEVERE}.
	 * @param duration The duration of the message, in milliseconds. Pass 0 for a
	 * permanent message. Should preferably be one of: {@code DURATION_PERMANENT,
	 * DURATION_SHORT, DURATION_NORMAL, DURATION_LONG}.
	 */
	public static void fire(HasHandlers source, String caption,String message, boolean dismissable, int level, int duration) {
		source.fireEvent(new DisplayNotificationEvent(caption,message, dismissable, level, duration));
	}

	/**
	 * Fires a new event to display short messages. The message will be
	 * dismissable, it has a level of {@code LEVEL_MESSAGE} and a normal
	 * duration.
	 * 
	 * @param source The source of this event (See {@link HasEventBus}).
	 * @param message Any {@link Widget} containing the message to display.
	 */
	public static void fireMessage(HasHandlers source, String caption,String message) {
		fire(source, caption,message, true, LEVEL_MESSAGE, DURATION_NORMAL);
	}

	/**
	 * Fires a new event to clear all displayed short messages.
	 * 
	 * @param source The source of this event (See {@link HasEventBus}).
	 */
	public static void fireClearMessage(HasHandlers source) {
		//fire(source, null, false, LEVEL_MESSAGE, DURATION_PERMANENT);
	}

	/**
	 * Fires a new event to display short error. The message will be
	 * dismissable, it has a level of {@code LEVEL_ERROR} and a permanent
	 * duration.
	 * 
	 * @param source The source of this event (See {@link HasEventBus}).
	 * @param message Any {@link Widget} containing the message to display.
	 */
	public static void fireError(HasHandlers source, String caption, String message) {
		fire(source,caption, message, true, LEVEL_ERROR, DURATION_PERMANENT);
	}



	private final String caption;
	private final String message;
	private final boolean dismissable;
	private final int level;
	private final int duration;

	private boolean alreadyDisplayed;

	/**
	 * Creates a new event to display short messages.
	 * 
	 * @param message Any {@link Widget} containing the message to display.
	 * @param dismissable {@code true} if the message should be dismissable 
	 *                    by the user (i.e. a "close" button should be available.)
	 * @param level The level of the message must be one of: {@code LEVEL_MESSAGE,
	 * LEVEL_WARNING, LEVEL_ERROR, LEVEL_SEVERE}.
	 * @param duration The duration of the message, in milliseconds. Pass {@code DURATION_PERMANENT} for a
	 * permanent message. Should preferably be one of: {@code DURATION_PERMANENT,
	 * DURATION_SHORT, DURATION_NORMAL, DURATION_LONG}.
	 */
	public DisplayNotificationEvent(String caption, String message, boolean dismissable, int level, int duration) {
		this.caption = caption;
		this.message = message;
		this.dismissable = dismissable;
		this.level = level;
		this.duration = duration;
	}

	/**
	 * @return The {@link Widget} containing the message to display. Can be {@code null}, 
	 *         in which case displayed messages should be cleared.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Checks if the message is dismissable. A dismissable message should be
	 * displayed with a UI mechanism that lets the user close the message if
	 * desired. (i.e. a "close" button)
	 * 
	 * @return {@code true} if the message is dismissable, {@code false} otherwise.
	 */
	public boolean isDismissable() {
		return dismissable;
	}

	/**
	 * Checks the level of the message. The UI can be adjusted (i.e. the color changed)
	 * depending on this level. Should be one of: {@code LEVEL_MESSAGE,
	 * LEVEL_WARNING, LEVEL_ERROR, LEVEL_SEVERE}
	 * 
	 * @return The level of the message.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Access the desired duration of the message. The UI should leave the message visible
	 * at least for that amount of time. If the value is {@code DURATION_PERMANENT}, the
	 * message should be displayed forever, or until dismissed if available.
	 * 
	 * @return The duration, in milliseconds.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Checks if the message has already been displayed. If {@code true}, then it probably
	 * shouldn't be added again to another display.
	 * 
	 * @return {@code true} if the message has already been displayed, {@code false} otherwise.
	 */
	public boolean isAlreadyDisplayed() {
		return alreadyDisplayed;
	}

	/**
	 * Call this method whenever you display the message within a UI, so that no other UI displays it.
	 * Don't call it if you're simply logging the message.
	 * 
	 * @see #isAlreadyDisplayed()
	 */
	public void setAlreadyDisplayed() {
		// Null messages should clear every UI, so don't set the flag.
		if (message != null) {
			alreadyDisplayed = true;
		}
	}

	@Override
	protected void dispatch(DisplayNotificationHandler handler) {
		handler.onDisplayNotifcation(this);
	}

	@Override
	public Type<DisplayNotificationHandler> getAssociatedType() {
		return getType();
	}

	public String getCaption() {
		return caption;
	}

}

