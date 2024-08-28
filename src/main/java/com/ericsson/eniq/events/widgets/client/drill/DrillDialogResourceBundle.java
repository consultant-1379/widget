package com.ericsson.eniq.events.widgets.client.drill;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
public interface DrillDialogResourceBundle extends ClientBundle {
    @Source("css/dialogBox.css")
    MessageDialogStyle css();
    
    interface MessageDialogStyle extends CssResource{

        String dialogBox();

        String chartByLink();
    }
}
