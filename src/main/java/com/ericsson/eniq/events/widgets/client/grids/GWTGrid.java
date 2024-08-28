package com.ericsson.eniq.events.widgets.client.grids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ericsson.eniq.events.widgets.client.grids.columns.BooleanColumn;
import com.ericsson.eniq.events.widgets.client.grids.columns.ColumnInfo;
import com.ericsson.eniq.events.widgets.client.grids.columns.DateColumn;
import com.ericsson.eniq.events.widgets.client.grids.columns.DefaultColumn;
import com.ericsson.eniq.events.widgets.client.grids.columns.DoubleColumn;
import com.ericsson.eniq.events.widgets.client.grids.columns.IntegerColumn;
import com.ericsson.eniq.events.widgets.client.grids.data.DataItem;
import com.ericsson.eniq.events.widgets.client.grids.filter.AbstractFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.BooleanFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.DateFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.DoubleFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterConfigs;
import com.ericsson.eniq.events.widgets.client.grids.filter.IntegerFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.StringFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.components.BooleanFilterPanel;
import com.ericsson.eniq.events.widgets.client.grids.filter.components.DateFilterPanel;
import com.ericsson.eniq.events.widgets.client.grids.filter.components.DoubleFilterPanel;
import com.ericsson.eniq.events.widgets.client.grids.filter.components.IFilterUpdate;
import com.ericsson.eniq.events.widgets.client.grids.filter.components.IntegerFilterPanel;
import com.ericsson.eniq.events.widgets.client.grids.filter.components.StringFilterPanel;
import com.ericsson.eniq.events.widgets.client.grids.headers.GridHeader;
import com.ericsson.eniq.events.widgets.client.grids.headers.ISortClickHandler;
import com.ericsson.eniq.events.widgets.client.grids.headers.SortType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 * @param <D>
 */
public class GWTGrid<D extends DataItem> extends Composite implements HasData<D>, IResizable {
    interface GWTGridUiBinder extends UiBinder<Widget, GWTGrid> {

    }

    public static final String NULL_OBJECT = "-";

    public static final int SCROLLBAR_HEIGHT = 17;

    public static final int SCROLLBAR_WIDTH = 17;

    private final CellTable<D> cellTable;

    @UiField
    FlowPanel container;

    @UiField
    FlowPanel headersContainer;

    @UiField
    SimplePanel cellTableContainer;

    private final EventBus eventBus;

    private final ListDataProvider<D> dataProvider;

    private List<D> rawDataList;

    private final Map<String, FilterConfigs<?, ?>> filterConfigsMap;

    private final GWTGridUiBinder INSTANCE = GWT.create(GWTGridUiBinder.class);

    private final GWTGridResource gridResource = GWT.create(GWTGridResource.class);

    private GridHeader sortHeader;

    private final Map<String, Column> columnMap;

    private int fixedWidth = SCROLLBAR_WIDTH;

    private boolean visibleItemsChangeHandlerAdded = false;

    public GWTGrid() {
        initWidget(INSTANCE.createAndBindUi(this));
        filterConfigsMap = new HashMap<String, FilterConfigs<?, ?>>();
        columnMap = new HashMap<String, Column>();
        dataProvider = new ListDataProvider<D>();
        eventBus = new SimpleEventBus();
        cellTable = new CellTable<D>(10, gridResource);
        dataProvider.addDataDisplay(cellTable);
        gridResource.gridBackgroundStyle().ensureInjected();
        cellTableContainer.add(cellTable);
        cellTableContainer.addStyleName(gridResource.gridBackgroundStyle().gridStripeBackground());
    }

    @Override
    public HandlerRegistration addCellPreviewHandler(
            final com.google.gwt.view.client.CellPreviewEvent.Handler<D> handler) {
        return cellTable.addCellPreviewHandler(handler);
    }

