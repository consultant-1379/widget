package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import java.util.Arrays;
import java.util.List;

import com.ericsson.eniq.events.widgets.client.grids.filter.AbstractFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.grids.filter.components.FilterPopup;
import com.ericsson.eniq.events.widgets.client.grids.filter.components.FilterResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public abstract class NumberEditFilterView<C extends AbstractFilter<T>, T> extends AbstractEditFilterView<C, T> {

    interface NumericTemplate extends SafeHtmlTemplates {
        @Template("<div class='{0}'></div><span title='{2}' class='{1}'>{2}</span>")
        SafeHtml div(String infoIconStyle, String labelStyle, String label);
    }

    private final FilterPopup filterPopup;

    private static final NumericTemplate NUMERIC_TEMPLATE = GWT.create(NumericTemplate.class);

    protected FilterType lastSelectedType;

    private final FilterResourceBundle resourceBundle = GWT.create(FilterResourceBundle.class);

    public NumberEditFilterView(final FilterType[] filterTypes, final FilterType selectedFilterType, final T filter) {
        super();
        resourceBundle.style().ensureInjected();
        final List<FilterType> filterTypeList = Arrays.asList(filterTypes);
        this.lastSelectedType = selectedFilterType;
        setFilterAppliedButtonVisible(filter != null);
        final int boxWidth = Math.max(0, FILTER_CONTAINER_WIDTH - 20 - 2 - 14);
        getEditFilterTextBox().setWidth(boxWidth + "px");
        filterBtn.setWidth(FILTER_CONTAINER_WIDTH);
        final String nonNullStr = filter == null ? "" : String.valueOf(filter);
        setButtonText(nonNullStr);
        getEditFilterTextBox().setText(nonNullStr);
        filterPopup = new FilterPopup(filterTypeList, resourceBundle.style().filterCell());
        final SingleSelectionModel<FilterType> selectionModel = new SingleSelectionModel<FilterType>();
        selectionModel.addSelectionChangeHandler(new Handler() {

            @Override
            public void onSelectionChange(final SelectionChangeEvent event) {
                final FilterType selectedType = selectionModel.getSelectedObject();
                selectFilterType(selectedType);
                filterPopup.hide();
            }
        });
        filterPopup.addSelectionModel(selectionModel);
        filterPopup.setStyleName(resourceBundle.style().filterPopUp());
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
        filterIcon.setStyleName(resourceBundle.style().filterIcon());
        selectFilterType(lastSelectedType);

    }

    @Override
    protected void setFilterAppliedButtonVisible(final boolean visible) {
        super.setFilterAppliedButtonVisible(visible);
        filterIcon.setVisible(!visible);
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
    public void setButtonText(final String text) {
        filterBtn.setHTML(NUMERIC_TEMPLATE.div(lastSelectedType.getStyle(), "labelStyle", text));
    }
    
    protected FilterResourceBundle getResourceBundle(){
        return resourceBundle;
    }
}