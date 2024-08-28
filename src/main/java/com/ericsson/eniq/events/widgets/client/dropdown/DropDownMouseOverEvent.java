/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.dropdown;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author ecarsea
 * @since 2012
 *
 */
public class DropDownMouseOverEvent<T> extends GwtEvent<DropDownMouseOverHandler<T>> {

    /**
     * Handler type.
     */
    private static Type<DropDownMouseOverHandler<?>> TYPE;

    private final T value;

    public DropDownMouseOverEvent(T value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DropDownMouseOverHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    /**
     * Fires a value change event on all registered handlers in the handler
     * manager. If no such handlers exist, this method will do nothing.
     * 
     * @param <T> the old value type
     * @param source the source of the handlers
     * @param value the value
     */
    public static <T> void fire(HasDropDownMouseOverHandlers<T> source, T value) {
        if (TYPE != null) {
            DropDownMouseOverEvent<T> event = new DropDownMouseOverEvent<T>(value);
            source.fireEvent(event);
        }
    }

    /**
     * @return the value
     */
    public T getValue() {
        return value;
    };

    /**
     * Gets the type associated with this event.
     * 
     * @return returns the handler type
     */
    public static Type<DropDownMouseOverHandler<?>> getType() {
        if (TYPE == null) {
            TYPE = new Type<DropDownMouseOverHandler<?>>();
        }
        return TYPE;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    protected void dispatch(DropDownMouseOverHandler<T> handler) {
        handler.onMouseOver(this);
    }

}
