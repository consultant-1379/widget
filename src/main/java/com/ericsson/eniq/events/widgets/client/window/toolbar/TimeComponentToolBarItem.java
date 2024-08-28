package com.ericsson.eniq.events.widgets.client.window.toolbar;

import java.util.List;

import com.ericsson.eniq.events.widgets.client.dropdown.DropDownPanel;
import com.ericsson.eniq.events.widgets.client.dropdown.TimePeriodDropDownItem;
import com.ericsson.eniq.events.widgets.client.dropdown.time.DropDownTimeComponent;
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
public class TimeComponentToolBarItem extends AbstractToolbarItem {
    private final DropDownTimeComponent dropDown;

    public TimeComponentToolBarItem(final String id) {
        super(id);
        dropDown = new DropDownTimeComponent() {
            @Override
            protected int[] getPositionOffsets() {
                return TimeComponentToolBarItem.this.getPositionOffsets();
            }
        };
    }

    protected int[] getPositionOffsets() {
        return new int[] { DropDownTimeComponent.CALENDER_OFFSET_Y, DropDownTimeComponent.CALENDER_OFFSET_X };
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

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<TimePeriodDropDownItem> handler) {
        return dropDown.addValueChangeHandler(handler);
    }

    public void update(final List<TimePeriodDropDownItem> items) {
        dropDown.update(items);
    }

    public void setValue(final TimePeriodDropDownItem value) {
        dropDown.setValue(value);
    }

    public TimePeriodDropDownItem getValue() {
        return dropDown.getValue();
    }

    public DropDownPanel getPopup() {
        return dropDown.getPopup();
    }

    public void setValue(final TimePeriodDropDownItem value, final boolean fireEvents) {
        dropDown.setValue(value, fireEvents);
    }

    public void updateCalendarSelection(final long fromDate, final long toDate) {
        dropDown.updateCalendarSelection(fromDate, toDate);
    }

    public void hideCalendar() {
        dropDown.hideCalendar();
    }

    public DropDownTimeComponent getTimeComponent() {
        return dropDown;
    }
}
