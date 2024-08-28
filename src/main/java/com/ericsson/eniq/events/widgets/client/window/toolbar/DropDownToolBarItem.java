package com.ericsson.eniq.events.widgets.client.window.toolbar;

import java.util.List;

import com.ericsson.eniq.events.widgets.client.dropdown.IDropDownItem;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDownMenu;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDownPanel;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 * @param <T>
 */
public class DropDownToolBarItem<T extends IDropDownItem> extends AbstractToolbarItem {
    private final DropDownMenu<T> dropDown;

    public DropDownToolBarItem(final String id) {
        super(id);
        dropDown = new DropDownMenu<T>();
    }

    @Override
    public Widget asWidget() {
        return dropDown;
    }

    @Override
    public void setEnable(final boolean enabled) {
        dropDown.setEnabled(enabled);
    }

    @Override
    public void setHidden(final boolean hidden) {
        dropDown.setVisible(!hidden);
    }

    @Override
    public boolean isHidden() {
        return !dropDown.isVisible();
    }

    @Override
    public boolean isEnabled() {
        return dropDown.isEnabled();
    }

    @Override
    public void setToolTip(final String toolTip) {
        dropDown.setTitle(toolTip);
    }

    @Override
    public int getWidth() {
        return dropDown.getOffsetWidth();
    }

    @Override
    public int getHeight() {
        return dropDown.getOffsetHeight();
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<T> handler) {
        return dropDown.addValueChangeHandler(handler);
    }

    public void update(final List<T> items) {
        dropDown.update(items);
    }

    public void setValue(final T value) {
        dropDown.setValue(value);
    }

    public T getValue() {
        return dropDown.getValue();
    }

    public DropDownPanel getPopup() {
        return dropDown.getPopup();
    }

    public void setValue(final T value, final boolean fireEvents) {
        dropDown.setValue(value, fireEvents);
    }

    public DropDownMenu<T> getDropDown() {
        return dropDown;
    }
}
