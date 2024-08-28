package com.ericsson.eniq.events.widgets.client.scroll.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for {@link CustomScrollEvent} events.
 *
 * @author ealeerm - Alexey Ermykin
 * @author ekurshi
 * @since 04 2012
 */
public interface CustomScrollHandler extends EventHandler {

    /**
     * Fired when the browser window is scrolled.
     *
     * @param event the event
     */
    void onScroll(CustomScrollEvent event);

}
