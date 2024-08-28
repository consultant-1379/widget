/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WarningIndicator extends Composite {

    private static final Binder binder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, WarningIndicator> {
    }

    @UiField
    HTMLPanel messageWrapper;

    @UiField
    Label warningMessage;

    public WarningIndicator() {
        initWidget(binder.createAndBindUi(this));
    }

    public void setMessage(String message) {
        this.warningMessage.setText(message);
    }

    public String getMessage() {
        return warningMessage.getText();
    }

    public void show() {
        messageWrapper.getElement().getStyle().setDisplay(Display.BLOCK);
    }

    public void hide() {
        messageWrapper.getElement().getStyle().setDisplay(Display.NONE);
    }
}
