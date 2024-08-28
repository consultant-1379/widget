package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import java.util.List;

import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Resources;
import com.google.gwt.user.cellview.client.CellList.Style;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.SelectionModel;

/**
 * 
 * @author eorstap
 * @since 2013
 *
 */
public class FilterPopup extends PopupPanel {
    private final CellList<FilterType> popupFilterList;

    private final CellListResource resource = GWT.create(CellListResource.class);

    public interface CellListResource extends Resources {
        @Override
        @Source("CellList.css")
        Style cellListStyle();
    }

    public FilterPopup(final List<FilterType> images, final String cellStyle) {
        resource.cellListStyle().ensureInjected();
        setAutoHideEnabled(true);
        popupFilterList = new CellList<FilterType>(new CustomImageCell(cellStyle), resource);
        this.add(popupFilterList);
        popupFilterList.setRowCount(images.size(), true);
        popupFilterList.setRowData(images);
    }

    public void addSelectionModel(final SelectionModel<? super FilterType> selectionModel) {
        popupFilterList.setSelectionModel(selectionModel);
    }

    public void bringInFront() {
        getElement().getStyle().setZIndex(ZIndexHelper.getHighestZIndex());
    }

}
