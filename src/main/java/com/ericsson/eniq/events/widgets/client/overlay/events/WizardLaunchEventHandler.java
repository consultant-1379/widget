package com.ericsson.eniq.events.widgets.client.overlay.events;

import com.google.gwt.event.shared.EventHandler;

public interface WizardLaunchEventHandler<T> extends EventHandler {

    void onLaunch(WizardLaunchEvent<T> event);

}
