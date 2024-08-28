package com.ericsson.eniq.events.widgets.client.grids.columns;

import java.util.Date;

import com.ericsson.eniq.events.widgets.client.grids.GWTGrid;
import com.ericsson.eniq.events.widgets.client.grids.data.DataItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.TextColumn;

/**
 * 
 * @author ekurshi
 *
 * @param <T>
 */
public class DateColumn<T extends DataItem> extends TextColumn<T> {

    private final String columnID;

    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yy";

    private final DateTimeFormat formatter;

    public DateColumn(final ColumnInfo columnInfo) {
        super();
        this.columnID = columnInfo.getColumnId();
        final String format = columnInfo.getFormat();
        this.formatter = DateTimeFormat.getFormat((format == null || format.isEmpty()) ? DEFAULT_DATE_FORMAT : format);
    }

    @Override
    public String getValue(final T record) {
        try {
            final Date columnValue = record.getColumnValue(columnID);
            return formatter.format(columnValue);
        } catch (final Exception e) {
            return GWTGrid.NULL_OBJECT;
        }
    }
}
