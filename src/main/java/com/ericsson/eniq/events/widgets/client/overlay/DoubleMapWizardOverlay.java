/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.overlay;

import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEvent;
import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEventHandler;
import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanel;
import com.ericsson.eniq.events.widgets.client.overlay.caching.SelectedItemsCache;
import com.ericsson.eniq.events.widgets.client.overlay.events.DoubleMapWizardLaunchEvent;
import com.ericsson.eniq.events.widgets.client.overlay.events.DoubleMapWizardLaunchEventHandler;
import com.ericsson.eniq.events.widgets.client.overlay.events.WizardDblKeyChangeEventHandler;
import com.ericsson.eniq.events.widgets.client.overlay.groups.IGroupHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.HashMap;
import java.util.List;

/**
 * {@link com.google.gwt.user.client.ui.Composite} widget for bottom wizard.
 * <p/>
 * It consists of one wizard overlay panel only that contains one collapsible section with checkboxes / radio buttons.
 * <p/>
 * It is used for KPI Analysis bottom wizard for chart (Tab KPI Analysis -> Launch bar -> Press button "Update" ->
 * Select Chart -> Press Launch -> the wizard at the bottom of the chart window).
 *
 * @param <T> an object with data for one particular checkbox / radio button; e.g. {@link
 *            com.ericsson.eniq.events.common.client.json.IJSONObject} or {@link
 *            com.ericsson.eniq.events.ui.shared.enums.IKpiType}
 * @see WizardOverlay
 */
public class DoubleMapWizardOverlay<K1, K2, T> extends Composite {

    private static DoubleMapWizardOverlayUiBinder uiBinder = GWT.create(DoubleMapWizardOverlayUiBinder.class);

    public static final int HEADER_HEIGHT = 17; // in px

    @UiField
    protected FlowPanel content;

    @UiField
    protected SimplePanel overlayPanelContainer;

    @UiField
    protected SimplePanel buttonsPanel;

    @UiField
    protected Button launchButton;

    @UiField
    protected DoubleMapWizardOverlayStyle style;

    private DoubleMapWizardKeyChangeEventHandler<K1, K2, T> groupChangeHandler;

    private boolean isLaunchButtonExternallyEnabled = true;

    private boolean isCollapsible = true;

    protected
    DoubleMapOverlayPanelHolder<K1, K2, T, AWizardOverlayPanel<T>> overlayPanelHolder;

    /**
     * Note: {@link java.util.LinkedHashMap} is used because order of panels is important as well.
     *
     * @param overlayPanelsMap map of wizard overlay panels
     * @param currentK1        current key 2
     * @param currentK2        current key 2
     */
    public DoubleMapWizardOverlay(
            HashMap<K1, HashMap<K2, AWizardOverlayPanel<T>>> overlayPanelsMap, K1 currentK1, K2 currentK2) {
        this.overlayPanelHolder =
                new DoubleMapOverlayPanelHolder<K1, K2, T, AWizardOverlayPanel<T>>(
                        overlayPanelsMap, currentK1, currentK2);

        init();
    }

    public void doLaunch() {
        List<T> cachedSelectedItems = overlayPanelHolder.getCurrentPanel().storeSelectedItems();

        // Check if disabled also fires click event
        fireEvent(new DoubleMapWizardLaunchEvent<T>(cachedSelectedItems));

        setCollapsible(true);
        setCollapsed(true);
    }

    public void setCollapsible(final boolean isCollapsible) {
        this.isCollapsible = isCollapsible;
    }

    public void setCollapsed(final boolean isCollapsed) {
        if (isCollapsible) {
            buttonsPanel.setVisible(!isCollapsed);

            for (AWizardOverlayPanel<T> panel : overlayPanelHolder.getOverlayPanels()) {
                if (panel.getGroupHandler() != null) {
                    panel.getGroupHandler().setPanelsCollapsed(isCollapsed);
                }
            }
        }
    }

    public AWizardOverlayPanel<T> getCurrentOverlayPanel() {
        return overlayPanelHolder.getCurrentPanel();
    }

