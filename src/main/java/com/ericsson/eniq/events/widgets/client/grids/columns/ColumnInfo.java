package com.ericsson.eniq.events.widgets.client.grids.columns;

import java.util.Date;

import com.ericsson.eniq.events.widgets.client.common.DateTimeUtil;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class ColumnInfo {

    private final String columnId;

    private final ColumnType type;

    private boolean hidden;

    private int index;

    private int width = 150;

    private boolean filterEnabled = true;

    private final String header;

    private boolean sortingEnabled = true;

    private String tooltip;

    private String format = "";

    private Date fromDate;

    private Date toDate;

    public ColumnInfo(final String columnId, final ColumnType type, final String header) {
        this.columnId = columnId;
        this.type = type;
        this.header = header;
    }

    public ColumnInfo(final String columnId, final ColumnType type, final String header, final String tooltip,
            final int width) {
        this(columnId, type, header);
        this.tooltip = tooltip;
        this.width = width;
    }

    public ColumnInfo(final String columnId, final ColumnType type, final String header, final String tooltip,
            final int width, final String format, final boolean hidden) {
        this(columnId, type, header, tooltip, width);
        this.format = format;
        this.hidden = hidden;
    }

    public ColumnInfo(final String columnId, final ColumnType type, final String header, final String tooltip,
            final int width, final String format, final boolean hidden, final Date fromDate, final Date toDate) {
        this(columnId, type, header, tooltip, width, format, hidden);
        this.fromDate = DateTimeUtil.trimHourAndMinutes(fromDate);

        if (toDate != null) {
            this.toDate = toDate;
            this.toDate.setHours(23);
            this.toDate.setMinutes(45);
        }
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public void setToolTip(final String tooltip) {
        this.tooltip = tooltip;

    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public String getTooltip() {
        return tooltip;
    }

    public String getColumnId() {
        return columnId;
    }

    public String getHeader() {
        return header;
    }

    public int getIndex() {
        return index;
    }

    public ColumnType getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public boolean isFilterEnabled() {
        return filterEnabled;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isSortingEnabled() {
        return sortingEnabled;
    }

    public void setFilterEnabled(final boolean filterEnabled) {
        this.filterEnabled = filterEnabled;
    }

    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public void setSortingEnabled(final boolean sortEnabled) {
        this.sortingEnabled = sortEnabled;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public String getFormat() {
        return format;
    }

}
