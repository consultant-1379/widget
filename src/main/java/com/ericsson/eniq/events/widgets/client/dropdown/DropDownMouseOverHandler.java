/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.dropdown;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author ecarsea
 * @since 2012
 *
 */
public interface DropDownMouseOverHandler<T> extends EventHandler {
    void onMouseOver(DropDownMouseOverEvent<T> event);
}
