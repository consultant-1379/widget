package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import java.util.Arrays;
import java.util.List;

import com.ericsson.eniq.events.widgets.client.grids.filter.AbstractFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.EventBus;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public abstract class NumberFilterPanel<C extends AbstractFilter<T>, T> extends FilterPanel<C, T> {

    private static final int ICON_WIDTH = 20;

    private FilterPopup filterPopup;

    protected FilterType lastSelectedType;

    private FlowPanel filterIcon;

    public NumberFilterPanel(final IFilterUpdate<C, T> filterUpdateHandler, final EventBus eventBus) {
        super(filterUpdateHandler, eventBus);
    }

    @Override
    protected void init() {
        final List<FilterType> filterTypes = Arrays.asList(FilterType.values());
        filterPopup = new FilterPopup(filterTypes, getResourceBundle().style().filterCell());
        final SingleSelectionModel<FilterType> selectionModel = new SingleSelectionModel<FilterType>();
        selectionModel.addSelectionChangeHandler(new Handler() {

            @Override
            public void onSelectionChange(final SelectionChangeEvent event) {
                final FilterType selectedType = selectionModel.getSelectedObject();
                selectFilterType(selectedType);
                filterPopup.hide();
                
                if(selectedType == FilterType.EQUAL){
                   disableCog();
                }else{
                    enableCog();
                }
            }
        });
        filterPopup.addSelectionModel(selectionModel);
        filterPopup.setStyleName(getResourceBundle().style().filterPopUp());
        filterIcon = new FlowPanel();
        filterIcon.sinkEvents(Event.ONMOUSEOVER);
        filterIcon.addHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(final MouseOverEvent event) {
                showFilterPopup();
            }
        }, MouseOverEvent.getType());

        filterPopup.sinkEvents(Event.ONMOUSEOUT);
        filterPopup.addHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(final MouseOutEvent event) {
                filterPopup.hide();
            }
        }, MouseOutEvent.getType());
        filterIcon.setStyleName(getResourceBundle().style().filterIcon());
        this.lastSelectedType = filterTypes.get(0);
        selectFilterType(lastSelectedType);
        filterContainer.add(filterIcon);
        super.init();
    }

    private void showFilterPopup() {
        if (filterPopup.isAttached()) {
            return;
        }
        filterPopup.bringInFront();
        final int absoluteLeft = filterIcon.getAbsoluteLeft() - 9;
        final int absoluteTop = filterIcon.getAbsoluteTop() - 11;
        filterPopup.setPopupPosition(absoluteLeft, absoluteTop);
        filterPopup.show();
    }

    private void selectFilterType(final FilterType filterType) {
        filterIcon.removeStyleName(lastSelectedType.getStyle());
        filterIcon.addStyleName(filterType.getStyle());
        this.lastSelectedType = filterType;
    }

    @Override
    public SafeHtml getFilterHTML(final String filter, final FilterType filterType) {
        return getImageTemplate(filterType, filter);
    }

    @Override
    protected void setFilterAppliedButtonVisible(final boolean visible) {
        filterIcon.setVisible(!visible);
        super.setFilterAppliedButtonVisible(visible);
    }

    @Override
    protected void onFilterAppliedClick() {
        onConfigurationClick();
    }

    @Override
    public void setWidth(final int width) {
        final int boxWidth = Math.max(0, width - CONFIG_BUTTON_WIDTH - BORDER_WIDTH - ICON_WIDTH);
        getFilterBox().setWidth(boxWidth + "px");
        filterAppliedButton.setWidth(width - CONFIG_BUTTON_WIDTH - BORDER_WIDTH);
    }

}
