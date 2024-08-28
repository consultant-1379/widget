package com.ericsson.eniq.events.widgets.client.overlay;

import com.ericsson.eniq.events.widgets.client.overlay.events.WizardDblKeyChangeEvent;
import com.ericsson.eniq.events.widgets.client.overlay.events.WizardDblKeyChangeEventHandler;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
class DoubleMapWizardKeyChangeEventHandler<K1, K2, T>
        implements WizardDblKeyChangeEventHandler<K1, K2> {

    final DoubleMapWizardOverlay<K1, K2, T> wizardOverlay;

    DoubleMapWizardKeyChangeEventHandler(DoubleMapWizardOverlay<K1, K2, T> wizardOverlay) {
        this.wizardOverlay = wizardOverlay;
    }

    @Override public void onAnyKeyChange(WizardDblKeyChangeEvent<K1, K2> event) {
        DoubleMapOverlayPanelHolder<K1, K2, T, AWizardOverlayPanel<T>> overlayPanelHolder =
                wizardOverlay.getOverlayPanelHolder();

        switchWizardPanelOnKeyChange(event, overlayPanelHolder);

        // Handle launch button enabling
        wizardOverlay.handleLaunchButtonEnabling();
    }

    private void switchWizardPanelOnKeyChange(WizardDblKeyChangeEvent<K1, K2> event,
            DoubleMapOverlayPanelHolder<K1, K2, T, AWizardOverlayPanel<T>> overlayPanelHolder) {
        AWizardOverlayPanel<T> currentSelectedPanel = overlayPanelHolder.getCurrentPanel();
        AWizardOverlayPanel<T> newSelectedPanel = overlayPanelHolder.setCurrentPanel(event.getKey1(),
                event.getKey2());

        if (newSelectedPanel != null && currentSelectedPanel != newSelectedPanel) {
            boolean isAnyPanelExtended = currentSelectedPanel.getGroupHandler()
                    .isAnyPanelExpanded();

            currentSelectedPanel.setEnabled(false);
            currentSelectedPanel.removeFromParent();

            newSelectedPanel.setEnabled(true);
            newSelectedPanel.getGroupHandler().setPanelsCollapsed(!isAnyPanelExtended);

            wizardOverlay.getOverlayPanelContainer().add(newSelectedPanel);
        }
    }
}
