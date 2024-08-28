package com.ericsson.eniq.events.widgets.client.togglebuttons;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface ToggleRailResourceBundle extends ClientBundle {
    @Source("ToggleRail.css")
    ToggleRail css();

    interface ToggleRail extends CssResource {

        String toggleRail();

        String selected();

        String disabled();
        
        String focus();
    }
}
