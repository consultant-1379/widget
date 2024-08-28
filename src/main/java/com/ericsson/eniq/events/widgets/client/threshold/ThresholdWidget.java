/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.threshold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ericsson.eniq.events.common.client.CommonMain;
import com.ericsson.eniq.events.widgets.client.threshold.dataType.IThreshold;
import com.ericsson.eniq.events.widgets.client.threshold.dataType.IThresholdItem;
import com.ericsson.eniq.events.widgets.client.threshold.dataType.Threshold;
import com.ericsson.eniq.events.widgets.client.threshold.events.ThresholdWidgetChangeEvent;
import com.ericsson.eniq.events.widgets.client.togglebuttons.ToggleButtonMiniOnOff;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;

/**
 * @author eeikbe
 * @since 10/2012
 */
public class ThresholdWidget extends AbsolutePanel {

    private static int MARGIN_TOP = 20;

    private static double MARGIN_BOTTOM = 40;

    private static int SPACE = 7;

    private static int WIDGET_WIDTH = 78;

    //Default colors for the thresholds.
    private static String RED = "#FF0000";

    private static String ORANGE = "#FF9900";

    private static String YELLOW = "#FFFF00";

    private static String GREY = "#C0C0C0";

    private final List<String> colors = new ArrayList<String>();

    private static double TOGGLE_SQUARE = 15;

    private IThreshold threshold;

    static final FocusImpl impl = FocusImpl.getFocusImplForPanel();

    static final ThresholdWidgetResourceBundle resources;

    static {
        resources = GWT.create(ThresholdWidgetResourceBundle.class);
        resources.css().ensureInjected();
    }

    public @UiConstructor
    ThresholdWidget(final int numberOfThresholds, final String title) {
        super(impl.createFocusable());
        this.addStyleName(resources.css().thresholdWidget());

        //Add the title to the widget...
        addTitle(numberOfThresholds, title);

        //Add the toggle buttons and labels, and set the size of the widget...
        switch (numberOfThresholds) {
        case 2:
            createThresholdWidget(RED, GREY);
            break;
        case 3:
            createThresholdWidget(RED, ORANGE, GREY);
            break;
        case 4:
            createThresholdWidget(RED, ORANGE, YELLOW, GREY);
            break;
        default:
            createThresholdWidget(RED, ORANGE, YELLOW, GREY);
            break;
        }
    }

    public ThresholdWidget(final String... color) {
        createThresholdWidget(color);
    }

