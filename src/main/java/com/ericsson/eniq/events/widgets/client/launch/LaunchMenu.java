/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.widgets.client.launch;

import com.ericsson.eniq.events.common.client.CommonMain;
import com.ericsson.eniq.events.widgets.client.launch.resources.LaunchResourceBundle;
import com.ericsson.eniq.events.widgets.client.utilities.ZIndexUpdatedEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Side Bar Launch Menu
 *
 * @author ecarsea
 * @since October 2011
 */
public class LaunchMenu extends Composite {
    private static final Template TEMPLATE = GWT.create(Template.class);

    private static final int SLIDE_LEFT_OFFSET = 2;

    private static final String SLIDE_IN_TITLE = "Click to hide the launcher.";

    private static final String SLIDE_OUT_TITLE = "Click here to show Launcher.";

    interface LaunchMenuUiBinder extends UiBinder<Widget, LaunchMenu> {
    }

    private static LaunchMenuUiBinder uiBinder = GWT.create(LaunchMenuUiBinder.class);

    @UiField(provided = true)
    static LaunchResourceBundle resourceBundle;

    static {
        resourceBundle = GWT.create(LaunchResourceBundle.class);
        resourceBundle.style().ensureInjected();
    }

    @UiField
    FlowPanel container;

    @UiField
    Label header;

    @UiField
    FlowPanel flowPanel;

    @UiField
    Image handle;

    @UiField
    LaunchResizerPanel resizerPanel;

    @UiField
    HTML footerPanel;

    private boolean isSlideInState;

    private HandlerRegistration handlerRegistration;

    @UiHandler("handle")
    void onClick(final ClickEvent event) {
        if (isSlideInState) {
            slideOut();
        } else {
            slideIn();
        }
    }

    public LaunchMenu() {
        initWidget(uiBinder.createAndBindUi(this));
        /** Start with slide in **/
        slideIn();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        handlerRegistration = CommonMain.getCommonInjector().getEventBus()
                .addHandler(ZIndexUpdatedEvent.TYPE, new ZIndexUpdatedEvent.ZIndexUpdatedEventHandler() {

                    @Override
                    public void onZIndexUpdated(final String componentId, final int zIndex) {
                        bringToFront(zIndex+1);
                    }
                });
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        handlerRegistration.removeHandler();
    }

    private void bringToFront(final int zIndex) {
        container.getElement().getStyle().setZIndex(zIndex);
    }

    public void slideIn() {
        container.getElement().getStyle().setLeft(0, Unit.PX);
        handle.setResource(resourceBundle.handleClose());
        handle.addStyleName(resourceBundle.style().handle());
        handle.setTitle(SLIDE_IN_TITLE);
        isSlideInState = true;

    }

    public void slideOut() {
        container.getElement().getStyle().setLeft(-container.getOffsetWidth() + SLIDE_LEFT_OFFSET, Unit.PX);
        handle.setResource(resourceBundle.handleOpen());
        handle.addStyleName(resourceBundle.style().handle());
        handle.setTitle(SLIDE_OUT_TITLE);
        isSlideInState = false;

    }

    public void setLaunchMenuHeight(final double height, final Unit unit) {
        container.getElement().getStyle().setHeight(height, unit);
    }

    public boolean isSlideInState() {
        return isSlideInState;
    }

    public void setFooterHtml(final SafeHtml safeHtml) {
        footerPanel.setHTML(safeHtml);
    }

    /**
     * @param launchMenu
     * @param header
     */
    public void setLaunchMenu(final Widget launchMenu, final String header) {
        final HTMLPanel panel = new HTMLPanel(TEMPLATE.header(header));
        panel.setStyleName(resourceBundle.style().stackHeader());
        flowPanel.add(panel);
        flowPanel.add(launchMenu);
    }

    interface Template extends SafeHtmlTemplates {
        @Template("<span>{0}</span>")
        SafeHtml header(String header);
    }
}