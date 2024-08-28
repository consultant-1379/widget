package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import static com.ericsson.eniq.events.widgets.client.common.Constants.INVALID_NUMERIC_GRID_FILTER_INFO_MSG;

import java.util.Comparator;
import java.util.List;

import com.ericsson.eniq.events.widgets.client.common.Constants;
import com.ericsson.eniq.events.widgets.client.dialog.MessageDialog;
import com.ericsson.eniq.events.widgets.client.dialog.MessageDialog.DialogType;
import com.ericsson.eniq.events.widgets.client.grids.filter.DoubleFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterCategory;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterConfigs;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.grids.filter.configs.DoubleFilterConfigView;
import com.ericsson.eniq.events.widgets.client.grids.filter.configs.FilterConfigPresenter;
import com.ericsson.eniq.events.widgets.client.grids.filter.configs.IFilterApplyHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.event.shared.EventBus;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class DoubleFilterPanel extends NumberFilterPanel<DoubleFilter, Double> {
    private final FilterConfigPresenter<DoubleFilter, Double> presenter;

    public DoubleFilterPanel(final IFilterUpdate<DoubleFilter, Double> filterUpdateHandler, final EventBus eventBus) {
        super(filterUpdateHandler, eventBus);
        validateDouble();
        final DoubleFilterConfigView view = new DoubleFilterConfigView();
        presenter = new FilterConfigPresenter<DoubleFilter, Double>(view, getEventBus());
        presenter.addApplyClickHandler(new IFilterApplyHandler<DoubleFilter, Double>() {

            @Override
            public void onFilterApplyClick(final List<FilterType> filterTypes, final List<Double> filtersList,
                    final FilterCategory category) {
                clearFilterBox();
                onFilterApply2(filterTypes, filtersList, category);
            }
        });
    }

    private void validateDouble() {
        final TextBox filterBox = getFilterBox();
        filterBox.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                String text = filterBox.getText();
                if (text == null || text.trim().equals("")) {
                    filterBox.removeStyleName(getResourceBundle().style().invalidText());
                    filterBox.setTitle("");
                } else {
                    try {
                        Double.parseDouble(text);
                        filterBox.removeStyleName(getResourceBundle().style().invalidText());
                        filterBox.setTitle("");
                    } catch (NumberFormatException e) {
                        getFilterBox().addStyleName(getResourceBundle().style().invalidText());
                        filterBox.setTitle(Constants.INVALID_NUMERIC_GRID_FILTER_TOOL_TIP);
                    }
                }
            }
        });
    }

    @Override
    public Comparator<Double> getComparator() {
        return new Comparator<Double>() {

            @Override
            public int compare(final Double o1, final Double o2) {
                return o1.compareTo(o2);
            }
        };
    }

    @Override
    protected void onConfigurationClick() {
        final FilterConfigs<DoubleFilter, Double> configs = getFilterConfigs();
        final List<DoubleFilter> filters = (configs == null || configs.getFilters().size() == 0) ? null : configs
                .getFilters();
        presenter.init(filters, getCategory());
        presenter.launchView();
    }

    @Override
    public void parseFilterText(final String text, final List<Double> filters, final List<FilterType> types) {
        try {
            filters.add(Double.parseDouble(text.trim()));
            types.add(lastSelectedType);
        } catch (final Exception e) {
            final MessageDialog infoDialog = new MessageDialog("Validation", INVALID_NUMERIC_GRID_FILTER_INFO_MSG,
                    DialogType.INFO);
            infoDialog.setGlassEnabled(true);
            infoDialog.center();
        }
    }

    @Override
    protected FilterConfigs<DoubleFilter, Double> createFilterConfigs(final List<FilterType> filterTypes,
            final List<Double> filters, final FilterCategory category) {
        final FilterConfigs<DoubleFilter, Double> configs = new FilterConfigs<DoubleFilter, Double>(category);
        for (int i = 0; i < filters.size(); i++) {
            configs.addFilter(new DoubleFilter(filterTypes.get(i), filters.get(i)));
        }
        return configs;
    }

}
