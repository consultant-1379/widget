package com.ericsson.eniq.events.widgets.client.utilities;

import com.ericsson.eniq.events.widgets.client.utilities.ZIndexUpdatedEvent.ZIndexUpdatedEventHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Only Launch menu is going to listen that event because launch menu will always be on the top
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class ZIndexUpdatedEvent extends GwtEvent<ZIndexUpdatedEventHandler> {

    public static final Type<ZIndexUpdatedEventHandler> TYPE = new Type<ZIndexUpdatedEventHandler>();

    private final String componentId;

    private final int zIndex;

    /**
     * That event should fire from Common EventBus(i.e same event bus which is being used to register the event)
     * @param componentId
     * @param zIndex
     */

    public ZIndexUpdatedEvent(final String componentId, final int zIndex) {
        this.componentId = componentId;
        this.zIndex = zIndex;
    }

    @Override
    public Type<ZIndexUpdatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final ZIndexUpdatedEventHandler handler) {
        handler.onZIndexUpdated(componentId, zIndex);
    }

    public interface ZIndexUpdatedEventHandler extends EventHandler {
        void onZIndexUpdated(String componentId, int zIndex);
    }
}