package com.ericsson.eniq.events.widgets.client.button;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
public interface CogButtonResourceBundle extends ClientBundle {
    @Source("CogButton.css")
    ButtonStyle style();

    @ClientBundle.Source(value = "images/cog_Upgrade.png")
    ImageResource cogWheel();

    public interface ButtonStyle extends CssResource {
        String cogWheel();

        String configurationImage();

        String configurationText();

        String configurationDisabled();
    }
}
