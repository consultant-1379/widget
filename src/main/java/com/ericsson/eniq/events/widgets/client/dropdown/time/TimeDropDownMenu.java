/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.dropdown.time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ericsson.eniq.events.common.client.time.TimePeriod;
import com.ericsson.eniq.events.widgets.client.calendar.CalendarPanel;
import com.ericsson.eniq.events.widgets.client.calendar.CalendarPopUp;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDownMenu;
import com.ericsson.eniq.events.widgets.client.dropdown.TimePeriodDropDownItem;
import com.extjs.gxt.ui.client.core.XDOM;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author ecarsea
 * @since 2012
 *
 *Use DropDownTimeComponent. It is having handling of cancel button as well and is consistent through out eniq events.
 */
@Deprecated
public class TimeDropDownMenu extends DropDownMenu<TimePeriodDropDownItem> {

    private final static DateTimeFormat labelDateFormat = DateTimeFormat.getFormat("HH:mm, yyyy-MM-dd");

    private static final int CALENDAR_LEFT_OFFSET = 50;

    private int left, top;

    private CalendarPopUp customDatePicker;

    public TimeDropDownMenu() {
        populateTimeDropDown();
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Composite#onAttach()
     */
    @Override
    protected void onAttach() {
        super.onAttach();
        /**
         * Defer command, the dimensions are wrong in current event loop if absolute positioning of parent container 
         */
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                left = TimeDropDownMenu.this.getAbsoluteLeft() + TimeDropDownMenu.this.getOffsetWidth()
                        + CALENDAR_LEFT_OFFSET;
                top = TimeDropDownMenu.this.getAbsoluteTop() - 20;
            }
        });
    }

    protected void populateTimeDropDown() {
        final List<TimePeriodDropDownItem> timePeriods = new ArrayList<TimePeriodDropDownItem>();
        for (final TimePeriod timePeriod : TimePeriod.values()) {
            timePeriods.add(new TimePeriodDropDownItem(timePeriod));
        }
        update(timePeriods);
        addValueChangeHandler(new ValueChangeHandler<TimePeriodDropDownItem>() {
            @Override
            public void onValueChange(final ValueChangeEvent<TimePeriodDropDownItem> event) {
                if (TimePeriod.CUSTOM.equals(getValue().getTimePeriod())) {
                    getCalendarPopUp().show();
                    getCalendarPopUp().setPopupPosition(left, top);
                    getCalendarPopUp().setZIndex(Math.max(5000, XDOM.getTopZIndex()));
                }
            }
        });
    }

    public Date getFrom() {
        Date from;

        final TimePeriod period = getValue().getTimePeriod();
        if (TimePeriod.CUSTOM.equals(period)) {
            from = getValue().getFrom();
            if (from == null) {
                from = getCalendarPopUp().getEcalendar().getFromDate();
            }
        } else {
            final Date curDate = new Date();
            from = new Date(curDate.getTime() - period.toMiliseconds());
        }

        return from;
    }

    public Date getTo() {
        Date to;

        if (TimePeriod.CUSTOM.equals(getValue().getTimePeriod())) {
            to = getValue().getTo();
            if (to == null) {
                to = getCalendarPopUp().getEcalendar().getToDate();
            }
        } else {
            to = new Date();
        }

        return to;
    }

    /**
     * @param left
     */
    public void setCalendarPopupLeft(final int left) {
        this.left = left;
    }

    /**
     * @return left
     */
    public int getCalendarPopupLeft() {
        return left;
    }

    /**
     * @param top
     */
    public void setCalendarPopupTop(final int top) {
        this.top = top;
    }

    /**
     * @return top
     */
    public int getCalendarPopupTop() {
        return top;
    }

    protected void setCustomTimeText() {
        setDefaultText(getFormattedDateRangeLabel(getCalendarPopUp().getEcalendar().getFromDate(), getCalendarPopUp()
                .getEcalendar().getToDate()));
    }

    protected CalendarPopUp getCalendarPopUp() {
        if (customDatePicker != null) {
            return customDatePicker;
        }
        customDatePicker = new CalendarPopUp();
        customDatePicker.addValueChangeHandler(new ValueChangeHandler<CalendarPanel>() {

            @Override
            public void onValueChange(final ValueChangeEvent<CalendarPanel> event) {
                setCustomTimeText();
            }
        });
        return customDatePicker;
    }

    String getFormattedDateRangeLabel(final Date from, final Date to) {
        return labelDateFormat.format(from) + " to " + labelDateFormat.format(to);
    }
}
