/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.overlay;

import static com.ericsson.eniq.events.widgets.client.overlay.WizardOverlayType.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEvent;
import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEventHandler;
import com.ericsson.eniq.events.widgets.client.overlay.caching.SelectedItemsCache;
import com.ericsson.eniq.events.widgets.client.overlay.events.WizardDblKeyChangeEventHandler;
import com.ericsson.eniq.events.widgets.client.overlay.events.WizardLaunchEvent;
import com.ericsson.eniq.events.widgets.client.overlay.events.WizardLaunchEventHandler;
import com.ericsson.eniq.events.widgets.client.overlay.translators.ItemTranslator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Main {@link Composite} widget for wizard dialog.
 * <p/>
 * <ul>It consists of: <li>Radio button panel that is used to switch between wizard overlay panels ({@link
 * AWizardOverlayPanel})</li> <li>Wizard overlay panels that contain collapsible sections with checkboxes / radio
 * buttons</li> </ul>
 * <p/>
 * It is used for KPI Analysis main wizard (Tab KPI Analysis -> Launch bar -> Press button "Update" -> the wizard).
 *
 * @param <T> an object with data for one particular checkbox / radio button; e.g. {@link
 *            com.ericsson.eniq.events.common.client.json.IJSONObject} or {@link
 *            com.ericsson.eniq.events.ui.shared.enums.IKpiType}
 * @see DoubleMapWizardOverlay
 */
public class WizardOverlay<T> extends Composite {

    private static WizardOverlayUiBinder uiBinder = GWT.create(WizardOverlayUiBinder.class);

    public static final int HEADER_HEIGHT = 17; // in px

    @UiField
    protected Label header;

    @UiField
    protected FlowPanel content;

    @UiField
    protected SimplePanel overlayPanelContainer;

    @UiField
    protected Button launchButton;

    @UiField
    protected WizardOverlayStyle style;

    @UiField
    protected FlowPanel radioButtonPanel;

    protected OverlayPanelHolder<T, AWizardOverlayPanel<T>> overlayPanelHolder;

    private boolean isCollapsible = true;

    private final LinkedList<RadioButtonItem> radioButtonsList = new LinkedList<RadioButtonItem>();

    private final List<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

    /**
     * Creates one {@link com.ericsson.eniq.events.widgets.client.overlay.CheckBoxesWizardOverlayPanel}
     * inside.
     *
     * @param itemResolver for wizard overlay panel
     * @param label        text for header
     * @param overlayTypes supported {@link com.ericsson.eniq.events.widgets.client.overlay.WizardOverlayType}s
     */
    public WizardOverlay(final ItemTranslator<T> itemResolver, final String label,
            final LinkedHashSet<WizardOverlayType> overlayTypes) {
        AWizardOverlayPanel<T> overlayPanel = new CheckBoxesWizardOverlayPanel<T>(itemResolver);
        this.overlayPanelHolder = new OverlayPanelHolder<T, AWizardOverlayPanel<T>>(overlayPanel);
        init(label, overlayTypes);
    }

    /**
     * Note: {@link LinkedHashMap} is used because order of panels is important as well.
     *
     * @param label                     text for header
     * @param overlayPanelsMap          map of panels for each supported {@link com.ericsson.eniq.events.widgets.client.overlay.WizardOverlayType}
     * @param currentVisibleOverlayType current {@link com.ericsson.eniq.events.widgets.client.overlay.WizardOverlayType}
     *                                  to show
     */
    public WizardOverlay(String label, LinkedHashMap<WizardOverlayType, AWizardOverlayPanel<T>> overlayPanelsMap,
            WizardOverlayType currentVisibleOverlayType) {
        this.overlayPanelHolder = new OverlayPanelHolder<T, AWizardOverlayPanel<T>>(overlayPanelsMap,
                currentVisibleOverlayType);
        init(label, overlayPanelsMap.keySet());
    }

    public void doLaunch() {
        List<T> cachedSelectedItems = overlayPanelHolder.getCurrentPanel().storeSelectedItems();

        // Check if disabled also fires click event
        fireEvent(new WizardLaunchEvent<T>(getType(), cachedSelectedItems));
        launchButton.setEnabled(true);
        setCollapsible(true); /*LG EDIT - only now it can be collapsed*/
        setCollapsed(true); /*LG EDIT */
    }

    public void setType(final WizardOverlayType type) {
        // Questionable, should we allow setting type, if isTypeSelectionEnabled is disabled
        // TODO eeicmsy question where set false ?
        for (final RadioButtonItem item : radioButtonsList) {
            if (type.equals(item.getOverlayType())) {
                item.getRadioButton().setValue(Boolean.TRUE, true);
            }
        }
    }

    public void setTypeSelectionEnabled(final boolean isTypeSelectionEnabled) {
        for (final RadioButtonItem item : radioButtonsList) {
            item.getRadioButton().setEnabled(isTypeSelectionEnabled);
        }
    }

    public WizardOverlayType getType() {
        for (final RadioButtonItem item : radioButtonsList) {
            if (item.getRadioButton().getValue()) {
                return item.getOverlayType();
            }
        }
        return null;
    }

    public void setCollapsible(final boolean isCollapsible) {
        this.isCollapsible = isCollapsible;
    }

