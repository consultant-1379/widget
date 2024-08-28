package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import com.ericsson.eniq.events.widgets.client.grids.filter.AbstractFilter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author eorstap
 * @since 2013
 *
 */
public abstract class AbstractEditFilterView<C extends AbstractFilter<T>, T> extends Composite {

    protected static final int FILTER_CONTAINER_WIDTH = 170;

    interface AbstractEditFilterViewUiBinder extends UiBinder<Widget, AbstractEditFilterView> {
    }

    private static final AbstractEditFilterViewUiBinder uiBinder = GWT.create(AbstractEditFilterViewUiBinder.class);

    @UiField
    Image applyButton;

    @UiField
    FlowPanel filterIcon;

    @UiField
    CustomButton filterBtn;

    @UiField
    TextBox editFilterTextBox;

    @UiField
    Image addFilter;

    @UiField
    Image removeFilter;

    @UiField
    FlowPanel filterContainer;

    @UiField
    SymbolsResourceBundle resourceBundle;

    public AbstractEditFilterView() {
        initWidget(uiBinder.createAndBindUi(this));
        filterContainer.setWidth(FILTER_CONTAINER_WIDTH + "px");
        filterContainer.setHeight(20 + "px");
        filterBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                setFilterAppliedButtonVisible(false);
            }
        });
        applyButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                if (editFilterTextBox.getText().trim().length() > 0) {
                    setButtonText(editFilterTextBox.getText());
                    setFilterAppliedButtonVisible(true);
                }
            }
        });

    }

    public boolean isEmpty() {
        return editFilterTextBox.getText().trim().isEmpty();
    }

    public abstract void setButtonText(final String text);

    public HandlerRegistration addPlusClickHandler(final ClickHandler handler) {
        return addFilter.addClickHandler(handler);

    }

    public HandlerRegistration addMinusClickHandler(final ClickHandler handler) {
        return removeFilter.addClickHandler(handler);

    }

    protected void setFilterAppliedButtonVisible(final boolean visible) {
        filterBtn.setVisible(visible);
        editFilterTextBox.setVisible(!visible);
        applyButton.setVisible(!visible);
    }

    public TextBox getEditFilterTextBox() {
        return editFilterTextBox;
    }

    public void enableRemoveFilter(final boolean enabled) {
        if (enabled) {
            removeFilter.removeStyleName(resourceBundle.style().disableRemoveFilter());
            removeFilter.addStyleName(resourceBundle.style().enableRemoveFilter());
        } else {
            removeFilter.removeStyleName(resourceBundle.style().enableRemoveFilter());
            removeFilter.addStyleName(resourceBundle.style().disableRemoveFilter());
        }
    }

    public abstract C getFilter();
}
