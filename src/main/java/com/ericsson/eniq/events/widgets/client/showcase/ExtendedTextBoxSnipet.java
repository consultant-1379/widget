package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.textbox.ExtendedTextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedTextBoxSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final FlowPanel container = new FlowPanel();
        container.getElement().getStyle().setWidth(300, Style.Unit.PX);
        //first textbox...
        final ExtendedTextBox extendedTextBox = new ExtendedTextBox();
        extendedTextBox.setWidth("100%");
        extendedTextBox.setDefaultText("This is the default text.");

        //second textbox...
        final ExtendedTextBox extendedTextBoxWithError = new ExtendedTextBox();
        extendedTextBoxWithError.setWidth("100%");
        extendedTextBoxWithError.setDefaultText("This text is invalid.");
        extendedTextBoxWithError.highlightInvalidField(true);

        container.add(extendedTextBox);
        container.add(extendedTextBoxWithError);

        final String source = wrapCode(sourceBundle.extendedtextbox().getText());
        return wrapWidgetAndSource(container, source);
    }
}
