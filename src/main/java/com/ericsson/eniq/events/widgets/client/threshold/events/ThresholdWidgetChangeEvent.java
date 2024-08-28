/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.threshold.events;

import com.ericsson.eniq.events.widgets.client.threshold.dataType.IThreshold;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author eeikbe
 * @since 10/2012
 */
public class ThresholdWidgetChangeEvent extends GwtEvent<IThresholdWidgetChange>{

    private IThreshold threshold;

    public final static Type<IThresholdWidgetChange> TYPE = new Type<IThresholdWidgetChange>();

    public ThresholdWidgetChangeEvent(final IThreshold threshold){
        this.threshold = threshold;
    }
    
    @Override
    public Type<IThresholdWidgetChange> getAssociatedType() {
        return TYPE;
    }

    /**
     * Should only be called by {@link com.google.gwt.event.shared.HandlerManager}. In other words, do not use
     * or call.
     *
     * @param handler handler
     */
    @Override
    protected void dispatch(IThresholdWidgetChange handler) {
        handler.handleThresholdWidgetChange(this.threshold);
    }
}