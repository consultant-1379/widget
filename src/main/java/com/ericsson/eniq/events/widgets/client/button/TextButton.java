/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.widgets.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;

public class TextButton extends Button {

    private static final ButtonResourceBundle resources;

    static {
        resources = GWT.create(ButtonResourceBundle.class);
        resources.css().ensureInjected();
    }

    public TextButton(final String buttonText) {
        super(buttonText);
        addStyleName(resources.css().textButton());
    }

    public TextButton() {
        super();
        addStyleName(resources.css().textButton());
    }
}
