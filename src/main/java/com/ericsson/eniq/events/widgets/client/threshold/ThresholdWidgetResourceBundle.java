package com.ericsson.eniq.events.widgets.client.threshold;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
public interface ThresholdWidgetResourceBundle extends ClientBundle {
    @Source("ThresholdWidget.css")
    ThresholdWidgetStyle css();

    interface ThresholdWidgetStyle extends CssResource {
        String thresholdWidget();

        String rangeLine();

        String title();

        String range();
    }
}
