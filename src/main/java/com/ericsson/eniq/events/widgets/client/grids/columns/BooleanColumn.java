package com.ericsson.eniq.events.widgets.client.grids.columns;

import com.ericsson.eniq.events.widgets.client.grids.GWTGrid;
import com.ericsson.eniq.events.widgets.client.grids.data.DataItem;
import com.google.gwt.user.cellview.client.TextColumn;

/**
 * 
 * @author ekurshi
 *
 * @param <T>
 */
public class BooleanColumn<T extends DataItem> extends TextColumn<T> {

    private final String columnID;

    public BooleanColumn(final ColumnInfo columnInfo) {
        super();
        this.columnID = columnInfo.getColumnId();
    }

    @Override
    public String getValue(final T record) {
        try {
            final Boolean columnValue = record.getColumnValue(columnID);
            return String.valueOf(columnValue);
        } catch (final Exception e) {
            return GWTGrid.NULL_OBJECT;
        }
    }
}
