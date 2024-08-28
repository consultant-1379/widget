package com.ericsson.eniq.events.widgets.client.overlay.translators;

public interface ItemTranslator<I> {

    String getLabel(I item);
    String getTooltip(I item);
    String getGroupId(I item);
    String getId(I item);
}
