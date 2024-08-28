/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.dropdown;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author ecarsea
 * @since 2012
 *
 */
public interface HasDropDownMouseOverHandlers<T> extends HasHandlers {
    HandlerRegistration addDropDownMouseOverHandler(DropDownMouseOverHandler<T> handler);
}
