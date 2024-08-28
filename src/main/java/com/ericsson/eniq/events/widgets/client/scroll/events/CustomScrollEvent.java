package com.ericsson.eniq.events.widgets.client.scroll.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Fired when the browser window is scrolled.
 *
 * @author ealeerm - Alexey Ermykin
 * @author ekurshi
 * @since 04 2012
 */
public class CustomScrollEvent extends GwtEvent<CustomScrollHandler> {

    static final Type<CustomScrollHandler> TYPE = new Type<CustomScrollHandler>();

    private final int contentPosition;

    private final int thumbPosition;

    private final EventType eventType;

    public enum EventType {
        PAGE_CHANGE, LINE_CHANGE, DRAGGED, SCROLL_COMPLETION
    }

    public static Type<CustomScrollHandler> getType() {
        return TYPE;
    }

    /**
     * Construct a new {@link CustomScrollEvent}.
     */
    public CustomScrollEvent(final int contentPosition, final int thumbPosition, final EventType eventType) {
        this.contentPosition = contentPosition;
        this.thumbPosition = thumbPosition;
        this.eventType = eventType;
    }

    public int getVerticalScrollPosition() {
        return contentPosition;
    }

    public int getThumbPosition() {
        return thumbPosition;
    }

    @Override
    public final Type<CustomScrollHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public void dispatch(final CustomScrollHandler handler) {
        handler.onScroll(this);
    }

    public EventType getEventType() {
        return eventType;
    }
}
