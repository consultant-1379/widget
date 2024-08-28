package com.ericsson.eniq.events.widgets.client.window.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 *
 */
public class BoundaryUpdatedEvent extends GwtEvent<BoundaryUpdatedEventHandler> {

    public static final Type<BoundaryUpdatedEventHandler> TYPE = new Type<BoundaryUpdatedEventHandler>();

    private final String componentId;

    /**
     * That event should fire from Common EventBus(i.e same event bus which is being used to register the event)
     * @param componentId
     */

    public BoundaryUpdatedEvent(final String componentId) {
        this.componentId = componentId;
    }

    @Override
    public Type<BoundaryUpdatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final BoundaryUpdatedEventHandler handler) {
        handler.onBoundaryUpdated(componentId);
    }
}