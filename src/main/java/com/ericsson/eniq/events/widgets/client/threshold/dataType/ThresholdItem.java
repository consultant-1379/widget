/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.threshold.dataType;

/**
 * @author eeikbe
 * @since 10/2012
 */
public class ThresholdItem implements IThresholdItem{
    
    private double min;
    private double max;
    private String color;
    private boolean isEnabled;
    /**
     * Set the color of the threshold
     *
     * @param color - hex color of threshold
     */
    @Override
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Set the max value for the threshold
     *
     * @param max - max value
     */
    @Override
    public void setMax(double max) {
        this.max = max;
    }

    /**
     * Set the min value for the threshold
     *
     * @param min - min value
     */
    @Override
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * Get the hex color of the threshold
     *
     * @return - hex color of the threshold
     */
    @Override
    public String getColor() {
        return color;
    }

    /**
     * Get the max value of the threshold
     *
     * @return - max value of the threshold
     */
    @Override
    public double getMax() {
        return this.max;
    }

    /**
     * Get the min value of the threshold
     *
     * @return - min value of the threshold
     */
    @Override
    public double getMin() {
        return this.min;
    }

    /**
     * Get the enabled state of the threshold.
     *
     * @return - true: threshold is enabled | false: threshold is disabled.
     */
    @Override
    public Boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * Set the thresholdItem enabled|disabled
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
}
