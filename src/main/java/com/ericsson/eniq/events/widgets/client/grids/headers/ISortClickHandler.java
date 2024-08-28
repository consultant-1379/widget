package com.ericsson.eniq.events.widgets.client.grids.headers;

import java.util.Comparator;

import com.ericsson.eniq.events.widgets.client.grids.columns.ColumnInfo;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 * @param <T>
 */
public interface ISortClickHandler<T> {
    void onSortClick(ColumnInfo columnInfo, SortType type, Comparator<T> comparator);
}
