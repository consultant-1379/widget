package com.ericsson.eniq.events.widgets.client.overlay.events;

import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public class DoubleMapWizardLaunchEvent<T> extends GwtEvent<DoubleMapWizardLaunchEventHandler<T>> {

    private final static Type<DoubleMapWizardLaunchEventHandler> TYPE = new Type<DoubleMapWizardLaunchEventHandler>();

    private final List<T> selectedItems;

    public DoubleMapWizardLaunchEvent(List<T> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @Override
    public Type<DoubleMapWizardLaunchEventHandler<T>> getAssociatedType() {
        //noinspection unchecked
        return (Type) getType();
    }

    public static Type<DoubleMapWizardLaunchEventHandler> getType() {
        return TYPE;
    }

    public List<T> getSelectedItems() {
        return selectedItems;
    }

    @Override
    protected void dispatch(final DoubleMapWizardLaunchEventHandler<T> handler) {
        handler.onLaunch(this);
    }

    @Override public String toString() {
        String itemsStr = String.valueOf(selectedItems);
        final StringBuilder sb = new StringBuilder(itemsStr.length() + 51);
        sb.append("DoubleMapWizardLaunchEventHandler");
        sb.append("{selectedItems=").append(itemsStr).append('}');
        return sb.toString();
    }
}
