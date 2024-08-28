/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiConstructor;

/**
 * @author eeikbe
 * @since 09/2012
 */
public class CogButton extends ImageButton {

    private static final CogButtonResourceBundle resources;

    static {
        resources = GWT.create(CogButtonResourceBundle.class);
        resources.style().ensureInjected();
    }

    public @UiConstructor
    CogButton() {
        super(resources.cogWheel());
        setStyleName(resources.style().cogWheel());
    }

}
