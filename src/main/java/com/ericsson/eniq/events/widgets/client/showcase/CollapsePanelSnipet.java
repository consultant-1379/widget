package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:05
 * To change this template use File | Settings | File Templates.
 */
public class CollapsePanelSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final CollapsePanel collapsePanel = new CollapsePanel();
        collapsePanel.setWidth("300px");
        collapsePanel.setText("Simple collapse panel");
        collapsePanel.setContent(new Label("Just some content, can be any widget in fact"));

        final String source = wrapCode(sourceBundle.collapsepanel().getText());
        return wrapWidgetAndSource(collapsePanel, source);
    }
}
