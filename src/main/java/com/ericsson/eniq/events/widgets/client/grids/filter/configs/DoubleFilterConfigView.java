package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import com.ericsson.eniq.events.widgets.client.grids.filter.DoubleFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class DoubleFilterConfigView extends AbstractFilterConfigView<DoubleFilter, Double> {

    @Override
    public AbstractEditFilterView<DoubleFilter, Double> addFilterWidget(final DoubleFilter filter) {
        final Double filterValue = filter == null ? null : filter.getFilter();
        final FilterType selectedType = filter == null ? FilterType.values()[0] : filter.getType();
        final DoubleEditFilterView editFilterView = new DoubleEditFilterView(FilterType.values(), selectedType,
                filterValue);
        filterContainer.add(editFilterView);
        return editFilterView;
    }

    @Override
    protected void initHeader() {
        setHeaderText("Numeric Filter");
        
    }
}
