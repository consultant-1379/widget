package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.slider.ColoredBarDataType;
import com.ericsson.eniq.events.widgets.client.slider.SlideBarDataType;
import com.ericsson.eniq.events.widgets.client.slider.SlideGroup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
public class SlideBarSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final List<ColoredBarDataType> coloredBarDataTypes = new ArrayList<ColoredBarDataType>();
        coloredBarDataTypes.add(new ColoredBarDataType(40, "green", "item1"));
        coloredBarDataTypes.add(new ColoredBarDataType(30, "blue", "item2"));
        coloredBarDataTypes.add(new ColoredBarDataType(0, "orange", "item3"));
        coloredBarDataTypes.add(new ColoredBarDataType(20, "gray", "item4"));
        coloredBarDataTypes.add(new ColoredBarDataType(10, "red", "item5"));
        final SlideBarDataType dataType = new SlideBarDataType(coloredBarDataTypes, 100, "Service Request");
        final SlideGroup slideBar = new SlideGroup(dataType);
        slideBar.setHeight("100px");
        final String source = wrapCode(sourceBundle.Slidebar().getText());
        return wrapWidgetAndSource(slideBar, source);
    }
}
