/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.drill;

import com.ericsson.eniq.events.common.client.CommonConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class DrillDialogPanel extends Composite {

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    FlowPanel contentPanel;
   
    @UiField
    Label popupHeading;

    public DrillDialogPanel() {
        initWidget(binder.createAndBindUi(this));
    }

    public void addDrillOption(final DrillCategoryType drillByOption, final ClickHandler handler) {
        HTML option = new HTML(drillByOption.getName());
        option.addClickHandler(handler);
        option.getElement().setId(CommonConstants.SELENIUM_TAG+"DrillDialog_link");
        contentPanel.add(option);
    }

    public void addChartByOption(final ChartByOptionLink chartByOption) {
        contentPanel.add(chartByOption);
    }

    public void setDialogTitle(String text){
        popupHeading.setText(text);
    }

    interface Binder extends UiBinder<Widget, DrillDialogPanel> {
    }
}
