package com.ericsson.eniq.events.widgets.client.drill;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
public interface DrillBoxResourceBundle extends ClientBundle {
    @Source("css/drill_dialog.css")
    DialogBoxStyle css();

    interface DialogBoxStyle extends CssResource{

        String center();

        String bottom();

        String left();

        String right();

        String header();

        /*CSS for these is used, in a empty div, listed here to satisfy the interface*/
        String wrapperPanel();

        String contentPanel();

        String drillDialogIcon();

        String panelAlign();
    }
}
