package com.ericsson.eniq.events.widgets.client.window.toolbar;

import com.ericsson.eniq.events.widgets.client.button.CogButton;
import com.ericsson.eniq.events.widgets.client.window.toolbar.AbstractToolbarItem;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class CogToolbarItem extends AbstractToolbarItem {
    private final CogButton cogButton;

    public CogToolbarItem(final String id) {
        super(id);
        cogButton = new CogButton();
    }

    public HandlerRegistration addClickHandler(final ClickHandler clickHandler) {
        return cogButton.addClickHandler(clickHandler);

    }

    @Override
    public Widget asWidget() {
        return cogButton;
    }

    @Override
    public void setEnable(final boolean enabled) {
        cogButton.setEnabled(enabled);
    }

    @Override
    public void setHidden(final boolean hidden) {
        cogButton.setVisible(!hidden);
    }

    @Override
    public boolean isHidden() {
        return !cogButton.isVisible();
    }

    @Override
    public boolean isEnabled() {
        return cogButton.isEnabled();
    }

    @Override
    public void setToolTip(final String toolTip) {
        cogButton.setTitle(toolTip);
    }

    @Override
    public int getWidth() {
        return cogButton.getOffsetWidth();
    }

    @Override
    public int getHeight() {
        return cogButton.getOffsetHeight();
    }
}