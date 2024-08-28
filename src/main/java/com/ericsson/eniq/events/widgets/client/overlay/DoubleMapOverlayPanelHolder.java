package com.ericsson.eniq.events.widgets.client.overlay;

import java.util.*;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public class DoubleMapOverlayPanelHolder<K1, K2, V, P extends IWizardOverlayPanel<V>> {

    private final HashMap<K1, HashMap<K2, P>> overlayPanelsMap;

    private transient List<P> overlayPanelsList;

    private P currentPanel;
    private K1 currentKey1;
    private K2 currentKey2;

    DoubleMapOverlayPanelHolder(HashMap<K1, HashMap<K2, P>> overlayPanelsMap,
            K1 currentVisibleK1, K2 currentVisibleK2) {
        this.overlayPanelsMap = overlayPanelsMap;
        setCurrentPanel(currentVisibleK1, currentVisibleK2);
    }

    public P getCurrentPanel() {
        return currentPanel;
    }

    public K1 getCurrentKey1() {
        return currentKey1;
    }

    public K2 getCurrentKey2() {
        return currentKey2;
    }

    public P setCurrentPanel(final K1 key1, final K2 key2) {
        P result = null;
        if (!this.overlayPanelsMap.isEmpty()) {
            final HashMap<K2, P> next = getMapForKey1(key1);
            result = adjustCurrentPanel(key2, next);
        }
        return result;
    }

    public P put(K1 key1, K2 key2, P overlayPanel) {
        HashMap<K2, P> map = overlayPanelsMap.get(key1);
        if (map == null) {
            map = new HashMap<K2, P>(2);
            overlayPanelsMap.put(key1, map);
            overlayPanelsList = null; // reset
        }

        return map.put(key2, overlayPanel);
    }

    public List<P> getOverlayPanels() {
        if (overlayPanelsList != null) {
            return overlayPanelsList;
        }

        List<P> list = new LinkedList<P>();
        Collection<HashMap<K2, P>> col = overlayPanelsMap.values();
        for (HashMap<K2, P> map : col) {
            for (K2 key2 : map.keySet()) {
                list.add(map.get(key2));
            }
        }
        overlayPanelsList = Collections.unmodifiableList(list);

        return overlayPanelsList;
    }

    public P getOverlayPanel(K1 key1, K2 key2) {
        P result = null;
        HashMap<K2, P> map = overlayPanelsMap.get(key1);
        if (map != null) {
            result = map.get(key2);
        }
        return result;
    }

    private HashMap<K2, P> getMapForKey1(final K1 key1) {
        K1 actualKey1 = key1;
        final HashMap<K2, P> next;
        if (actualKey1 == null) {
            actualKey1 = currentKey1;
            if (actualKey1 == null) {
                actualKey1 = this.overlayPanelsMap.keySet().iterator().next(); // any first
            }
        }
        next = this.overlayPanelsMap.get(actualKey1);

        if (next != null) {
            currentKey1 = actualKey1;
        }
        return next;
    }

    private P adjustCurrentPanel(final K2 key2, final HashMap<K2, P> next) {
        if (next == null) {
            return null;
        }

        K2 actualKey2 = key2;
        if (actualKey2 == null) {
            actualKey2 = currentKey2;
            if (actualKey2 == null) {
                actualKey2 = next.keySet().iterator().next(); // any first
            }
        }
        currentPanel = next.get(actualKey2);

        if (currentPanel != null) {
            currentKey2 = actualKey2;
        }

        return currentPanel;
    }
}
