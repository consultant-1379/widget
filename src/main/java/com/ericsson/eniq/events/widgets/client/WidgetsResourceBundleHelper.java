package com.ericsson.eniq.events.widgets.client;

import com.google.gwt.core.client.GWT;

public class WidgetsResourceBundleHelper {

    private static final WidgetsResourceBundle bundle;

    static {
        bundle = GWT.create(WidgetsResourceBundle.class);
    }

    public static WidgetsResourceBundle getBundle() {
        return bundle;
    }
}
