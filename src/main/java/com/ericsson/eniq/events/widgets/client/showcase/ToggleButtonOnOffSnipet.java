package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.togglebuttons.ToggleButtonOnOff;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:09
 * To change this template use File | Settings | File Templates.
 */
public class ToggleButtonOnOffSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final ToggleButtonOnOff toggleButtonOnOff = new ToggleButtonOnOff();

        final String source = wrapCode(sourceBundle.togglebuttononoff().getText());
        return wrapWidgetAndSource(toggleButtonOnOff, source);
    }
}
