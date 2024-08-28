package com.ericsson.eniq.events.widgets.client.threshold.dataType;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
public interface IThresholdItem {

    /**
     *  Set the color of the threshold
     * @param color - hex color of threshold
     */
    void setColor(final String color);

    /**
     * Set the max value for the threshold
     * @param max - max value
     */
    void setMax(final double max);

    /**
     * Set the min value for the threshold
     * @param min - min value
     */
    void setMin(final double min);

    /**
     * Get the hex color of the threshold
     * @return - hex color of the threshold
     */
    String getColor();

    /**
     * Get the max value of the threshold
     * @return - max value of the threshold
     */
    double getMax();

    /**
     * Get the min value of the threshold
     * @return - min value of the threshold
     */
    double getMin();

    /**
     * Get the enabled state of the threshold.
     * @return - true: threshold is enabled | false: threshold is disabled.
     */
    Boolean isEnabled();

    /**
     * Set the thresholdItem enabled|disabled
     * @param enabled
     */
    public void setEnabled(final boolean enabled);
}
