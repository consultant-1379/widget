package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import java.util.List;

import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * 
 * @author eorstap
 * @since 2013
 *
 */
public class FilterDetailPopup {
    private static FilterDetailPopupUiBinder uiBinder = GWT.create(FilterDetailPopupUiBinder.class);

    @UiField
    static FilterResourceBundle resourceBundle = GWT.create(FilterResourceBundle.class);

    interface FilterDetailPopupUiBinder extends UiBinder<Widget, FilterDetailPopup> {
    }

    @UiField
    Label headerText;

    @UiField
    FlowPanel filtersContainer;

    @UiField
    PopupPanel detailPopUp;

    public FilterDetailPopup() {
        uiBinder.createAndBindUi(this);
        resourceBundle.style().ensureInjected();
        detailPopUp.setAutoHideEnabled(true);
    }

    public void configure(final String header, final List<String> filters) {
        filtersContainer.clear();
        headerText.setText(header);
        headerText.setWordWrap(false);
        for (final String filter : filters) {
            filtersContainer.add(new Label(filter));
        }
    }

    public static FilterDetailPopup getSharedInstance() {
        return new FilterDetailPopup();
    }

    public void bringToFront() {
        detailPopUp.getElement().getStyle().setZIndex(ZIndexHelper.getHighestZIndex());
    }

    public void hide() {
        detailPopUp.hide();
    }

    public boolean isShowing() {
        return detailPopUp.isShowing();
    }

    public void setPopupPosition(final int mouseXPosition, final int mouseYPosition) {
        detailPopUp.setPopupPosition(mouseXPosition, mouseYPosition);
    }

    public void show() {
        detailPopUp.show();
    }

    /**
     * @return
     */
    public int getClientWidth() {
        return detailPopUp.getElement().getClientWidth();
    }

    /**
     * @return
     */
    public int getClientHeight() {
        return detailPopUp.getElement().getClientHeight();
    }
}
