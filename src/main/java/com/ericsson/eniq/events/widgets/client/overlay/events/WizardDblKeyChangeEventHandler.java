package com.ericsson.eniq.events.widgets.client.overlay.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public interface WizardDblKeyChangeEventHandler<K1, K2> extends EventHandler {

    void onAnyKeyChange(WizardDblKeyChangeEvent<K1, K2> event);

}
