package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import static com.ericsson.eniq.events.widgets.client.common.Constants.INVALID_NUMERIC_INT_GRID_FILTER_INFO_MSG;

import java.util.Comparator;
import java.util.List;

import com.ericsson.eniq.events.widgets.client.common.Constants;
import com.ericsson.eniq.events.widgets.client.dialog.MessageDialog;
import com.ericsson.eniq.events.widgets.client.dialog.MessageDialog.DialogType;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterCategory;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterConfigs;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.grids.filter.IntegerFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.configs.FilterConfigPresenter;
import com.ericsson.eniq.events.widgets.client.grids.filter.configs.IFilterApplyHandler;
import com.ericsson.eniq.events.widgets.client.grids.filter.configs.IntegerFilterConfigView;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.event.shared.EventBus;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class IntegerFilterPanel extends NumberFilterPanel<IntegerFilter, Integer> {
    private final FilterConfigPresenter<IntegerFilter, Integer> presenter;

    public IntegerFilterPanel(final IFilterUpdate<IntegerFilter, Integer> filterUpdateHandler, final EventBus eventBus) {
        super(filterUpdateHandler, eventBus);
        validateInteger();
        final IntegerFilterConfigView view = new IntegerFilterConfigView();
        presenter = new FilterConfigPresenter<IntegerFilter, Integer>(view, getEventBus());
        presenter.addApplyClickHandler(new IFilterApplyHandler<IntegerFilter, Integer>() {

            @Override
            public void onFilterApplyClick(final List<FilterType> filterTypes, final List<Integer> filtersList,
                    final FilterCategory category) {
                clearFilterBox();
                onFilterApply2(filterTypes, filtersList, category);
            }
        });
    }

    private void validateInteger() {
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
                        Integer.parseInt(text);
                        filterBox.removeStyleName(getResourceBundle().style().invalidText());
                        filterBox.setTitle("");
                    } catch (NumberFormatException e) {
                        getFilterBox().addStyleName(getResourceBundle().style().invalidText());
                        filterBox.setTitle(Constants.INVALID_NUMERIC_INT_GRID_FILTER_TOOL_TIP);
                    }
                }
            }
        });
    }

    @Override
    public Comparator<Integer> getComparator() {
        return new Comparator<Integer>() {

            @Override
            public int compare(final Integer o1, final Integer o2) {
                final int result = o1 - o2;
                return result;
            }
        };
    }

    @Override
    public SafeHtml getFilterHTML(final String filter, final FilterType filterType) {
        return getImageTemplate(filterType, filter);
    }

    @Override
    protected void onConfigurationClick() {
        final FilterConfigs<IntegerFilter, Integer> configs = getFilterConfigs();
        final List<IntegerFilter> filters = (configs == null || configs.getFilters().size() == 0) ? null : configs
                .getFilters();
        presenter.init(filters, getCategory());
        presenter.launchView();
    }

    @Override
    public void parseFilterText(final String text, final List<Integer> filters, final List<FilterType> types) {
        try {
            filters.add(Integer.parseInt(text.trim()));
            types.add(lastSelectedType);
        } catch (final Exception e) {
            final MessageDialog infoDialog = new MessageDialog("Validation", INVALID_NUMERIC_INT_GRID_FILTER_INFO_MSG,
                    DialogType.INFO);
            infoDialog.setGlassEnabled(true);
            infoDialog.center();
        }
    }

    @Override
    protected FilterConfigs<IntegerFilter, Integer> createFilterConfigs(final List<FilterType> filterTypes,
            final List<Integer> filters, final FilterCategory category) {
        final FilterConfigs<IntegerFilter, Integer> configs = new FilterConfigs<IntegerFilter, Integer>(category);
        for (int i = 0; i < filters.size(); i++) {
            configs.addFilter(new IntegerFilter(filterTypes.get(i), filters.get(i)));
        }
        return configs;
    }

}
