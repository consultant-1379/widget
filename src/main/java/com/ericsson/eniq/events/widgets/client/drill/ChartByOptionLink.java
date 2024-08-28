/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.drill;

import com.ericsson.eniq.events.common.client.CommonConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

/**
 * @author egallou
 * @since 05/2013
 */

public class ChartByOptionLink extends Label {

    private String drillTypeId;

    private String linkUrl;

    private String displayName;

    static final DrillDialogResourceBundle resources;

    static {
        resources = GWT.create(DrillDialogResourceBundle.class);
        resources.css().ensureInjected();
    }

    public ChartByOptionLink(String drillTypeId, String text, String linkUrl) {
        super(text);
        this.displayName=text;
        this.drillTypeId = drillTypeId;
        this.linkUrl = linkUrl;

        this.getElement().setId(CommonConstants.SELENIUM_TAG+"DrillDialog_link");
        styleLabelAsLink();
    }

    public ChartByOptionLink(String drillTypeId, String text, String linkUrl, ClickHandler clickHandler) {
        this(drillTypeId, text, linkUrl);
        setClickHandler(clickHandler);
    }

    private void styleLabelAsLink() {
        setStyleName(resources.css().chartByLink());
    }

    public void setClickHandler(ClickHandler clickHandler) {
        addClickHandler(clickHandler);
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getDrillTypeId() {
        return drillTypeId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