    public List<AWizardOverlayPanel<T>> getOverlayPanels() {
        return overlayPanelHolder.getOverlayPanels();
    }

    public HandlerRegistration addLaunchEventHandler(final DoubleMapWizardLaunchEventHandler<T> handler) {
        return addHandler(handler, DoubleMapWizardLaunchEvent.getType());
    }

    /**
     * Used in CSS eval
     *
     * @return header height
     */
    @SuppressWarnings("UnusedDeclaration") public static String getHeaderHeight() {
        return HEADER_HEIGHT + "px";
    }

    public boolean isLaunchButtonExternallyEnabled() {
        return isLaunchButtonExternallyEnabled;
    }

    public void setLaunchButtonExternallyEnabled(boolean isEnabled) {
        if (!isEnabled && isLaunchButtonExternallyEnabled) {
            launchButton.setEnabled(isEnabled);
        }
        isLaunchButtonExternallyEnabled = isEnabled;

        handleLaunchButtonEnabling();
    }

    public void setLaunchButtonFocus(boolean isFocused) {
        launchButton.setFocus(isFocused);
    }

    public WizardDblKeyChangeEventHandler getAsChangeGroupHandler() {
        if (groupChangeHandler == null) {
            groupChangeHandler = new DoubleMapWizardKeyChangeEventHandler<K1, K2, T>(this);
        }

        return groupChangeHandler;
    }

    protected void handleLaunchButtonEnabling() {
        launchButton.setEnabled(isLaunchButtonExternallyEnabled
                && !overlayPanelHolder.getCurrentPanel().getSelected().isEmpty());
    }

    SelectedItemsCache<T> createSelectedItemsCache() {
        return new SelectedItemsCache<T>() {
            @Override
            public List<T> getSelected() {
                return overlayPanelHolder.getCurrentPanel().getCachedSelectedItems();
            }
        };
    }

    HasWidgets getOverlayPanelContainer() {
        return overlayPanelContainer;
    }

    DoubleMapOverlayPanelHolder<K1, K2, T, AWizardOverlayPanel<T>> getOverlayPanelHolder() {
        return overlayPanelHolder;
    }

    private void init() {
        SelectedItemsCache<T> cache = createSelectedItemsCache();

        for (IWizardOverlayPanel<T> overlayPanel : overlayPanelHolder.getOverlayPanels()) {
            overlayPanel.setCache(cache);
        }

        initWidget(uiBinder.createAndBindUi(this));
        overlayPanelContainer.add(overlayPanelHolder.getCurrentPanel());

        ChildSelectEventHandler childSelectEventHandler = new ChildSelectEventHandler() {
            @Override
            public void onChildSelect(final ChildSelectEvent event) {
                handleLaunchButtonEnabling();
            }
        };

        for (AWizardOverlayPanel<T> panel : overlayPanelHolder.getOverlayPanels()) {
            panel.addChildSelectEventHandler(childSelectEventHandler);

            final IGroupHandler groupHandler = panel.getGroupHandler();
            for (final CollapsePanel collapsePanel : groupHandler.getCollapsePanels()) {
                collapsePanel.addHeaderClickHandler(new ClickHandler() {
                    @Override public void onClick(ClickEvent event) {
                        if (isCollapsible) {
                            boolean isAnyExtended = groupHandler.isAnyPanelExpanded();
                            buttonsPanel.setVisible(isAnyExtended);
                        }
                    }
                });
            }
        }

        launchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (isLaunchButtonExternallyEnabled) {
                    doLaunch();
                }
            }
        });

        // By default disable Launch button & panel
        this.overlayPanelHolder.getCurrentPanel().setEnabled(false);
        launchButton.setEnabled(false);

        setCollapsed(false);
        setCollapsible(false);
    }

    interface DoubleMapWizardOverlayUiBinder extends UiBinder<Widget, DoubleMapWizardOverlay> {
    }

    interface DoubleMapWizardOverlayStyle extends CssResource {

        String content();

        String wrapper();

        String buttonsPanel();
    }

}