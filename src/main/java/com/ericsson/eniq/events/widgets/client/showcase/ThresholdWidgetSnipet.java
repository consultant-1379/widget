package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.threshold.ThresholdWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 09:55
 * To change this template use File | Settings | File Templates.
 */
public class ThresholdWidgetSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);
    public EventBus eventbus;

    public FlowPanel createWidget(){
        final ThresholdWidget thresholdWidget = new ThresholdWidget(4, "title");
        thresholdWidget.setHeight("125px");
        thresholdWidget.getElement().getStyle().setPosition(Style.Position.RELATIVE);
//        eventbus.addHandler(ThresholdWidgetChangeEvent.TYPE, new IThresholdWidgetChange() {
//            @Override
//            public void handleThresholdWidgetChange(final IThreshold threshold) {
//                final MessageDialog messageDialog = new MessageDialog();
//                final List<IThresholdItem> items = threshold.getThresholdItems();
//                String message = "\n";
//                for (final IThresholdItem item : items) {
//                    message = message + item.getColor() + " is enabled: " + item.isEnabled() + ", max = "
//                            + item.getMax() + ", min = " + item.getMin() + "\n";
//                }
//                messageDialog.show("Success", "Got a event from the Threshold Widget:" + message,
//                        MessageDialog.DialogType.INFO);
//            }
//        });

        final String source = wrapCode(sourceBundle.Threshold().getText());
        return wrapWidgetAndSource(thresholdWidget, source);
    }
}
