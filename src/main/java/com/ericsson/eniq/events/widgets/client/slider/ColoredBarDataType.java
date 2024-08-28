package com.ericsson.eniq.events.widgets.client.slider;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class ColoredBarDataType {
    private final int percentageWidth;

    private final String color;

    private int width;

    private final String label;

    public ColoredBarDataType(final int percentageWidth, final String color, final String label) {
        this.percentageWidth = percentageWidth;
        this.color = color;
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public String getLabel() {
        return label;
    }

    public int getWidth() {
        return width;
    }

    public int getPercentageWidth() {
        return percentageWidth;
    }
}
