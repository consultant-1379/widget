package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.calendar.CalendarPopUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public class CalendarSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final CalendarPopUp calendarPanel = new CalendarPopUp();
        calendarPanel.setMaxNumberDaysRange(7);
        Button calendarButton = new Button("Show Calendar");
        calendarButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                calendarPanel.show();
            }
        });

        final String source = wrapCode(sourceBundle.calendar().getText());
        return wrapWidgetAndSource(calendarButton, source);
    }
}
