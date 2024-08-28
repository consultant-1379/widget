/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class ConfigurationButton extends Button {

    private static final CogButtonResourceBundle resources;

    static {
        resources = GWT.create(CogButtonResourceBundle.class);
        resources.style().ensureInjected();
    }

    public ConfigurationButton() {
    }

    @Override
    public void setText(final String text) {
        setHTML("<div class=" + resources.style().configurationImage() + "></div><div class="
                + resources.style().configurationText() + ">" + text + "</div>");
    }

    @Override
    public void setEnabled(final boolean enabled) {
        if (enabled) {
            removeStyleName(resources.style().configurationDisabled());
        } else {
            addStyleName(resources.style().configurationDisabled());
        }
        super.setEnabled(enabled);
    }
}
