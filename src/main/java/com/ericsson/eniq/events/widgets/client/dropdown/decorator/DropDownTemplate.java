package com.ericsson.eniq.events.widgets.client.dropdown.decorator;

import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface DropDownTemplate extends SafeHtmlTemplates {
    @Template("<div onclick=\"\" __idx=\"{0}\" class=\"{1}\" >{2}</div>")
    SafeHtml div(int idx, String classes, SafeHtml cellContents);

    @Template("<div class=\"{0}\"><div class=\"{1}\"></div><div class=\"{2}\" >{3}</div></div>")
    SafeHtml separator(String itemStyle, String lineStyle, String labelStyle, SafeHtml cellContents);
}
