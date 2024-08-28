package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.window.FloatingWindow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:12
 * Floating Window Example
 */
public class FloatingWindowSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);
    public FlowPanel createWidget(){
        FlowPanel holder = new FlowPanel();
        final AbsolutePanel container = new AbsolutePanel ();
        container.setWidth("900px");
        container.setHeight("180px");
        final Button ExampleFloat = new Button("Floating Window Example");     //Adds a button to the
        container.add(ExampleFloat);
        holder.add(container);
        ExampleFloat.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                final FloatingWindow floatingWindow = new FloatingWindow(container, 300, 150, "Floating Window Example");
                floatingWindow.setFooterText("Text!!!");
                SimplePanel p = new SimplePanel();
                p.setHeight("10px");
                p.setWidth("10px");
                p.getElement().getStyle().setBackgroundColor("red");
                floatingWindow.setFooterWidget(p);                         //Adds the simple Panel to the footer of the floating window
                Label label = new Label("Main Text!!!");
                floatingWindow.add(label);
                floatingWindow.getElement().getStyle().setPosition(Style.Position.RELATIVE);
                floatingWindow.show();
            }
        });

        final String source = wrapCode(sourceBundle.FloatingWindow().getText());
        return wrapWidgetAndSource(holder, source);
    }}
