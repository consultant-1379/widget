package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.button.CogButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 09:44
 * To change this template use File | Settings | File Templates.
 */
public class CogButtonSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final String source = wrapCode(sourceBundle.CogButton().getText());
        final CogButton cog1 = new CogButton();
        cog1.getElement().getStyle().setMargin(3, Style.Unit.PX);
        return wrapWidgetAndSource(cog1, source);
    }
}
