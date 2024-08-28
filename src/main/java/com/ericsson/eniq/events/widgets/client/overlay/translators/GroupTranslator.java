package com.ericsson.eniq.events.widgets.client.overlay.translators;

public interface GroupTranslator<G> {
    String getId(G group);
    String getName(G group);
}
