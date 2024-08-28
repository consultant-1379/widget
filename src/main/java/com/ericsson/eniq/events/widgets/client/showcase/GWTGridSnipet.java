package com.ericsson.eniq.events.widgets.client.showcase;

import java.util.List;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.grids.GWTGrid;
import com.ericsson.eniq.events.widgets.client.grids.columns.ColumnInfo;
import com.ericsson.eniq.events.widgets.client.grids.data.DataItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:11
 * To change this template use File | Settings | File Templates.
 */
public class GWTGridSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    private static final int ROW_COUNT = 100;

    public FlowPanel createWidget() {
        final GWTGrid<DataItem> table = new GWTGrid<DataItem>();
        table.setPageSize(ROW_COUNT);
        table.setTableLayoutFixed(true);
        // Push the data into the widget.
        table.setDataList(GridDataHelper.generateData(ROW_COUNT));
        final List<ColumnInfo> columnInfos = GridDataHelper.getColumnInfos();
        for (final ColumnInfo columnInfo : columnInfos) {
            table.addColumn(columnInfo);
        }
        table.setTableWidth(700);
        table.setTableHeight(300);
        final String source = wrapCode(sourceBundle.gwtGrid().getText());
        return wrapWidgetAndSource(table, source);
    }
}