    public void addColumn(final ColumnInfo columnInfo) {
        if (columnInfo.isHidden()) {
            return;
        }
        final int width = columnInfo.getWidth();
        fixedWidth = fixedWidth + width;
        final Column col = createColumn(columnInfo);
        cellTable.addColumn(col);
        columnMap.put(columnInfo.getColumnId(), col);
        cellTable.setColumnWidth(col, width, Unit.PX);
        switch (columnInfo.getType()) {
        case STRING:
            final StringFilterPanel stringFilterPanel = new StringFilterPanel(
                    new IFilterUpdate<StringFilter, String>() {

                        @Override
                        public void onFilterUpdated(final FilterConfigs<StringFilter, String> filterConfigs) {
                            filterConfigsMap.put(columnInfo.getColumnId(), filterConfigs);
                            applyAllFilters();
                        }
                    }, eventBus);
            final GridHeader<StringFilter, String, D> stringHeader = new GridHeader<StringFilter, String, D>(
                    columnInfo, stringFilterPanel, eventBus);
            setSortHandler(stringHeader, columnInfo);
            stringHeader.addScrollBarWidth();
            headersContainer.add(stringHeader);
            break;
        case DOUBLE:
            final DoubleFilterPanel doubleFilterPanel = new DoubleFilterPanel(
                    new IFilterUpdate<DoubleFilter, Double>() {

                        @Override
                        public void onFilterUpdated(final FilterConfigs<DoubleFilter, Double> filterConfigs) {
                            filterConfigsMap.put(columnInfo.getColumnId(), filterConfigs);
                            applyAllFilters();
                        }

                    }, eventBus);
            final GridHeader<DoubleFilter, Double, D> numberHeader = new GridHeader<DoubleFilter, Double, D>(
                    columnInfo, doubleFilterPanel, eventBus);
            setSortHandler(numberHeader, columnInfo);
            numberHeader.addScrollBarWidth();
            headersContainer.add(numberHeader);
            break;
        case INTEGER:
            final IntegerFilterPanel intergerFilterPanel = new IntegerFilterPanel(
                    new IFilterUpdate<IntegerFilter, Integer>() {

                        @Override
                        public void onFilterUpdated(final FilterConfigs<IntegerFilter, Integer> filterConfigs) {
                            filterConfigsMap.put(columnInfo.getColumnId(), filterConfigs);
                            applyAllFilters();
                        }

                    }, eventBus);
            final GridHeader<IntegerFilter, Integer, D> integerHeader = new GridHeader<IntegerFilter, Integer, D>(
                    columnInfo, intergerFilterPanel, eventBus);
            setSortHandler(integerHeader, columnInfo);
            integerHeader.addScrollBarWidth();
            headersContainer.add(integerHeader);
            break;
        case BOOLEAN:
            final BooleanFilterPanel booleanFilterPanel = new BooleanFilterPanel(
                    new IFilterUpdate<BooleanFilter, Boolean>() {

                        @Override
                        public void onFilterUpdated(final FilterConfigs<BooleanFilter, Boolean> filterConfigs) {
                            filterConfigsMap.put(columnInfo.getColumnId(), filterConfigs);
                            applyAllFilters();
                        }

                    }, eventBus);
            final GridHeader<BooleanFilter, Boolean, D> booleanHeader = new GridHeader<BooleanFilter, Boolean, D>(
                    columnInfo, booleanFilterPanel, eventBus);
            setSortHandler(booleanHeader, columnInfo);
            booleanHeader.addScrollBarWidth();
            headersContainer.add(booleanHeader);
            break;
        case DATE:
            final DateFilterPanel dateFilterPanel = new DateFilterPanel(new IFilterUpdate<DateFilter, Date>() {

                @Override
                public void onFilterUpdated(final FilterConfigs<DateFilter, Date> filterConfigs) {
                    filterConfigsMap.put(columnInfo.getColumnId(), filterConfigs);
                    applyAllFilters();
                }

            }, columnInfo.getFromDate(), columnInfo.getToDate(), eventBus);
            final GridHeader<DateFilter, Date, D> dateHeader = new GridHeader<DateFilter, Date, D>(columnInfo,
                    dateFilterPanel, eventBus);
            setSortHandler(dateHeader, columnInfo);
            dateHeader.addScrollBarWidth();
            headersContainer.add(dateHeader);
            break;
        }
        setFixedWidth(fixedWidth);
        final int widgetCount = headersContainer.getWidgetCount();
        if (widgetCount > 1) {//last column
            getHeader(widgetCount - 2).removeScrollBarWidth();
        }
    }

