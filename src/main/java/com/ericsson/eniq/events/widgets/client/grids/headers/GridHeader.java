package com.ericsson.eniq.events.widgets.client.grids.headers;

import java.util.Comparator;

import com.ericsson.eniq.events.widgets.client.grids.GWTGrid;
import com.ericsson.eniq.events.widgets.client.grids.columns.ColumnInfo;
import com.ericsson.eniq.events.widgets.client.grids.data.DataItem;
import com.ericsson.eniq.events.widgets.client.grids.filter.AbstractFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.components.FilterPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 * @param <C>
 * @param <T>
 * @param <D>
 */
public class GridHeader<C extends AbstractFilter<T>, T, D extends DataItem> extends Composite {

    public static final int HEADER_HEIGHT = 44;

    interface GridHeaderUiBinder extends UiBinder<Widget, GridHeader> {

    }

    private final GridHeaderUiBinder INSTANCE = GWT.create(GridHeaderUiBinder.class);

    private final GridHeaderResourceBundle resourceBundle = GWT.create(GridHeaderResourceBundle.class);

    private EventBus eventBus;

    @UiField
    Label header;

    @UiField
    SimplePanel filterContainer;

    private final ColumnInfo columnInfo;

    private boolean sortAsc;

    private FilterPanel<C, T> filterPanel;

    private ISortClickHandler<D> clickHandler;

    public GridHeader(final ColumnInfo columnInfo, final EventBus eventBus) {
        this.eventBus = eventBus;
        initWidget(INSTANCE.createAndBindUi(this));
        resourceBundle.style().ensureInjected();
        this.columnInfo = columnInfo;
        header.setText(columnInfo.getHeader());
        header.addStyleName(resourceBundle.style().sortImage());
        setTooltip(columnInfo.getTooltip());
        eventBus.addHandler(SortClearEvent.TYPE, new SortClearEventHandler() {

            @Override
            public void onSortClear(final ColumnInfo columnInfo) {
                if (columnInfo.isSortingEnabled()
                        && !columnInfo.getColumnId().equals(GridHeader.this.columnInfo.getColumnId())) {
                    clearSorting();
                }
            }
        });

    }

    public GridHeader(final ColumnInfo columnInfo, final FilterPanel<C, T> filterPanel, final EventBus eventBus) {
        this(columnInfo, eventBus);
//        if (columnInfo.isFilterEnabled()) {
            this.filterPanel = filterPanel;
            filterContainer.setVisible(columnInfo.isFilterEnabled());
            filterContainer.add(filterPanel);
//        }
    }

    public void addScrollBarWidth() {
        setWidth(columnInfo.getWidth() + GWTGrid.SCROLLBAR_WIDTH);
    }

    public void addSortHandler(final ISortClickHandler<D> clickHandler) {
        this.clickHandler = clickHandler;
    }

    private void applySorting() {
        if (!columnInfo.isSortingEnabled()) {
            return;
        }
        eventBus.fireEvent(new SortClearEvent(columnInfo));
        if (!sortAsc) {
            sortAsc = true;
            header.removeStyleName(resourceBundle.style().sortImageDesc());
            header.addStyleName(resourceBundle.style().sortImageAsc());
        } else {
            sortAsc = false;
            header.removeStyleName(resourceBundle.style().sortImageAsc());
            header.addStyleName(resourceBundle.style().sortImageDesc());
        }
        sortColumn();
    }

    public void clearSorting() {
        if (sortAsc) {
            sortAsc = false;
            header.removeStyleName(resourceBundle.style().sortImageAsc());
        } else {
            header.removeStyleName(resourceBundle.style().sortImageDesc());
        }
    }

    public ColumnInfo getColumnInfo() {
        return columnInfo;
    }

    @UiHandler("header")
    void onHeaderClick(final ClickEvent event) {
        applySorting();
    }

    public void removeScrollBarWidth() {
        setWidth(columnInfo.getWidth());
    }

    public void setWidth(final int width) {
        super.setWidth(width + "px");
        header.setWidth((width - 2) + "px");
        if (filterPanel != null) {
            filterPanel.setWidth(width);
        }
    }

    public void setTooltip(final String tooltip) {
        header.setTitle(tooltip);
    }

    public void sortColumn() {
        final SortType sortType = sortAsc ? SortType.ASCENDING : SortType.DESCENDING;
        final String columnId = columnInfo.getColumnId();
        final Comparator<T> comparator = filterPanel.getComparator();
        clickHandler.onSortClick(columnInfo, sortType, new Comparator<D>() {

            @Override
            public int compare(final D o1, final D o2) {
                final Object val1 = o1.getColumnValue(columnId);
                final Object val2 = o2.getColumnValue(columnId);
                if (val1 == null || String.valueOf(val1).equals(GWTGrid.NULL_OBJECT)) {
                    return -1;
                }
                final T columnValue1 = o1.<T> getColumnValue(columnId);
                if (val2 == null || String.valueOf(val2).equals(GWTGrid.NULL_OBJECT)) {
                    return 1;
                }
                final T columnValue2 = o2.<T> getColumnValue(columnId);

                return comparator.compare(columnValue1, columnValue2);
            }

        });
    }
}
