package com.ericsson.eniq.events.widgets.client.togglebuttons;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface ToggleButtonOnOffResourceBundle extends ClientBundle {
    @Source("ToggleButtonOnOff.css")
    ToggleButtonStyle css();

    @Source("images/OFF_new.png")
    ImageResource offBackgroundImage();

    @Source("images/on_off.png")
    ImageResource onBackgroundImage();

    @Source("images/slider.png")
    ImageResource toggleSliderImage();

    interface ToggleButtonStyle extends CssResource {

        @ClassName("toggle-up-hovering")
        String toggleUpHovering();

        @ClassName("toggle-down-hovering")
        String toggleDownHovering();

        @ClassName("toggle-down")
        String toggleDown();

        @ClassName("toggle-up")
        String toggleUp();

        String toggle();

        @ClassName("toggle-up-disabled")
        String toggleUpDisabled();
    }
}