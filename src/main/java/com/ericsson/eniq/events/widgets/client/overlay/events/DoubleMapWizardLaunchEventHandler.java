package com.ericsson.eniq.events.widgets.client.overlay.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public interface DoubleMapWizardLaunchEventHandler<T> extends EventHandler {

    void onLaunch(DoubleMapWizardLaunchEvent<T> event);

}
