package com.ericsson.eniq.events.widgets.client.threshold.events;

import com.ericsson.eniq.events.widgets.client.threshold.dataType.IThreshold;
import com.google.gwt.event.shared.EventHandler;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
public interface IThresholdWidgetChange extends EventHandler {
    void handleThresholdWidgetChange(final IThreshold threshold);
}
