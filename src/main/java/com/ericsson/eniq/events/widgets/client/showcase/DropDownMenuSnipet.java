package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDown;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDownMenu;
import com.ericsson.eniq.events.widgets.client.dropdown.StringDropDownItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
public class DropDownMenuSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);
    private boolean enabled = true;

    public FlowPanel createWidget(){
        final List<StringDropDownItem> menuValues = prepareDropDownList();

        VerticalPanel holderPanel = new VerticalPanel();

        final DropDownMenu dropDownMenu = new DropDownMenu();
        dropDownMenu.setWidth("190px");
        dropDownMenu.update(menuValues);
        dropDownMenu.setValue(new StringDropDownItem("Value 2"));
        dropDownMenu.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        holderPanel.add(dropDownMenu);

        final DropDown<StringDropDownItem> testDropdown = new DropDown<StringDropDownItem>();
        testDropdown.update(menuValues);
        testDropdown.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        final Button enable = new Button("Enable/Disable");
        enable.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                testDropdown.setEnabled(enabled);
                enabled=!enabled;

            }
        });

        holderPanel.add(testDropdown);
        testDropdown.setEnabled(enabled);

        holderPanel.add(enable);

        final String source = wrapCode(sourceBundle.DropDown().getText());
        return wrapWidgetAndSource(holderPanel, source);
    }
    private List<StringDropDownItem> prepareDropDownList() {
        final List<StringDropDownItem> menuValues = new ArrayList<StringDropDownItem>();
        menuValues.add(new StringDropDownItem("Separator 1") {
            @Override
            public boolean isSeparator() {
                return true;
            }
        });
        menuValues.add(new StringDropDownItem("Value 1"));
        menuValues.add(new StringDropDownItem("Value 2"));
        menuValues.add(new StringDropDownItem("Value 3"));
        menuValues.add(new StringDropDownItem("Separator 2") {
            @Override
            public boolean isSeparator() {
                return true;
            }
        });

        menuValues.add(new StringDropDownItem("Value 4"));
        menuValues.add(new StringDropDownItem("Value 5 Long Text Example - no wrapping"));
        menuValues.add(new StringDropDownItem("Value 6"));
        menuValues.add(new StringDropDownItem("Value 7"));
        menuValues.add(new StringDropDownItem("Value 8"));
        menuValues.add(new StringDropDownItem("Value 9"));
        menuValues.add(new StringDropDownItem("Value 10"));
        menuValues.add(new StringDropDownItem("Value 11"));
        menuValues.add(new StringDropDownItem("Value 12"));
        menuValues.add(new StringDropDownItem("Value 13"));
        menuValues.add(new StringDropDownItem("Value 14"));
        menuValues.add(new StringDropDownItem("Value 15"));
        menuValues.add(new StringDropDownItem("Value 16"));
        menuValues.add(new StringDropDownItem("Value 17"));
        menuValues.add(new StringDropDownItem("Value 18"));
        menuValues.add(new StringDropDownItem("Value 19"));
        menuValues.add(new StringDropDownItem("Value 20"));

        return menuValues;
    }
}
