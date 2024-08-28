package com.ericsson.eniq.events.widgets.client.dropdown.time;

import com.ericsson.eniq.events.common.client.time.TimePeriod;
import com.ericsson.eniq.events.widgets.client.calendar.CalendarPanel;
import com.ericsson.eniq.events.widgets.client.calendar.CalendarPopUp;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDownMenu;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDownPanel;
import com.ericsson.eniq.events.widgets.client.dropdown.TimePeriodDropDownItem;
import com.extjs.gxt.ui.client.core.XDOM;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class DropDownTimeComponent extends Composite implements HasValueChangeHandlers<TimePeriodDropDownItem> {
    public static final int CALENDER_OFFSET_Y = -450;

    public static final int CALENDER_OFFSET_X = 20;

    private final DropDownMenu<TimePeriodDropDownItem> dropDown;

    private final HandlerManager handlerManager;

    private CalendarPopUp customDatePicker;

    private TimePeriodDropDownItem lastSelectedItem;

    public DropDownTimeComponent() {
        dropDown = new DropDownMenu<TimePeriodDropDownItem>();
        handlerManager = new HandlerManager(this);
        dropDown.setWidth("245px");
        dropDown.addValueChangeHandler(new ValueChangeHandler<TimePeriodDropDownItem>() {

            @Override
            public void onValueChange(final ValueChangeEvent<TimePeriodDropDownItem> event) {
                if (event.getValue() == null) {
                    fireEvent(event);
                    return;
                }
                if (TimePeriod.CUSTOM.equals(dropDown.getValue().getTimePeriod())) {
                    getCalendarPopUp().show();
                    final int[] offsets = getPositionOffsets();
                    final DropDownPanel popup = dropDown.getPopup();
                    final int left = Math.max(0, popup.getAbsoluteLeft() + offsets[0]);
                    final int top = popup.getAbsoluteTop() + offsets[1];
                    getCalendarPopUp().setZIndex(Math.max(1000003, XDOM.getTopZIndex()));
                    getCalendarPopUp().setPopupPosition(left, top);
                } else {
                    lastSelectedItem = event.getValue();
                    fireEvent(event);
                }
            }
        });
        initWidget(dropDown);
    }

    public TimePeriodDropDownItem getValue() {
        return dropDown.getValue();
    }

    public void setValue(final TimePeriodDropDownItem value) {
        setValue(value, false);
    }

    public void setValue(final TimePeriodDropDownItem value, final boolean fireEvents) {
        if (lastSelectedItem == null && !fireEvents) {
            lastSelectedItem = value;
        }
        if (value.getTimePeriod() == TimePeriod.CUSTOM) {
            updateCalendarSelection(value.getFrom().getTime(), value.getTo().getTime());
        }
        dropDown.setValue(value, fireEvents);
    }

    public void update(final List<TimePeriodDropDownItem> items) {
        dropDown.update(items);
    }

    /**
     * get left and top position to show calendar
     * @return
     */
    protected int[] getPositionOffsets() {
        return new int[] { CALENDER_OFFSET_Y, CALENDER_OFFSET_X };
    }

    public DropDownMenu<TimePeriodDropDownItem> getDropDown() {
        return dropDown;
    }

    @Override
    public void fireEvent(final GwtEvent<?> arg0) {
        handlerManager.fireEvent(arg0);
    }

    public CalendarPopUp getCalendarPopUp() {
        if (customDatePicker == null) {
            customDatePicker = new CalendarPopUp();
            customDatePicker.setMaxNumberDaysRange(7);
            customDatePicker.addValueChangeHandler(new ValueChangeHandler<CalendarPanel>() {

                @Override
                public void onValueChange(final ValueChangeEvent<CalendarPanel> event) {
                    if (!event.getValue().isCanceled()) {//Ok is pressed
                        final Date toDate = getCalendarPopUp().getEcalendar().getToDate();
                        final Date fromDate = getCalendarPopUp().getEcalendar().getFromDate();
                        final TimePeriodDropDownItem value = new TimePeriodDropDownItem(TimePeriod.CUSTOM, fromDate,
                                toDate);
                        lastSelectedItem = value;
                        setValue(lastSelectedItem, false);
                        fireEvent(new CustomValueChange(value));
                    } else {//cancel is pressed
                        setValue(lastSelectedItem, false);
                    }
                }
            });
        }
        return customDatePicker;
    }

    /**
     * Just to provide support for showing different time formatting
     * @param item
     * @return
     */
    protected String formatItem(final TimePeriodDropDownItem item) {
        return item.toString();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<TimePeriodDropDownItem> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    private class CustomValueChange extends ValueChangeEvent<TimePeriodDropDownItem> {

        protected CustomValueChange(final TimePeriodDropDownItem value) {
            super(value);
        }

    }

    public void setEnabled(final boolean enabled) {
        dropDown.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return dropDown.isEnabled();
    }

    public DropDownPanel getPopup() {
        return dropDown.getPopup();
    }

    public void updateCalendarSelection(final long fromDate, final long toDate) {
        getCalendarPopUp().setDates(new Date(fromDate), new Date(toDate));
    }

    public void hideCalendar() {
        final CalendarPopUp calendarPopUp = getCalendarPopUp();
        if (calendarPopUp != null) {
            calendarPopUp.hide();
        }
    }

    public void setMaxNumberDaysRange(final int i) {
        getCalendarPopUp().setMaxNumberDaysRange(i);
    }
}
