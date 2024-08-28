package com.ericsson.eniq.events.widgets.client.grids.columns;

import com.ericsson.eniq.events.widgets.client.grids.data.DataItem;
import com.google.gwt.user.cellview.client.TextColumn;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 * @param <T>
 */
public class DefaultColumn<T extends DataItem> extends TextColumn<T> {

    private final String columnID;

    public DefaultColumn(final ColumnInfo columnInfo) {
        super();
        this.columnID = columnInfo.getColumnId();
    }

    @Override
    public String getValue(final T record) {
        return record.getColumnValue(columnID);
    }
}
