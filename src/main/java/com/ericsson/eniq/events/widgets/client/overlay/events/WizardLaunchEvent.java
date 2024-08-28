package com.ericsson.eniq.events.widgets.client.overlay.events;

import com.ericsson.eniq.events.widgets.client.overlay.WizardOverlayType;
import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

/**
 * @author edavboj - Davis Bojars
 * @author ealeerm - Alexey Ermykin
 * @since 02 2012
 */
public class WizardLaunchEvent<T> extends GwtEvent<WizardLaunchEventHandler<T>> {

    private final static Type<WizardLaunchEventHandler> TYPE = new Type<WizardLaunchEventHandler>();

    private final WizardOverlayType wizardOverlayType;
    private final List<T> selectedItems;

    public WizardLaunchEvent(WizardOverlayType type, List<T> selectedItems) {
        this.wizardOverlayType = type;
        this.selectedItems = selectedItems;
    }

    @Override
    public Type<WizardLaunchEventHandler<T>> getAssociatedType() {
        //noinspection unchecked
        return (Type) getType();
    }

    public static Type<WizardLaunchEventHandler> getType() {
        return TYPE;
    }

    public WizardOverlayType getWizardOverlayType() {
        return wizardOverlayType;
    }

    public List<T> getSelectedItems() {
        return selectedItems;
    }

    @Override
    protected void dispatch(final WizardLaunchEventHandler<T> handler) {
        handler.onLaunch(this);
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("WizardLaunchEvent");
        sb.append("{wizardOverlayType=").append(wizardOverlayType);
        sb.append(", selectedItems=").append(selectedItems);
        sb.append('}');
        return sb.toString();
    }
}