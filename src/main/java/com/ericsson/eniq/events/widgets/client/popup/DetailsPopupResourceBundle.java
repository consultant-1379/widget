package com.ericsson.eniq.events.widgets.client.popup;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */


import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface DetailsPopupResourceBundle extends ClientBundle {

    @ClientBundle.Source("DetailsPopup.css")
    DetailsPopupStyle css();

    interface DetailsPopupStyle extends CssResource {
        String gwtPopupPanel();

        String header();

        String titleLabel();

        String iconHolder();

        String contents();

    }
}
