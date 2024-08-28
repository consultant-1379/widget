package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.window.title.TitleWindow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public class TitleWindowSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final AbsolutePanel titleWindowContainer = new AbsolutePanel();
        titleWindowContainer.getElement().getStyle().setProperty("minHeight", "120px");
        final TitleWindow titleWindow = new TitleWindow("This window is not draggable", 60, false);
        titleWindowContainer.add(titleWindow);

        final TitleWindow titleWindowDraggable = new TitleWindow("This window is draggable (top changes "
                + "onMouseOver)", 60);
        titleWindowContainer.add(titleWindowDraggable);
        final String source = wrapCode(sourceBundle.titlewindow().getText());
        return wrapWidgetAndSource(titleWindowContainer, source);
    }
}
