package com.ericsson.eniq.events.widgets.client.togglebuttons;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
public interface ToggleButtonMiniOnOffResourceBundle extends ClientBundle {
    @Source("ToggleButtonMiniOnOff.css")
    ToggleButtonMiniStyle css();
    
    interface ToggleButtonMiniStyle extends CssResource{

        String toggleMini();

        String toggleMiniOnDisabled();

        String toggleMiniOffDisabled();

        String toggleMiniOn();

        String toggleMiniOff();

    }
}
