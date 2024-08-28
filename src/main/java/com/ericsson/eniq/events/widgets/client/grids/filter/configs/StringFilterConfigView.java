package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.eniq.events.widgets.client.dropdown.StringDropDownItem;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterCategory;
import com.ericsson.eniq.events.widgets.client.grids.filter.StringFilter;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class StringFilterConfigView extends AbstractFilterConfigView<StringFilter, String> {

    public StringFilterConfigView() {
        categoryDropDown.setVisible(true);
        final List<StringDropDownItem> items = new ArrayList<StringDropDownItem>();
        items.add(new StringDropDownItem("Contains Any"));
        items.add(new StringDropDownItem("Contains All"));
        updateCategories(items);
        setCategory(FilterCategory.MATCH_ANY);
    }

    @Override
    protected void initHeader() {
        setHeaderText("String Filter");
        
    }
    
    @Override
    public AbstractEditFilterView<StringFilter, String> addFilterWidget(final StringFilter filter) {
        final String filterValue = filter == null ? null : filter.getFilter();
        final StringEditFilterView editFilterView = new StringEditFilterView(filterValue);
        filterContainer.add(editFilterView);
        return editFilterView;
    }

    private String fromCategoryToString(final FilterCategory category) {
        if (category == FilterCategory.MATCH_ALL) {
            return "Contains All";
        } else {
            return "Contains Any";
        }
    }

    private FilterCategory fromStringToCategory(final String category) {
        if (category.equals("Contains All")) {
            return FilterCategory.MATCH_ALL;
        } else {
            return FilterCategory.MATCH_ANY;
        }
    }

    @Override
    protected FilterCategory getSelectedCategory() {
        return fromStringToCategory(categoryDropDown.getValue().getValue());
    }

    @Override
    public void setCategory(final FilterCategory category) {
        categoryDropDown.setValue(new StringDropDownItem(fromCategoryToString(category)));
    }
}
