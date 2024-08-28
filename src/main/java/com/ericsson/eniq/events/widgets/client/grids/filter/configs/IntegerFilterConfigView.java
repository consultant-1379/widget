package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.grids.filter.IntegerFilter;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class IntegerFilterConfigView extends AbstractFilterConfigView<IntegerFilter, Integer> {

    @Override
    public AbstractEditFilterView<IntegerFilter, Integer> addFilterWidget(final IntegerFilter filter) {
        final Integer filterValue = filter == null ? null : filter.getFilter();
        final FilterType selectedType = filter == null ? FilterType.values()[0] : filter.getType();
        final IntegerEditFilterView editFilterView = new IntegerEditFilterView(FilterType.values(), selectedType,
                filterValue);
        filterContainer.add(editFilterView);
        return editFilterView;
    }
    
    @Override
    protected void initHeader() {
        setHeaderText("Numeric Filter (Integer)");
    }
}
