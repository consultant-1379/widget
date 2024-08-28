package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import java.util.Comparator;
import java.util.List;

import com.ericsson.eniq.events.widgets.client.grids.filter.BooleanFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterCategory;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterConfigs;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;

/**
 * 
 * @author ekurshi
 * @since 2013
 * 
 */
public class BooleanFilterPanel extends FilterPanel<BooleanFilter, Boolean> {
    public BooleanFilterPanel(final IFilterUpdate<BooleanFilter, Boolean> filterUpdateHandler, final EventBus eventBus) {
        super(filterUpdateHandler, eventBus);
    }

    @Override
    protected void addConfigButton() {
    }

    @Override
    public Comparator<Boolean> getComparator() {
        return new Comparator<Boolean>() {

            @Override
            public int compare(final Boolean o1, final Boolean o2) {
                return o1.compareTo(o2);
            }
        };
    }

    @Override
    public void parseFilterText(final String text, final List<Boolean> filters, final List<FilterType> types) {
        try {
            filters.add(Boolean.parseBoolean(text));
            types.add(FilterType.EQUAL);
        } catch (final Exception e) {
            Window.alert("Invalid filter. Valid values are true/false");
        }
    }

    @Override
    public void setWidth(final int width) {
        final int boxWidth = Math.max(0, width - BORDER_WIDTH);
        getFilterBox().setWidth(boxWidth + "px");
        filterAppliedButton.setWidth(boxWidth);
    }

    @Override
    protected FilterConfigs<BooleanFilter, Boolean> createFilterConfigs(final List<FilterType> filterTypes,
            final List<Boolean> filters, final FilterCategory category) {
        final FilterConfigs<BooleanFilter, Boolean> configs = new FilterConfigs<BooleanFilter, Boolean>(category);
        for (int i = 0; i < filters.size(); i++) {
            configs.addFilter(new BooleanFilter(filterTypes.get(i), filters.get(i)));
        }
        return configs;
    }
}
