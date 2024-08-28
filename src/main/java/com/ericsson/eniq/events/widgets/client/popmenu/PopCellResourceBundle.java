package com.ericsson.eniq.events.widgets.client.popmenu;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
public interface PopCellResourceBundle  extends ClientBundle {
    @ClientBundle.Source("PopCell.css")
    PopCellStyle css();
    
    interface PopCellStyle extends CssResource {
        String content();
    }
}
