package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.togglebuttons.ToggleRail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:11
 * To change this template use File | Settings | File Templates.
 */
public class ToggleRailSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final ToggleRail toggleRail = new ToggleRail("100%");
        toggleRail.add("IMSI");
        toggleRail.add("MSISDN");
        toggleRail.setValue("IMSI", true);
        final String source = wrapCode(sourceBundle.togglerail().getText());
        return wrapWidgetAndSource(toggleRail, source);
    }
}
