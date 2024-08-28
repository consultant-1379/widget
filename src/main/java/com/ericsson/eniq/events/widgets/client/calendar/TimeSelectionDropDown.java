package com.ericsson.eniq.events.widgets.client.calendar;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

import java.util.Date;

public class TimeSelectionDropDown extends Composite {

    private ListBox timePeriodsListBox;
    private TimePeriodEnum  currentSelection;
    private CalendarPopUp customDatePicker;

    private Date fromDate;
    private Date toDate;

    private String tempSelection;

    public TimeSelectionDropDown()
    {
       setUpListBox();
       initWidget(timePeriodsListBox);
    }

    private void setUpListBox() {

        timePeriodsListBox= new ListBox();

        for(TimePeriodEnum t: TimePeriodEnum.values())
        {
            timePeriodsListBox.addItem(t.toString());
        }

        timePeriodsListBox.addItem("Custom Time");

        timePeriodsListBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(final ChangeEvent event) {
               getCurrentSelection();
            }
        });
    }

    private void getCurrentSelection()
    {
        String current = timePeriodsListBox.getItemText(timePeriodsListBox.getSelectedIndex());
        System.err.println("Current: "+current);
        if (!current.equalsIgnoreCase("Custom Time"))
        {
            this.currentSelection = TimePeriodEnum.fromString(current);
        }
        else {
            getCustomDatePicker().show();
        }
    }

    private CalendarPopUp getCustomDatePicker()
    {
        if (customDatePicker!=null)
        {
            return customDatePicker;
        }
        else {customDatePicker = new CalendarPopUp();
        return customDatePicker;
        }
    }


    private Date getFromDate() {
        return fromDate;
    }

    private void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    private Date getToDate() {
        return toDate;
    }

    private void setToDate(final Date toDate) {
        this.toDate = toDate;
    }
}
