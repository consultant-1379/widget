package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 08/05/13
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
public class TextButtonSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public  FlowPanel createWidget(){
        final FlowPanel container = new FlowPanel();
        container.getElement().getStyle().setWidth(300, Style.Unit.PX);

        final Button button = new Button("Standard Button");
        container.add(button);

        final String source = wrapCode(sourceBundle.button().getText());
        return wrapWidgetAndSource(container, source);
    }
}
