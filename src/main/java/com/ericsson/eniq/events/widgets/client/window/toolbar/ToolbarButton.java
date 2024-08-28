package com.ericsson.eniq.events.widgets.client.window.toolbar;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class ToolbarButton extends AbstractToolbarItem {
    private final Button widget;

    public ToolbarButton(final String id, final String label, final ClickHandler handler) {
        super(id);
        widget = new Button(label, handler);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setEnable(final boolean enabled) {
        widget.setEnabled(enabled);
    }

    @Override
    public void setHidden(final boolean hidden) {
        widget.setVisible(!hidden);

    }

    @Override
    public boolean isHidden() {
        return widget.isVisible();
    }

    @Override
    public boolean isEnabled() {
        return widget.isEnabled();
    }

    @Override
    public void setToolTip(final String toolTip) {
        widget.setTitle(toolTip);
    }

    @Override
    public int getWidth() {
        return widget.getOffsetWidth();
    }

    @Override
    public int getHeight() {
        return widget.getOffsetHeight();
    }
}
