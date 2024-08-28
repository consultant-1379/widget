package com.ericsson.eniq.events.widgets.client.calendar;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface CalendarResourceBundle extends ClientBundle
{
    @Source("Calendar.css")
    CalendarStyle css();

    @Source("images/middle_arrow_gap.png")
    ImageResource middleArrow();

    @Source("images/middle_arrow_gap_hover.png")
    ImageResource middleArrowHover();

    @Source("images/warning_icon.png")
    ImageResource warningIcon();

    interface CalendarStyle extends CssResource {

        String calendarPopUp();

        String calendarHeader();

        String datePickerNextButton();

        String datePickerWeekdayLabel();

        String dropDown();

        String datePickerWeekendLabel();

        String datePickerMonth();

        String datePickerMonthSelector();

        String datePickerDay();

        String EDatePicker();

        String datePickerDayIsToday();

        String datePickerPreviousButton();

        String datePickerDayIsHighlighted();

        String left();

        String right();

        String middleArrow();

        String HeaderLabels();

        String calBody();

        String calendarHeaderLabel();

        String calButtonsPanel();

        String calendarFooter();

        String buttons();

        String datePickerDayIsWeekend();

        String warningLabel();

        String panelMargin();

        String datePickerDayIsValue();
    }
}