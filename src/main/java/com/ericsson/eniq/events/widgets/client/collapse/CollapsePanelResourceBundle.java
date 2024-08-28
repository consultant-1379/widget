package com.ericsson.eniq.events.widgets.client.collapse;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface CollapsePanelResourceBundle extends ClientBundle {

    @Source("images/arrow_across.png")
    ImageResource arrow_across();

    @Source("images/arrow_down.png")
    ImageResource arrow_down();

    @Source("CollapsePanel.css")
    CollapsePanelStyle style();

    interface CollapsePanelStyle extends CssResource {

        String wrapper();
        String disabled();
        String open();

        String header();
        String icon();
    }
}