    @Override
    public HandlerRegistration addRangeChangeHandler(final Handler handler) {
        return cellTable.addRangeChangeHandler(handler);
    }

    @Override
    public HandlerRegistration addRowCountChangeHandler(
            final com.google.gwt.view.client.RowCountChangeEvent.Handler handler) {
        return cellTable.addRowCountChangeHandler(handler);
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<D> handler) {
        return cellTable.addHandler(handler, ValueChangeEvent.getType());
    }

    public HandlerRegistration addVisibleItemsChangeHandler(final GwtGridVisibleItemsChangeEventHandler handler) {
        visibleItemsChangeHandlerAdded = true;
        return cellTable.addHandler(handler, GwtGridVisibleItemsChangeEvent.TYPE);
    }

    private void applyAllFilters() {
        final Set<String> keySet = filterConfigsMap.keySet();
        List<D> filteredList = rawDataList;
        for (final String columnId : keySet) {
            final FilterConfigs<?, ?> filterConfigs = filterConfigsMap.get(columnId);
            filteredList = applyFilter(filterConfigs, columnId, filteredList);
        }

        dataProvider.setList(filteredList);
        if (sortHeader != null) {
            sortHeader.sortColumn();
        }

        if (visibleItemsChangeHandlerAdded) {
            cellTable.fireEvent(new GwtGridVisibleItemsChangeEvent());
        }
    }

    private <C extends AbstractFilter<T>, T> List<D> applyFilter(final FilterConfigs<C, T> filterConfigs,
            final String columnId, final List<D> recordsList) {
        if (filterConfigs.isEmpty()) {
            return recordsList;
        }
        final List<D> newList = new ArrayList<D>();
        for (final D record : recordsList) {
            if (!filterConfigs.applyFilters(record.<T> getColumnValue(columnId))) {
                newList.add(record);
            }
        }
        return newList;
    }

    public void clearData() {
        rawDataList = new ArrayList<D>();
        dataProvider.getList().clear();
    }

    private Column createColumn(final ColumnInfo columnInfo) {
        switch (columnInfo.getType()) {
        case STRING:
            return new DefaultColumn<DataItem>(columnInfo);
        case BOOLEAN:
            return new BooleanColumn<DataItem>(columnInfo);
        case INTEGER:
            return new IntegerColumn<D>(columnInfo);
        case DATE:
            return new DateColumn<DataItem>(columnInfo);
        case DOUBLE:
            return new DoubleColumn<DataItem>(columnInfo);
        default:
            return null;
        }
    }

    private <T> GridHeader<? extends AbstractFilter<T>, T, D> getHeader(final int index) {
        return (GridHeader<? extends AbstractFilter<T>, T, D>) headersContainer.getWidget(index);
    }

    @Override
    public int getRowCount() {
        return cellTable.getRowCount();
    }

    @Override
    public SelectionModel<? super D> getSelectionModel() {
        return cellTable.getSelectionModel();
    }

    public void clear() {
        clearData();
        removeAllColumn();
        fixedWidth = SCROLLBAR_WIDTH;
        headersContainer.clear();
        sortHeader = null;
        columnMap.clear();
        filterConfigsMap.clear();
    }

    private void removeAllColumn() {
        final int columnCount = cellTable.getColumnCount();
        for (int i = columnCount - 1; i >= 0; i--) {
            cellTable.removeColumn(i);
        }
    }

    @Override
    public D getVisibleItem(final int indexOnPage) {
        return cellTable.getVisibleItem(indexOnPage);
    }

    @Override
    public int getVisibleItemCount() {
        return cellTable.getVisibleItemCount();
    }

    @Override
    public Iterable<D> getVisibleItems() {
        return cellTable.getVisibleItems();
    }

    @Override
    public Range getVisibleRange() {
        return cellTable.getVisibleRange();
    }

    @Override
    public boolean isRowCountExact() {
        return cellTable.isRowCountExact();
    }

