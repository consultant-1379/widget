package com.ericsson.eniq.events.widgets.client;

public interface ToString<V> {
    /**
     * @param value item, that has to be converted to String
     * @return string representation
     */
    String toString(V value);
}
