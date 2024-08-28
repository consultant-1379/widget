package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.ericsson.eniq.events.widgets.client.calendar.CalendarPanel;
import com.ericsson.eniq.events.widgets.client.calendar.CalendarPopUp;
import com.ericsson.eniq.events.widgets.client.grids.filter.DateFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterCategory;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterConfigs;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.web.bindery.event.shared.EventBus;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class DateFilterPanel extends FilterPanel<DateFilter, Date> {

    private final Button filterButton;

    private CalendarPopUp calendarPopUp;

    private Date validFromDate;
    
    private Date validToDate;
    
    public DateFilterPanel(final IFilterUpdate<DateFilter, Date> filterUpdateHandler, final EventBus eventBus) {
        super(filterUpdateHandler, eventBus);
        filterButton = new Button("Filter Time", new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                getCalendarPopUp().setGlassEnabled(true);
                getCalendarPopUp().setZIndex(ZIndexHelper.getHighestZIndex());
                getCalendarPopUp().center();
            }
        });
        addWidget(filterButton);
    }

    public DateFilterPanel(final IFilterUpdate<DateFilter, Date> filterUpdateHandler, final Date fromDate, final Date toDate, final EventBus eventBus) {
        this(filterUpdateHandler, eventBus);
        
        this.validFromDate = fromDate;
        this.validToDate = toDate;
    }
    
    private CalendarPopUp getCalendarPopUp() {
        if (calendarPopUp == null) {
            
            calendarPopUp = new CalendarPopUp(validFromDate, validToDate);
            calendarPopUp.addValueChangeHandler(new ValueChangeHandler<CalendarPanel>() {

                @Override
                public void onValueChange(final ValueChangeEvent<CalendarPanel> event) {
                    if (!event.getValue().isCanceled()) {//Ok is pressed
                        final Date toDate = getCalendarPopUp().getEcalendar().getToDate();
                        final Date fromDate = getCalendarPopUp().getEcalendar().getFromDate();

                        final List<FilterType> typeList = new ArrayList<FilterType>();
                        final List<Date> filterList = new ArrayList<Date>();
                        typeList.add(FilterType.GREATER_THAN_EQUAL);
                        filterList.add(fromDate);
                        typeList.add(FilterType.LESS_THAN_EQUAL);
                        filterList.add(toDate);
                        setFilterAppliedButtonVisible(true);
                        onFilterApply2(typeList, filterList, getCategory());
                    } else {//cancel is pressed
                        clearFilter();
                    }
                }
            });
        }
        return calendarPopUp;
    }

    @Override
    protected void clearFilterBox() {
    }

    @Override
    protected void init() {
    }

    @Override
    protected void onFilterAppliedClick() {
        getCalendarPopUp().center();
    }

    @SuppressWarnings("serial")
    @Override
    protected void onFilterApply2(final List<FilterType> typeList, final List<Date> filterList,
            final FilterCategory category) {
        setFilterAppliedButtonVisible(!typeList.isEmpty());
        applyFilter(typeList, filterList, category);
        if (filterList.size() == 2) {
            final DateTimeFormat formatter = DateTimeFormat.getFormat(DateFilter.DD_MM_YY_MM_HH);
            final String filterText = formatter.format(filterList.get(0)) + " to "
                    + formatter.format(filterList.get(1));
            filterAppliedButton.setHTML(getFilterHTML(filterText, null));
            getHoverPopup().configure("1 Filter Applied", new ArrayList<String>() {
                {
                    add(filterText);
                }
            });
        }
    }

    @Override
    public Comparator<Date> getComparator() {
        return new Comparator<Date>() {

            @Override
            public int compare(final Date o1, final Date o2) {
                return o1.compareTo(o2);
            }
        };
    }

    @Override
    public void setWidth(final int width) {
        final int boxWidth = Math.max(0, width - BORDER_WIDTH);
        filterButton.setWidth(boxWidth + "px");
        filterAppliedButton.setWidth(boxWidth);
    }

    @Override
    protected void setFilterAppliedButtonVisible(final boolean visible) {
        filterAppliedButton.setVisible(visible);
        filterButton.setVisible(!visible);
    }

    @Override
    public void parseFilterText(final String text, final List<Date> filters, final List<FilterType> types) {

    }

    @Override
    protected FilterConfigs<DateFilter, Date> createFilterConfigs(final List<FilterType> filterTypes,
            final List<Date> filters, final FilterCategory category) {
        final FilterConfigs<DateFilter, Date> configs = new FilterConfigs<DateFilter, Date>(category);
        for (int i = 0; i < filters.size(); i++) {
            configs.addFilter(new DateFilter(filterTypes.get(i), filters.get(i)));
        }
        return configs;
    }
}
