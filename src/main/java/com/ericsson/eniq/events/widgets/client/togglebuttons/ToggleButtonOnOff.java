package com.ericsson.eniq.events.widgets.client.togglebuttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * Ericsson Branded GWT ON/OFF Slide Toggle Button
 */

public class ToggleButtonOnOff extends ToggleButton {

    private static final ToggleButtonOnOffResourceBundle resources = GWT.create(ToggleButtonOnOffResourceBundle.class);

    private final static String CSS_CLASS_NAME = "toggle";
    private String onColor = "#0f4583";

    public ToggleButtonOnOff() {
        this(new Image());
        //apply the default color to the toggle switch...
        getElement().getStyle().setBackgroundColor(onColor);
    }

    private ToggleButtonOnOff(Image slider) {
        AbstractImagePrototype.create(resources.toggleSliderImage()).applyTo(slider);

        getDownFace().setImage(slider);
        getUpFace().setImage(slider);

        resources.css().ensureInjected();

        setStyleName(CSS_CLASS_NAME);
    }

    /**
     * Use this method to set the "on" color of the toggle switch to a color
     * other than the default (#0f4583).
     * @param onColor
     */
    public void setOnColor(final String onColor){
        this.onColor = onColor;
        getElement().getStyle().setBackgroundColor(onColor);
    }
}