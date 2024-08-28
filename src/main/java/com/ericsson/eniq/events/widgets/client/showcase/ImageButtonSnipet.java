package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.button.ImageButton;
import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanelResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 09:42
 * To change this template use File | Settings | File Templates.
 */
public class ImageButtonSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);
    final CollapsePanelResourceBundle bundle = GWT.create(CollapsePanelResourceBundle.class);

    public FlowPanel createWidget(){
        final ImageButton imageButton = new ImageButton(bundle.arrow_across());
        imageButton.getElement().getStyle().setMargin(3, Style.Unit.PX);
        final String source = wrapCode(sourceBundle.Imagebutton().getText());
        return (wrapWidgetAndSource(imageButton, source));
    }
}
