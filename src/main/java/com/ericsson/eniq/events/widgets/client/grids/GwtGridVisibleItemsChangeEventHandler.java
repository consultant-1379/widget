package com.ericsson.eniq.events.widgets.client.grids;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author eorstap
 * @since 2013
 *
 */
public interface GwtGridVisibleItemsChangeEventHandler extends EventHandler{
    public void onVisibleItemsChange(GwtGridVisibleItemsChangeEvent event);
}
