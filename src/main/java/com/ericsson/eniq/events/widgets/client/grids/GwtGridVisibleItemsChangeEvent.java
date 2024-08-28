package com.ericsson.eniq.events.widgets.client.grids;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author eorstap
 * @since 2013
 *
 */
public class GwtGridVisibleItemsChangeEvent extends GwtEvent<GwtGridVisibleItemsChangeEventHandler>{

    public static final Type<GwtGridVisibleItemsChangeEventHandler> TYPE = new Type<GwtGridVisibleItemsChangeEventHandler>();
    
    @Override
    protected void dispatch(GwtGridVisibleItemsChangeEventHandler handler) {
        handler.onVisibleItemsChange(this);
    }

    @Override
    public Type<GwtGridVisibleItemsChangeEventHandler> getAssociatedType() {
        return TYPE;
    }
}
