package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.grids.filter.StringFilter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class StringEditFilterView extends AbstractEditFilterView<StringFilter, String> {
    interface StringTemplate extends SafeHtmlTemplates {
        @Template("<span title='{0}'>{0}</span>")
        SafeHtml div(String filter);
    }

    private static final StringTemplate STRING_TEMPLATE = GWT.create(StringTemplate.class);

    public StringEditFilterView(final String filter) {
        super();
        setFilterAppliedButtonVisible(filter != null);
        editFilterTextBox.setWidth(FILTER_CONTAINER_WIDTH - 14 - 2 + "px");
        filterBtn.setWidth(FILTER_CONTAINER_WIDTH);
        final String nonNullStr = filter == null ? "" : String.valueOf(filter);
        setButtonText(nonNullStr);
        getEditFilterTextBox().setText(nonNullStr);
    }

    @Override
    public void setButtonText(final String text) {
        filterBtn.setHTML(STRING_TEMPLATE.div(text));
    }

    @Override
    public StringFilter getFilter() {
        return new StringFilter(FilterType.EQUAL, getEditFilterTextBox().getText().trim());
    }
}