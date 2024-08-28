package com.ericsson.eniq.events.widgets.client.overlay;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public class OverlayPanelHolder<T, P extends IWizardOverlayPanel<T>> {

    private final HashMap<WizardOverlayType, P> overlayPanelsMap;

    private P currentPanel;

    OverlayPanelHolder(HashMap<WizardOverlayType, P> overlayPanelsMap,
            WizardOverlayType currentVisibleOverlayType) {
        this.overlayPanelsMap = overlayPanelsMap;
        if (!overlayPanelsMap.isEmpty()) {
            if (overlayPanelsMap.size() == 1 || currentVisibleOverlayType == null) {
                currentPanel = overlayPanelsMap.values().iterator().next();
            } else {
                currentPanel = overlayPanelsMap.get(currentVisibleOverlayType);
            }
        }
    }

    OverlayPanelHolder(final P currentPanel) {
        this(new HashMap<WizardOverlayType, P>(1) {
            {
                put(null, currentPanel);
            }
        });
    }

    OverlayPanelHolder(HashMap<WizardOverlayType, P> overlayPanelsMap) {
        this(overlayPanelsMap, null);
    }

    public P getCurrentPanel() {
        return currentPanel;
    }

    public void setCurrentPanel(P currentPanel) {
        this.currentPanel = currentPanel;
    }

    public P put(WizardOverlayType key, P overlayPanel) {
        return overlayPanelsMap.put(key, overlayPanel);
    }

    public Collection<P> getOverlayPanels() {
        return overlayPanelsMap.values();
    }

    public P getOverlayPanel(WizardOverlayType type) {
        return overlayPanelsMap.get(type);
    }
}
