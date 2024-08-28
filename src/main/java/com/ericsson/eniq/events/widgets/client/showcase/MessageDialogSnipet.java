package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.dialog.MessageDialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class MessageDialogSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final VerticalPanel container = new VerticalPanel();
        final MessageDialog messageDialog = new MessageDialog();
        //container.clear();
        final Button WarningButton = new Button("Show Warning message");
        final Button ErrorButton = new Button("Show Error message");
        final Button InfoButton = new Button("Show Info message");
        WarningButton.setWidth("150px");
        ErrorButton.setWidth("150px");
        InfoButton.setWidth("150px");

        container.setHeight("100px");
        container.add(ErrorButton);
        container.add(InfoButton);
        container.add(WarningButton);
        WarningButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                messageDialog.show("Title", "This is the content", MessageDialog.DialogType.WARNING);
            }
        });
        InfoButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                messageDialog.show("Title", "This is the content", MessageDialog.DialogType.INFO);
            }
        });
        ErrorButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                messageDialog.show("Title", "This is the content", MessageDialog.DialogType.ERROR);
            }
        });

        final String source = wrapCode(sourceBundle.messagedialog().getText());
        return wrapWidgetAndSource(container, source);
    }
}
