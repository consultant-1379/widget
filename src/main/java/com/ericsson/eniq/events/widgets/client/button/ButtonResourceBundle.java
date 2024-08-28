package com.ericsson.eniq.events.widgets.client.button;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface ButtonResourceBundle extends ClientBundle {
    @Source("Button.css")
    ButtonStyle css();

    interface ButtonStyle extends CssResource {
        String textButton();
    }
}