    private void createThresholdWidget(final String... color) {

        //Create the order of the colors (good (0%) -> bad(100%)).
        for (final String c : color) {
            colors.add(c);
        }

        try {
            //create the Threshold Object.
            threshold = new Threshold(color);

            //add the toggles to the ThresholdWidget.
            addToggles(color);

            //Add the range labels...
            addRange();

            //configure the height of the ThresholdWidget based on the number of toggles.
            final double height = MARGIN_TOP + MARGIN_BOTTOM + (color.length * TOGGLE_SQUARE)
                    + (color.length - 1 * SPACE);
            this.getElement().getStyle().setHeight(height, Style.Unit.PX);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    //Add the toggles to the ThresholdWidget.
    //Add a HR between each toggle.
    private void addToggles(final String... color) {
        final int left = 20;
        int top = MARGIN_TOP;

        final HTMLPanel maxRangeLine = new HTMLPanel("<hr/>");
        maxRangeLine.setStyleName(resources.css().rangeLine());
        maxRangeLine.getElement().getStyle().setTop(10, Style.Unit.PX);
        this.add(maxRangeLine);

        //Create a toggle for every color entered.
        for (final String c : color) {
            //create the HR...
            final HTMLPanel hr = new HTMLPanel("<hr/>");
            hr.setStyleName(resources.css().rangeLine());
            //Set it's position relative to the toggle.
            hr.getElement().getStyle().setTop(top + TOGGLE_SQUARE - 3, Style.Unit.PX);

            //create a toggle.
            final ToggleButtonMiniOnOff toggle = new ToggleButtonMiniOnOff(c, "Toggle Threshold");
            toggle.addClickHandler(new ThresholdClickHandler());
            toggle.setSize(TOGGLE_SQUARE, TOGGLE_SQUARE, Style.Unit.PX);
            toggle.setEnabled(true);

            //add the HR and toggle to the ThresholdWidget.
            this.add(hr);
            this.add(toggle, WIDGET_WIDTH - left - (int) TOGGLE_SQUARE, top);

            top = top + (int) TOGGLE_SQUARE + SPACE;
        }
    }

    //Add the title to the ThresholdWidget.
    private void addTitle(final int noOfThresholds, final String title) {
        final Label titleLabel = new Label(title);
        titleLabel.setStyleName(resources.css().title());

        //Set the position of the title...
        final double top = (noOfThresholds - 1) * TOGGLE_SQUARE;
        titleLabel.getElement().getStyle().setTop(top, Style.Unit.PX);
        this.add(titleLabel);
    }

    /**
     * Add the range labels.
     */
    private void addRange() {
        double top = 10;

        //add a label for each thresholdItem.
        for (final IThresholdItem item : threshold.getThresholdItems()) {
            final Label label = new Label();
            label.setStyleName(resources.css().range());
            label.getElement().getStyle().setTop(top, Style.Unit.PX);
            label.setText("" + (int) item.getMax() + "%"); //only show the max in the label.
            label.setTitle("threshold");
            this.add(label);
            top = top + 22;
        }
        final Label label = new Label();
        label.setStyleName(resources.css().range());
        label.getElement().getStyle().setTop(top, Style.Unit.PX);
        label.setText("0%");
        this.add(label);
    }

    /**
     * This is a test method to set the threshold!
     */
    public void setThreshold() {
        reverseThresholdColors();
        setThresholds(90, 80, 50, 3000);
    }

    //This method sets the the % threshold for each button and 
    //the maximum value for the threshold. 
    //The values should be entered high to low.
    private void setThresholds(final int... value) {
        //set the max for the threshold
        threshold.setRange(0, value[value.length - 1]); //the last value entered is the upper limit...

        //set the max and min for each thresholdItem...
        int i = 0;
        for (final IThresholdItem thresholdItem : threshold.getThresholdItems()) {
            if (i == 0) {
                thresholdItem.setMax(100); //set the max = 100%
            } else {
                thresholdItem.setMax(value[i - 1]);
            }
            if (i == value.length - 1) {
                thresholdItem.setMin(0); //set the min = 0%
            } else {
                thresholdItem.setMin(value[i++]);
            }
        }

        //update the labels...
        final Iterator<Widget> widgets = this.iterator();
        int j = 0;
        String val;
        while (widgets.hasNext()) {
            final Widget w = widgets.next();
            if (w.getTitle().equalsIgnoreCase("threshold")) {
                val = (int) threshold.getThresholdItems().get(j++).getMax() + "%";
                ((Label) w).setText(val);
            }
        }
    }

    /**
     * This is called when the threshold colors must be reversed...duhh!
     */
    private void reverseThresholdColors() {
        final Iterator<Widget> widgets = this.iterator();

        //reverse the colors...
        Collections.reverse(colors);
        threshold.setColor(colors);
        int i = 0;

        //set the new colors of the toggle buttons...
        while (widgets.hasNext()) {
            final Widget w = widgets.next();
            if (w instanceof ToggleButtonMiniOnOff) {
                ((ToggleButtonMiniOnOff) w).setToggleColor(colors.get(i++));
            }
        }
    }

    private class ThresholdClickHandler implements ClickHandler {

        /**
         * Called when a toggle is clicked on.
         *
         * @param event the {@link com.google.gwt.event.dom.client.ClickEvent} that was fired
         */
        @Override
        public void onClick(final ClickEvent event) {
            try {
                final IThresholdItem item = threshold.getThresholdItem(((ToggleButtonMiniOnOff) event.getSource())
                        .getColor());
                item.setEnabled(((ToggleButtonMiniOnOff) event.getSource()).getIsToggleOn());

                //fire the event to anyone who cares...
                CommonMain.getCommonInjector().getEventBus().fireEvent(new ThresholdWidgetChangeEvent(threshold));
            } catch (final Exception e) {
                e.printStackTrace();
            }
            final int i = 0;
        }
    }
}
