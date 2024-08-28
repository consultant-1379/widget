package com.ericsson.eniq.events.widgets.client.calendar;

import java.util.Date;

import com.ericsson.eniq.events.widgets.client.common.DateTimeUtil;
import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class CalendarPopUp implements HasValueChangeHandlers<CalendarPanel> {

    private static CalendarResourceBundle resources;

    static {
        resources = GWT.create(CalendarResourceBundle.class);
        resources.css().ensureInjected();
    }

    private static CalendarPopUpUiBinder uiBinder = GWT.create(CalendarPopUpUiBinder.class);

    interface CalendarPopUpUiBinder extends UiBinder<Widget, CalendarPopUp> {
    }

    @UiField
    HorizontalPanel header;

    @UiField
    PopupPanel calendarPopUp;

    @UiField
    Button okButton;

    @UiField
    Button cancelButton;

    @UiField
    CalendarPanel ecalendar;

    private Date validFromDate;

    private Date validToDate;

    private final HandlerManager handlerManager;

    private int maxNumDays;

    private boolean isGlassEnabled;

    private static DivElement glassPanel;

    private static final String glassStyleName = "gwt-PopupPanelGlass";

    public CalendarPopUp() {
        uiBinder.createAndBindUi(this);
        setUpCancelButton();
        setUpOKButton();
        handlerManager = new HandlerManager(this);
    }

    public CalendarPopUp(final Date fromDate, final Date toDate) {
        this();

        if (fromDate != null && toDate != null) {
            validFromDate = new Date(fromDate.getTime());
            validToDate = new Date(toDate.getTime());
            ecalendar.setFromDate(fromDate);
            ecalendar.setToDate(toDate);
        }
    }

    private void setUpCancelButton() {
        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                ecalendar.setCanceled(true);
                ecalendar.restoreCalendarState();
                ecalendar.removeErrorMessage();
                hide();
                /** Even though we are cancelling the default date is now set in the calendar and will be used **/
                ValueChangeEvent.fire(CalendarPopUp.this, ecalendar);
            }
        });
    }

    private void setUpOKButton() {
        okButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                final Date fromDate = ecalendar.getFromDate();
                final Date toDate = ecalendar.getToDate();
                if (isValid(fromDate, toDate)) {
                    ecalendar.setCanceled(false);
                    hide();
                    ecalendar.saveCalendarState();
                    ValueChangeEvent.fire(CalendarPopUp.this, ecalendar);
                }
            }
        });
    }

    public void setMaxNumberDaysRange(final int maxNumDays) {
        this.maxNumDays = maxNumDays;
    }

    private boolean isValid(final Date fromDate, final Date toDate) {
        if (validFromDate != null && validToDate != null) {
            if (validFromDate.after(fromDate) || validFromDate.after(toDate) || validToDate.before(fromDate)
                    || validToDate.before(toDate)) {
                ecalendar.setErrorMessage("Please select between " + DateTimeUtil.timeToDateString(validFromDate) + " - " + DateTimeUtil.timeToDateString(validToDate),
                        okButton);
                okButton.setEnabled(false);
                return false;
            }
        }
        if (fromDate.after(new Date()) || toDate.after(new Date())) {
            ecalendar.setErrorMessage("Please select date & time in the past only", okButton);
            okButton.setEnabled(false);
            return false;
        }
        if (fromDate.after(toDate)) {
            ecalendar.setErrorMessage("\"End Date/Time\" selected is before the \"Start Date/Time\"", okButton);
            okButton.setEnabled(false);
            return false;
        }
        if (maxNumDays > 0 && CalendarUtil.getDaysBetween(fromDate, toDate) > maxNumDays) {
            ecalendar.setErrorMessage("Please select shorter time range, max days = " + maxNumDays, okButton);
            okButton.setEnabled(false);
            return false;
        }
        if (fromDate.equals(toDate)) {
            ecalendar.setErrorMessage("Identical Start/End date and time selected", okButton);
            okButton.setEnabled(false);
            return false;
        }
        return true;
    }

    public void setDates(final Date fromDate, final Date toDate) {
        assert isValid(fromDate, toDate) : "Passed dates must be valid.";
        ecalendar.setToDate(toDate);
        ecalendar.setFromDate(fromDate);
        ecalendar.saveCalendarState();
    }

    public CalendarPanel getEcalendar() {
        return ecalendar;
    }

    public void show() {
        if (!calendarPopUp.isShowing()) {
            setGlassEnabled(true);
            if (isGlassEnabled) {
                Document.get().getBody().appendChild(glassPanel);
            }
            calendarPopUp.show();
        }
    }

    public void setPopupPosition(final int left, final int top) {
        calendarPopUp.setPopupPosition(left, top);
    }

    public void center() {
        if(isGlassEnabled) {
            Document.get().getBody().appendChild(glassPanel);
        }
        calendarPopUp.center();
    }

    public void hide() {
        if (calendarPopUp.isShowing()) {
            cancelButton.setFocus(false);
            okButton.setFocus(false);
            glassPanel.removeFromParent();
            calendarPopUp.hide();
        }
    }

    public int getPopupLeft() {
        return calendarPopUp.getPopupLeft();
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.HasHandlers#fireEvent(com.google.gwt.event.shared.GwtEvent)
     */
    @Override
    public void fireEvent(final GwtEvent<?> arg0) {
        handlerManager.fireEvent(arg0);
    }

    public void setZIndex(final int zIndex) {
        calendarPopUp.getElement().getStyle().setZIndex(zIndex);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
     */
    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<CalendarPanel> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    static {
        glassPanel = Document.get().createDivElement();
        glassPanel.setClassName(glassStyleName);
        glassPanel.getStyle().setHeight(100, Style.Unit.PCT);
        glassPanel.getStyle().setWidth(100, Style.Unit.PCT);
        glassPanel.getStyle().setPosition(Style.Position.ABSOLUTE);
        glassPanel.getStyle().setLeft(0, Style.Unit.PX);
        glassPanel.getStyle().setTop(0, Style.Unit.PX);
    }

    public void setGlassEnabled(final boolean isEnabled) {
        this.isGlassEnabled = isEnabled;
        glassPanel.getStyle().setZIndex(ZIndexHelper.getHighestZIndex());
    }
}