    public void onColumnSort(final SortType type, final Comparator<D> comparator) {
        if (comparator == null) {
            return;
        }
        final List<D> list = dataProvider.getList();
        // Sort using the comparator.
        if (type == SortType.ASCENDING) {
            Collections.sort(list, comparator);
        } else {
            Collections.sort(list, new Comparator<D>() {
                @Override
                public int compare(final D o1, final D o2) {
                    return -comparator.compare(o1, o2);
                }
            });
        }
    }

    @Override
    public void onResize(final int width, final int height) {
        if (isAttached()) {
            setTableSize(width, height);
        }
    }

    public <T> void removeColumn(final ColumnInfo columnInfo) {
        final Column textColumn = columnMap.get(columnInfo.getColumnId());
        cellTable.removeColumn(textColumn);
        final int widgetCount = headersContainer.getWidgetCount();
        for (int i = 0; i < widgetCount; i++) {
            final GridHeader<? extends AbstractFilter<T>, T, D> header = getHeader(i);
            if (getHeader(widgetCount - 1).equals(header)) {//last column
                final GridHeader<? extends AbstractFilter<T>, T, D> secondLastHeader = getHeader(Math.max(0,
                        widgetCount - 2));
                secondLastHeader.addScrollBarWidth();
            }
            if (header.getColumnInfo().getColumnId().equals(columnInfo.getColumnId())) {
                filterConfigsMap.remove(columnInfo.getColumnId());
                applyAllFilters();
                headersContainer.remove(header);
                fixedWidth = fixedWidth - columnInfo.getWidth();
                setFixedWidth(fixedWidth);
                break;
            }
        }
    }

    public void setDataList(final List<D> dataList) {
        this.rawDataList = dataList;
        final int pageSize = dataList == null ? 0 : dataList.size();
        setPageSize(pageSize);
        dataProvider.setList(dataList);
    }

    private void setFixedWidth(final int width) {
        headersContainer.setWidth(width + "px");
        cellTableContainer.setWidth(width + "px");
    }

    public void setPageSize(final int size) {
        cellTable.setPageSize(size);

    }

    @Override
    public void setRowCount(final int count) {
        cellTable.setRowCount(count);
    }

    @Override
    public void setRowCount(final int count, final boolean isExact) {
        cellTable.setRowCount(count, isExact);
    }

    @Override
    public void setRowData(final int start, final List<? extends D> values) {
        cellTable.setRowData(start, values);
    }

    @Override
    public void setSelectionModel(final SelectionModel<? super D> selectionModel) {
        cellTable.setSelectionModel(selectionModel);
    }

    private <C extends AbstractFilter<T>, T> void setSortHandler(final GridHeader<C, T, D> header,
            final ColumnInfo columnInfo) {
        if (!columnInfo.isSortingEnabled()) {
            return;
        }
        header.addSortHandler(new ISortClickHandler<D>() {

            @Override
            public void onSortClick(final ColumnInfo columnInfo, final SortType type, final Comparator<D> comparator) {
                sortHeader = header;
                onColumnSort(type, comparator);
            }
        });
    }

    public void setTableHeight(final int height) {
        cellTableContainer.setHeight(Math.max(0, height - GridHeader.HEADER_HEIGHT - SCROLLBAR_HEIGHT) + "px");
    }

    public void setTableLayoutFixed(final boolean isFixed) {
        cellTable.setTableLayoutFixed(isFixed);
    }

    public void setTableSize(final int width, final int height) {
        setTableHeight(height);
        setTableWidth(width);
    }

    public void setTableWidth(final int width) {
        setWidth(width + "px");
    }

    @Override
    public void setVisibleRange(final int start, final int length) {
        cellTable.setVisibleRange(start, length);
    }

    @Override
    public void setVisibleRange(final Range range) {
        cellTable.setVisibleRange(range);
    }

    @Override
    public void setVisibleRangeAndClearData(final Range range, final boolean forceRangeChangeEvent) {
        cellTable.setVisibleRangeAndClearData(range, forceRangeChangeEvent);
    }
    
    public HandlerRegistration addCellPreviewHadler(CellPreviewEvent.Handler<D> handler){
       return cellTable.addCellPreviewHandler(handler);
    }
    
    public void setId(String id){
        getElement().setId(id);
    }
}
