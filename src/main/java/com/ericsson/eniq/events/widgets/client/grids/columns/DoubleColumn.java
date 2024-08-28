package com.ericsson.eniq.events.widgets.client.grids.columns;

import com.ericsson.eniq.events.widgets.client.grids.GWTGrid;
import com.ericsson.eniq.events.widgets.client.grids.data.DataItem;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.TextColumn;

/**
 * 
 * @author ekurshi
 *
 * @param <T>
 */
public class DoubleColumn<T extends DataItem> extends TextColumn<T> {
    public static String DEFAULT_DOUBLE_FORMAT = "###.##";

    private final String columnID;

    private final NumberFormat formatter;

    public DoubleColumn(final ColumnInfo columnInfo) {
        super();
        this.columnID = columnInfo.getColumnId();
        final String format = columnInfo.getFormat();
        this.formatter = NumberFormat.getFormat((format == null || format.isEmpty()) ? DEFAULT_DOUBLE_FORMAT : format);
    }

    @Override
    public String getValue(final T record) {
        try {
            final Double columnValue = record.getColumnValue(columnID);
            return String.valueOf(formatter.format(columnValue));
        } catch (final Exception e) {
            return GWTGrid.NULL_OBJECT;
        }
    }
}
