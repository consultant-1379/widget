package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.component.ComponentMessagePanel;
import com.ericsson.eniq.events.widgets.client.component.ComponentMessageType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:25
 * To change this template use File | Settings | File Templates.
 */
public class MessagePanelSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final ComponentMessagePanel messagePanel = new ComponentMessagePanel();
        messagePanel.setWidth("300px");
        messagePanel.populate(ComponentMessageType.INFO, "Simple message example");

        final String source = wrapCode(sourceBundle.messagedpanel().getText());
        return wrapWidgetAndSource(messagePanel, source);
    }
}
