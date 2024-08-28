package com.ericsson.eniq.events.widgets.client.slider;

import java.util.List;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class SlideBarDataType {
    private final List<ColoredBarDataType> barDataTypes;

    private final int minDistance = 0;

    private final int thumbWidth = 0;

    private final int width = 500;

    private Formatter formatter;

    private int availableWidth;

    private final int value;

    private final String label;

    public SlideBarDataType(final List<ColoredBarDataType> coloredBarDataTypes, final int value, final String label) {
        barDataTypes = coloredBarDataTypes;
        this.value = value;
        this.label = label;
        calculateWidths();
    }

    private void calculateWidths() {
        final int barCount = barDataTypes.size();
        final int thumbCount = barCount - 1;
        assert barCount > 1 : "Number of bars must be greater that 1.";
        this.availableWidth = width - (thumbCount * thumbWidth);
        assert availableWidth > (barCount * minDistance) : "Invalid slide bar width.";
        int occupiedSize = 0;
        for (int i = 0; i < barCount - 1; i++) {
            final ColoredBarDataType coloredBarDataType = barDataTypes.get(i);
            final int calculatedWidth = (int) (coloredBarDataType.getPercentageWidth() * availableWidth / 100.0);
            assert calculatedWidth >= minDistance : "Bar width cannot be less that minimum width.";
            coloredBarDataType.setWidth(calculatedWidth);
            occupiedSize = occupiedSize + calculatedWidth;
        }
        assert availableWidth > occupiedSize : "Invalid percentages for colored bars.";
        barDataTypes.get(barCount - 1).setWidth(availableWidth - occupiedSize);
    }

    public int getAvailableWidth() {
        return availableWidth;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public List<ColoredBarDataType> getBarDataTypes() {
        return barDataTypes;
    }

    public int getWidth() {
        return width;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(final Formatter formatter) {
        this.formatter = formatter;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