    public void setCollapsed(final boolean isCollapsed) {
        if (isCollapsible) {
            content.setVisible(!isCollapsed);
            if (isCollapsed) {
                header.removeStyleName(style.open());
            } else {
                header.addStyleName(style.open());
            }
        }
    }

    public AWizardOverlayPanel<T> getCurrentOverlayPanel() {
        return overlayPanelHolder.getCurrentPanel();
    }

    public Collection<AWizardOverlayPanel<T>> getOverlayPanels() {
        return overlayPanelHolder.getOverlayPanels();
    }

    /**
     * Note: does not return <tt>HandlerRegistration</tt> because removing is taken into account within the method.
     *
     * @param handler handler of group switches
     */
    public void addGroupChangeHandler(WizardDblKeyChangeEventHandler handler) {
        for (AWizardOverlayPanel<T> overlayPanel : getOverlayPanels()) {
            registerHandler(overlayPanel.addGroupChangeHandler(handler));
        }
    }

    /**
     * Note: does not return <tt>HandlerRegistration</tt> because removing is taken into account within the method.
     *
     * @param handler the handler
     */
    public void addLaunchEventHandler(final WizardLaunchEventHandler<T> handler) {
        registerHandler(addHandler(handler, WizardLaunchEvent.getType()));
    }

    /**
     * Used in CSS eval
     *
     * @return header height
     */
    @SuppressWarnings("UnusedDeclaration") public static String getHeaderHeight() {
        return HEADER_HEIGHT + "px";
    }

    @Override protected void onDetach() {
        super.onDetach();
        for (HandlerRegistration registration : handlerRegistrations) {
            registration.removeHandler();
        }
        handlerRegistrations.clear();
    }

    protected void handleLaunchButtonEnabling(final WizardOverlayType type) {
        final boolean isLaunchButton = GRID.equals(type) || MAP.equals(type) || CHART.equals(type)
                && !overlayPanelHolder.getCurrentPanel().getSelected().isEmpty();
        launchButton.setEnabled(isLaunchButton);
    }

    protected void onWizardOverlayTypeChange(final WizardOverlayType type) {
        // Handle launch button enabling
        handleLaunchButtonEnabling(type);

        // Handle panel enabling
        overlayPanelHolder.getCurrentPanel().setEnabled(CHART.equals(type));
    }

    SelectedItemsCache<T> createSelectedItemsCache() {
        return new SelectedItemsCache<T>() {
            @Override
            public List<T> getSelected() {
                return overlayPanelHolder.getCurrentPanel().getCachedSelectedItems();
            }
        };
    }

    /**
     * Any {@link HandlerRegistration}s added will be removed when
     * {@link #onDetach()} is called. This provides a handy way to track event
     * handler registrations when binding and unbinding.
     *
     * @param handlerRegistration The registration.
     */
    private void registerHandler(final HandlerRegistration handlerRegistration) {
        handlerRegistrations.add(handlerRegistration);
    }

    private void init(String label, Set<WizardOverlayType> overlayTypes) {
        SelectedItemsCache<T> cache = createSelectedItemsCache();
        for (IWizardOverlayPanel<T> overlayPanel : overlayPanelHolder.getOverlayPanels()) {
            overlayPanel.setCache(cache);
        }

        initWidget(uiBinder.createAndBindUi(this));
        overlayPanelContainer.add(overlayPanelHolder.getCurrentPanel());

        final TypeSelectionHandler typeSelectionHandler = new TypeSelectionHandler();
        for (final WizardOverlayType overlayType : overlayTypes) {
            final RadioButton radioButton = new RadioButton("wizardOverlay", overlayType.getText());
            radioButton.setStyleName(style.type());
            radioButtonsList.add(new RadioButtonItem(overlayType, radioButton));
            radioButtonPanel.add(radioButton);
            registerHandler(radioButton.addValueChangeHandler(typeSelectionHandler));
        }

        registerHandler(header.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                setCollapsed(content.isVisible());
            }
        }));

        ChildSelectEventHandler childSelectEventHandler = new ChildSelectEventHandler() {
            @Override
            public void onChildSelect(final ChildSelectEvent event) {
                handleLaunchButtonEnabling(getType());
            }
        };

        for (AWizardOverlayPanel<T> panel : overlayPanelHolder.getOverlayPanels()) {
            registerHandler(panel.addChildSelectEventHandler(childSelectEventHandler));
        }

        registerHandler(launchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                doLaunch();
            }
        }));

        header.setText(label);

        // By default disable Launch button & panel
        this.overlayPanelHolder.getCurrentPanel().setEnabled(false);
        launchButton.setEnabled(false);

        setCollapsed(false); /*LG EDIT - wizard always open on first launch*/
        setCollapsible(false);/*LG EDIT - when opened first do not allow it to collapse*/
    }

    class TypeSelectionHandler implements ValueChangeHandler<Boolean> {
        @Override
        public void onValueChange(final ValueChangeEvent<Boolean> ignored) {
            onWizardOverlayTypeChange(WizardOverlay.this.getType());
        }
    }

    interface WizardOverlayUiBinder extends UiBinder<Widget, WizardOverlay> {
    }

    interface WizardOverlayStyle extends CssResource {

        String content();

        String wrapper();

        String buttonsPanel();

        String type();

        String header();

        String open();

        String radioButtonPanel();
    }
}