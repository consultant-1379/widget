/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.drill;

/**
 * @author ecarsea
 * @since 2012
 *
 */
public interface IDrillCallback {

    /**
     * @param drillDownTargetId
     * @return
     */
    void onDrillDownSelected(String drillDownTargetId);
}
