package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import java.util.Comparator;
import java.util.List;

import com.ericsson.eniq.events.widgets.client.grids.filter.FilterCategory;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterConfigs;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.grids.filter.StringFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.configs.FilterConfigPresenter;
import com.ericsson.eniq.events.widgets.client.grids.filter.configs.IFilterApplyHandler;
import com.ericsson.eniq.events.widgets.client.grids.filter.configs.StringFilterConfigView;
import com.google.web.bindery.event.shared.EventBus;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class StringFilterPanel extends FilterPanel<StringFilter, String> {
    private final FilterConfigPresenter<StringFilter, String> presenter;

    public StringFilterPanel(final IFilterUpdate<StringFilter, String> filterUpdateHandler, final EventBus eventBus) {
        super(filterUpdateHandler, eventBus);
        final StringFilterConfigView view = new StringFilterConfigView();
        presenter = new FilterConfigPresenter<StringFilter, String>(view, getEventBus());
        presenter.addApplyClickHandler(new IFilterApplyHandler<StringFilter, String>() {

            @Override
            public void onFilterApplyClick(final List<FilterType> filterTypes, final List<String> filtersList,
                    final FilterCategory category) {
                clearFilterBox();
                onFilterApply2(filterTypes, filtersList, category);
            }

        });
    }

    @Override
    public Comparator<String> getComparator() {
        return new Comparator<String>() {

            @Override
            public int compare(final String o1, final String o2) {
                return o1.compareTo(o2);
            }
        };
    }

    @Override
    protected void onFilterAppliedClick() {
        onConfigurationClick();
    }

    @Override
    protected void onConfigurationClick() {
        final FilterConfigs<StringFilter, String> configs = getFilterConfigs();
        final List<StringFilter> filters = (configs == null || configs.getFilters().size() == 0) ? null : configs
                .getFilters();
        final FilterCategory category = (configs == null) ? getCategory() : configs.getCategory();
        presenter.init(filters, category);
        presenter.launchView();
    }

    @Override
    public void parseFilterText(final String text, final List<String> filters, final List<FilterType> types) {
        filters.add(text);
        types.add(FilterType.EQUAL);
    }

    @Override
    protected FilterConfigs<StringFilter, String> createFilterConfigs(final List<FilterType> filterTypes,
            final List<String> filters, final FilterCategory category) {
        final FilterConfigs<StringFilter, String> configs = new FilterConfigs<StringFilter, String>(category);
        for (int i = 0; i < filters.size(); i++) {
            configs.addFilter(new StringFilter(filterTypes.get(i), filters.get(i)));
        }
        return configs;
    }

}
