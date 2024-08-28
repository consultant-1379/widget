package com.ericsson.eniq.events.widgets.client.grids.headers;

import com.ericsson.eniq.events.widgets.client.grids.columns.ColumnInfo;
import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public interface SortClearEventHandler extends EventHandler {
    void onSortClear(ColumnInfo columnInfo);
}