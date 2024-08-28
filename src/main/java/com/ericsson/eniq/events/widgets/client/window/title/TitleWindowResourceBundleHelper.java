package com.ericsson.eniq.events.widgets.client.window.title;

import com.google.gwt.core.client.GWT;

public class TitleWindowResourceBundleHelper {

    private static final TitleWindowResourceBundle bundle;

    static {
        bundle = GWT.create(TitleWindowResourceBundle.class);
        bundle.style().ensureInjected();
    }

    public static TitleWindowResourceBundle getBundle() {
        return bundle;
    }

}
