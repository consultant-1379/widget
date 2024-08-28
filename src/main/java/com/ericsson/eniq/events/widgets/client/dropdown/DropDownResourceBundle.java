package com.ericsson.eniq.events.widgets.client.dropdown;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface DropDownResourceBundle extends ClientBundle {

    @Source("images/dropdownArrowButton_NoBorder.png")
    ImageResource iconNormal();

    @Source("images/dropdownArrowButtonhover_NoBorder.png")
    ImageResource iconHover();

    @Source("images/dropdownArrowButtonClicked_NoBorder.png")
    ImageResource iconClick();

    @Source("css/DropDown.css")
    DropDownStyle dropDownStyle();

    @Source("css/DropDownMenu.css")
    DropDownMenuStyle menuStyle();

    @Source("css/DropDownPanel.css")
    DropDownPanelStyle popupStyle();

    @Source("css/DropDownItem.css")
    DropDownItemStyle itemStyle();

    interface DropDownStyle extends CssResource {
        String dropdown();

        String disabled();

        String icon();

        String content();

        String container();

        String wrapperContainer();

        String open();

        String defaultText();
    }

    interface DropDownMenuStyle extends CssResource {
        String menu();

        String disabled();

        String icon();

        String content();

        String container();

        String wrapperContainer();

        String open();

        String focus();
    }

    interface DropDownPanelStyle extends CssResource {
        String popupPanel();

        String reverse();

        String popupContent();
    }

    interface DropDownItemStyle extends CssResource {
        String item();

        String selected();

        String marked();

        String seperatorLabel();

        String seperatorItem();

        String separatorLine();
    }
}
