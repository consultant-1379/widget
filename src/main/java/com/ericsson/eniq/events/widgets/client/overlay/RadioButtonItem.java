package com.ericsson.eniq.events.widgets.client.overlay;

import com.google.gwt.user.client.ui.RadioButton;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 02 2012
 */
class RadioButtonItem {

    private final WizardOverlayType overlayType;
    private final RadioButton radioButton;

    public RadioButtonItem(WizardOverlayType overlayType, RadioButton radioButton) {
        this.overlayType = overlayType;
        this.radioButton = radioButton;
    }

    public WizardOverlayType getOverlayType() {
        return overlayType;
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RadioButtonItem");
        sb.append("{overlayType=").append(overlayType);
        sb.append(", radioButton=").append(radioButton);
        sb.append('}');
        return sb.toString();
    }
}
