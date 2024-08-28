package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.eniq.events.common.client.mvp.BasePresenter;
import com.ericsson.eniq.events.widgets.client.grids.filter.AbstractFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterCategory;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class FilterConfigPresenter<C extends AbstractFilter<T>, T> extends
        BasePresenter<AbstractFilterConfigView<C, T>> {

    private IFilterApplyHandler<C, T> filterApplyHandler;

    public FilterConfigPresenter(final AbstractFilterConfigView<C, T> view, final EventBus eventBus) {
        super(view, eventBus);
        bind();
    }

    public void init(final List<C> filters, final FilterCategory category) {
        clearFilters();
        getView().setCategory(category);
        final int size = filters == null ? 0 : filters.size();
        if (size == 0) {
            getView().addFilter((C) null);
        }
        for (int i = 0; i < size; i++) {
            getView().addFilter(filters.get(i));
        }
        
        checkAndEnableDisableFirstFilter(false);
    }

    public void launchView() {
        getView().show();
    }

    public void clearFilters() {
        getView().clearFilters();

    }

    public void handleApplyClick(final List<C> filters, final FilterCategory category) {
        final List<FilterType> filterTypes = new ArrayList<FilterType>();
        final List<T> filtersList = new ArrayList<T>();
        for (final C c : filters) {
            filterTypes.add(c.getType());
            filtersList.add(c.getFilter());
        }
        filterApplyHandler.onFilterApplyClick(filterTypes, filtersList, category);
    }

    public void handlePlusClick() {
        checkAndEnableDisableFirstFilter(true);

        getView().addFilter((C) null);
    }

    public void handleMinusClick(final int index) {
        //IF there is only one filter then it can not be removed
        if (getView().getFiltersCount() > 1) {
            getView().removeFilter(index);

            checkAndEnableDisableFirstFilter(false);
        }
    }

    private void checkAndEnableDisableFirstFilter(final boolean enabled) {
        if (getView().getFiltersCount() == 1) {
            Widget widget = getView().getFilter(0);

            if (widget instanceof AbstractEditFilterView) {
                ((AbstractEditFilterView) widget).enableRemoveFilter(enabled);
            }
        }
    }

    public void addApplyClickHandler(final IFilterApplyHandler<C, T> filterApplyHandler) {
        this.filterApplyHandler = filterApplyHandler;

    }
}
