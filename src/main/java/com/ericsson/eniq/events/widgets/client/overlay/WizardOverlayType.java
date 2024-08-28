package com.ericsson.eniq.events.widgets.client.overlay;

public enum WizardOverlayType {

    CHART("Chart View"), GRID("Grid View"), MAP("Map View");

    private String text;

    private WizardOverlayType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
