package com.ericsson.eniq.events.widgets.client.window.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public interface BoundaryUpdatedEventHandler extends EventHandler {
    void onBoundaryUpdated(String componentId);
}