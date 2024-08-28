package com.ericsson.eniq.events.widgets.client.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ericsson.eniq.events.widgets.client.dropdown.DropDownMenu;
import com.ericsson.eniq.events.widgets.client.dropdown.StringDropDownItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * @since 03 2012
 */
public class CalendarPanel extends Composite {

    private static CalendarUiBinder uiBinder = GWT.create(CalendarUiBinder.class);

    @UiField
    DatePicker toCalendar;

    @UiField
    DatePicker fromCalendar;

    @UiField
    DropDownMenu<StringDropDownItem> toHour;

    @UiField
    DropDownMenu<StringDropDownItem> fromHour;

    @UiField
    DropDownMenu<StringDropDownItem> toMin;

    @UiField
    DropDownMenu<StringDropDownItem> fromMin;

    @UiField
    Label errorLabel;

    @UiField
    FocusPanel arrowButton;

    Button okButtonRef;

    private Date fromDate;

    private Date toDate;

    private boolean isCanceled;

    private final CalendarState calendarState = new CalendarState();

    @SuppressWarnings("rawtypes")
    CalendarChange calendarChangeHandler = new CalendarChange();

    @SuppressWarnings("deprecation")
    public Date getFromDate() {
        if (fromCalendar.getValue() != null) {
            fromDate = fromCalendar.getValue();
        } else {
            fromDate = new Date();//today
            fromDate.setDate(fromDate.getDate() - 1);//yesterday, take one from todays date to use as default
        }
        fromDate.setHours(Integer.parseInt(fromHour.getValue().getValue()));
        fromDate.setMinutes(Integer.parseInt(fromMin.getValue().getValue()));
        fromDate.setSeconds(0);
        fromDate.setTime(setMilliSeconds(fromDate.getTime()));

        return fromDate;
    }

    @SuppressWarnings("deprecation")
    public Date getToDate() {
        if (toCalendar.getValue() != null) {
            toDate = toCalendar.getValue();
        } else {
            toDate = new Date();
        }
        toDate.setHours(Integer.parseInt(toHour.getValue().getValue()));
        toDate.setMinutes(Integer.parseInt(toMin.getValue().getValue()));
        toDate.setSeconds(0);
        toDate.setTime(setMilliSeconds(toDate.getTime()));

        return toDate;
    }

    @SuppressWarnings("deprecation")
    public void setFromDate(final Date fromDate) {
        if (fromDate != null) {
            this.fromDate = fromDate;
            fromCalendar.setValue(fromDate);
            fromCalendar.setCurrentMonth(fromDate);
            fromHour.setValue(new StringDropDownItem(appendZero(fromDate.getHours())), false);
            fromMin.setValue(new StringDropDownItem(appendZero(fromDate.getMinutes())), false);
        }
    }

    @SuppressWarnings("deprecation")
    public void setToDate(final Date toDate) {
        if (toDate != null) {
            this.toDate = toDate;
            toCalendar.setValue(toDate);
            toCalendar.setCurrentMonth(toDate);
            toHour.setValue(new StringDropDownItem(appendZero(toDate.getHours())), false);
            toMin.setValue(new StringDropDownItem(appendZero(toDate.getMinutes())), false);
        }
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(final boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    public void setErrorMessage(final String message, final Button okButton) {
        this.okButtonRef = okButton;
        if (errorLabel != null) {
            errorLabel.setWidth((message.length() > 43 ? 275 : 215) + "px");
            errorLabel.setText(message);

            if (!errorLabel.isVisible()) {
                errorLabel.setVisible(true);
            }
        }
    }

    public void removeErrorMessage() {
        if (errorLabel != null) {
            errorLabel.setText("");
            if (errorLabel.isVisible()) {
                errorLabel.setVisible(false);
            }
            if (okButtonRef != null) {
                okButtonRef.setEnabled(true);
            }
        }
    }

    interface CalendarUiBinder extends UiBinder<Widget, CalendarPanel> {
    }

    public CalendarPanel() {
        initWidget(uiBinder.createAndBindUi(this));

        toHour.setWidth("87px");
        fromHour.setWidth("87px");
        toMin.setWidth("87px");
        fromMin.setWidth("87px");

        setUpCalendars();
        populateHours();
        populateMinutes();
        setupArrowButton();
        saveCalendarState();
    }

    private void setupArrowButton() {
        arrowButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                toCalendar.setValue(fromCalendar.getValue());
                toCalendar.setCurrentMonth(fromCalendar.getValue());
            }
        });
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    private void setUpCalendars() {
        toCalendar.addValueChangeHandler(calendarChangeHandler);
        fromCalendar.addValueChangeHandler(calendarChangeHandler);
        toCalendar.setValue(new Date());

        final Date temp = new Date();
        temp.setDate(temp.getDate() - 1);
        fromCalendar.setValue(temp);
    }

    public void saveCalendarState() {
        calendarState.saveState();
    }

    public void restoreCalendarState() {
        calendarState.restoreState();
    }

    @SuppressWarnings("unchecked")
    private void populateHours() {
        final List<StringDropDownItem> hours = new ArrayList<StringDropDownItem>();
        for (int i = 0; i < 24; i++) {
            hours.add(new StringDropDownItem(appendZero(i)));
        }

        toHour.update(hours);
        fromHour.update(hours);

        toHour.setValue(new StringDropDownItem("00"));
        fromHour.setValue(new StringDropDownItem("00"));

        toHour.addValueChangeHandler(calendarChangeHandler);
        fromHour.addValueChangeHandler(calendarChangeHandler);
    }

    private String appendZero(final int i) {
        final String hour = i < 10 ? "0" + i : "" + i;
        return hour;
    }

    @SuppressWarnings("unchecked")
    private void populateMinutes() {
        final List<StringDropDownItem> minutes = new ArrayList<StringDropDownItem>();
        for (int i = 0; i < 60; i = i + 15) {
            final String min = appendZero(i);
            minutes.add(new StringDropDownItem(min));
        }

        toMin.update(minutes);
        fromMin.update(minutes);

        toMin.setValue(new StringDropDownItem("00"));
        fromMin.setValue(new StringDropDownItem("00"));

        toMin.addValueChangeHandler(calendarChangeHandler);
        fromMin.addValueChangeHandler(calendarChangeHandler);
    }

    private long setMilliSeconds(final long time) {
        return time / 1000 * 1000;
    }

    class CalendarChange<V> implements ValueChangeHandler<V> {
        @Override
        public void onValueChange(final ValueChangeEvent<V> valueChangeEvent) {
            removeErrorMessage();
        }
    }

    private class CalendarState {
        private Date savedFromDate;

        private Date savedToDate;

        private StringDropDownItem savedFromHour;

        private StringDropDownItem savedFromMin;

        private StringDropDownItem savedToHour;

        private StringDropDownItem savedToMin;

        public void saveState() {
            savedFromDate = fromCalendar.getValue();
            savedToDate = toCalendar.getValue();
            savedFromHour = fromHour.getValue();
            savedFromMin = fromMin.getValue();
            savedToHour = toHour.getValue();
            savedToMin = toMin.getValue();
        }

        public void restoreState() {
            fromCalendar.setValue(savedFromDate);
            toCalendar.setValue(savedToDate);
            fromHour.setValue(savedFromHour);
            fromMin.setValue(savedFromMin);
            toHour.setValue(savedToHour);
            toMin.setValue(savedToMin);

        }
    }
}
