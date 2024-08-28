package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * 
 *  @author eorstap
 * @since 2013
 *
 */
public class CustomImageCell extends AbstractCell<FilterType> {

    interface Template extends SafeHtmlTemplates {
        @Template("<div class='{0} {1}'></div>")
        SafeHtml div(String url, String cellStyle);
    }

    private final String cellStyle;

    private static Template template = GWT.create(Template.class);

    /**
     * Construct a new ImageCell.
     */
    public CustomImageCell(final String cellStyle) {
        this.cellStyle = cellStyle;
    }

    @Override
    public void render(final com.google.gwt.cell.client.Cell.Context context, final FilterType value,
            final SafeHtmlBuilder sb) {
        if (value != null) {
            sb.append(template.div(value.getStyle(), cellStyle));
        }

    }
}
