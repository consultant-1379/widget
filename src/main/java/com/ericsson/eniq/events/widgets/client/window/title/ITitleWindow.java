package com.ericsson.eniq.events.widgets.client.window.title;

import com.ericsson.eniq.events.widgets.client.component.ComponentMessageType;

public interface ITitleWindow {

    void setMessage(ComponentMessageType type, String title, String description);

    boolean hasMessage();

    void clearMessage();

}
