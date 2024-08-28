package com.ericsson.eniq.events.widgets.client.grids.headers;

import com.ericsson.eniq.events.widgets.client.grids.columns.ColumnInfo;
import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class SortClearEvent extends GwtEvent<SortClearEventHandler> {

    public static final Type<SortClearEventHandler> TYPE = new Type<SortClearEventHandler>();

    private final ColumnInfo columnInfo;

    public SortClearEvent(final ColumnInfo columnInfo) {
        this.columnInfo = columnInfo;
    }

    @Override
    protected void dispatch(final SortClearEventHandler handler) {
        handler.onSortClear(columnInfo);
    }

    @Override
    public Type<SortClearEventHandler> getAssociatedType() {
        return TYPE;
    }
}