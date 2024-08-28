package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.togglebuttons.ToggleButtonMiniOnOff;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:07
 * To change this template use File | Settings | File Templates.
 */
public class ToggleButtonMiniOnOffSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final FlowPanel buttonsContainer = new FlowPanel();
        // buttonsContainer.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        buttonsContainer.getElement().getStyle().setWidth(100, Style.Unit.PX);
        final ToggleButtonMiniOnOff toggleButtonMiniOnOff1 = new ToggleButtonMiniOnOff("#549895",
                "Button 1: On - Enabled");
        final ToggleButtonMiniOnOff toggleButtonMiniOnOff2 = new ToggleButtonMiniOnOff("#549895",
                "Button 2: Off - Enabled");
        final ToggleButtonMiniOnOff toggleButtonMiniOnOff3 = new ToggleButtonMiniOnOff("#549895",
                "Button 3: On - Disabled");
        final ToggleButtonMiniOnOff toggleButtonMiniOnOff4 = new ToggleButtonMiniOnOff("#549895",
                "Button 4: Off - Disabled");
        final ToggleButtonMiniOnOff toggleButtonMiniOnOff5 = new ToggleButtonMiniOnOff("#89ba17",
                "Button 5: A different color");
        final ToggleButtonMiniOnOff toggleButtonMiniOnOff6 = new ToggleButtonMiniOnOff("#00285f",
                "Button 6: A different color");

        toggleButtonMiniOnOff4.setSize(6, 12, Style.Unit.PX);
        toggleButtonMiniOnOff5.setSize(12, 12, Style.Unit.PX);

        //set up the margins between the buttons...
        toggleButtonMiniOnOff1.getElement().getStyle().setMargin(3, Style.Unit.PX);
        toggleButtonMiniOnOff2.getElement().getStyle().setMargin(3, Style.Unit.PX);
        toggleButtonMiniOnOff3.getElement().getStyle().setMargin(3, Style.Unit.PX);
        toggleButtonMiniOnOff4.getElement().getStyle().setMargin(3, Style.Unit.PX);
        toggleButtonMiniOnOff5.getElement().getStyle().setMargin(3, Style.Unit.PX);
        toggleButtonMiniOnOff6.getElement().getStyle().setMargin(3, Style.Unit.PX);
        //set the tab index...
        toggleButtonMiniOnOff1.setTabIndex(5);
        toggleButtonMiniOnOff2.setTabIndex(4);
        toggleButtonMiniOnOff3.setTabIndex(3);
        toggleButtonMiniOnOff4.setTabIndex(2);
        toggleButtonMiniOnOff5.setTabIndex(1);
        toggleButtonMiniOnOff6.setTabIndex(6);
        //set the state of the buttons...
        //toggle1
        toggleButtonMiniOnOff1.setToggleOn();
        toggleButtonMiniOnOff1.setEnabled(true);
        //toggle2
        toggleButtonMiniOnOff2.setToggleOff();
        toggleButtonMiniOnOff2.setEnabled(true);
        //toggle3
        toggleButtonMiniOnOff3.setToggleOn();
        toggleButtonMiniOnOff3.setEnabled(false);
        //toggle4
        toggleButtonMiniOnOff4.setToggleOff();
        toggleButtonMiniOnOff4.setEnabled(false);
        //toggle5
        toggleButtonMiniOnOff5.setToggleOn();
        toggleButtonMiniOnOff5.setEnabled(true);
        //toggle 6
        toggleButtonMiniOnOff6.setToggleOn();
        toggleButtonMiniOnOff6.setEnabled(true);
        toggleButtonMiniOnOff6.addStyleName("rounded-corners");
        toggleButtonMiniOnOff6.setSize(12, 12, Style.Unit.PX);

        buttonsContainer.add(toggleButtonMiniOnOff1);
        buttonsContainer.add(toggleButtonMiniOnOff2);
        buttonsContainer.add(toggleButtonMiniOnOff3);
        buttonsContainer.add(toggleButtonMiniOnOff4);
        buttonsContainer.add(toggleButtonMiniOnOff5);
        buttonsContainer.add(toggleButtonMiniOnOff6);
        final String source = wrapCode(sourceBundle.togglebuttonminionoff().getText());
        return wrapWidgetAndSource(buttonsContainer, source);
    }
}
