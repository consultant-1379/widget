package com.ericsson.eniq.events.widgets.client.overlay.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public class WizardDblKeyChangeEvent<K1, K2> extends GwtEvent<WizardDblKeyChangeEventHandler<K1, K2>> {

    private final static Type<WizardDblKeyChangeEventHandler> TYPE = new Type<WizardDblKeyChangeEventHandler>();

    private final K1 key1;
    private final K2 key2;

    /**
     * @param key1 can be <tt>null</tt> if unchanged
     * @param key2 can be <tt>null</tt> if unchanged
     */
    public WizardDblKeyChangeEvent(K1 key1, K2 key2) {
        this.key1 = key1;
        this.key2 = key2;
    }

    @Override
    public Type<WizardDblKeyChangeEventHandler<K1, K2>> getAssociatedType() {
        //noinspection unchecked
        return (Type) getType();
    }

    public static Type<WizardDblKeyChangeEventHandler> getType() {
        return TYPE;
    }

    /**
     * @return if <tt>null</tt> - it means that it was not changed
     */
    public K1 getKey1() {
        return key1;
    }

    /**
     * @return if <tt>null</tt> - it means that it was not changed
     */
    public K2 getKey2() {
        return key2;
    }

    @Override
    protected void dispatch(final WizardDblKeyChangeEventHandler<K1, K2> handler) {
        handler.onAnyKeyChange(this);
    }

    @Override public String toString() {
        String key1Str = String.valueOf(key1);
        String key2Str = String.valueOf(key2);
        final StringBuilder sb = new StringBuilder(43 + key1Str.length() + key2Str.length());
        sb.append("WizardDblKeyChangeEvent");
        sb.append("{key1=").append(key1Str);
        sb.append(", key2=").append(key2Str);
        sb.append('}');
        return sb.toString();
    }
}
