package com.ericsson.eniq.events.widgets.client.drill;

import com.ericsson.eniq.events.common.client.CommonConstants;
import com.ericsson.eniq.events.widgets.client.dialog.MessageDialogResourceBundle;
import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;

public class DrillDialog extends DialogBox {

    private static final int ARROW_OFFSET = 20;

    private static final MessageDialogResourceBundle messageDialogResources = GWT.create(MessageDialogResourceBundle.class);

    private static final DrillBoxResourceBundle drillDialogResources = GWT.create(DrillBoxResourceBundle.class);

    public final DrillDialogPanel panel;

    public static DrillDialog get() {
        return new DrillDialog();
    }

    public void setElementId(final String id) {
        getElement().setId(id);
    }

    public DrillDialog() {
        super(true, true); //Set as False, true if you wish to disable click Out to close event
        setAnimationEnabled(false);
        panel = createPanel();
        messageDialogResources.css().ensureInjected();
        setStyleName(messageDialogResources.css().dialogBox());
        addStyleName(messageDialogResources.css().includeBorder());
        setElementId(CommonConstants.SELENIUM_TAG+"DrillDialog");
        setWidget(panel);
    }

    public DrillDialogPanel createPanel() {
        return new DrillDialogPanel();
    }

    public void addDrillOption(final DrillCategoryType category, final IDrillCallback callback) {
        panel.addDrillOption(category, new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hide();
                callback.onDrillDownSelected(category.getId());
            }
        });
    }

    public void addChartByOption(final ChartByOptionLink chartByOption) {
        panel.addChartByOption(chartByOption);
    }

    public void show(final Point point) {
        super.show();
        alignDrillDialog(point);
    }

    public void alignDrillDialog(Point point) {
        int width = getOffsetWidth();                                       //Gets Width
        int halfWidth = width / 2;                                          //Halves the Width              was 2
        int top = point.y - (getOffsetHeight() + ARROW_OFFSET);             //Y access = y location -(The Height + Arrow Offset)
        int left = point.x - 37;// -halfWidth;                              // Set the arrow =  location (Center)

        panel.removeStyleName(drillDialogResources.css().bottom());

        if (!showTop(top)) {
            top = point.y + ARROW_OFFSET;
            panel.addStyleName(drillDialogResources.css().bottom());
        }
        if (moveLeft(left, width)) {
            left = point.x - halfWidth;
            left -= (halfWidth - 35);
            panel.addStyleName(drillDialogResources.css().left());
        } else if (moveRight(left, width)) {
            left = point.x - halfWidth;
            left += (halfWidth - 10);
            panel.addStyleName(drillDialogResources.css().right());
        } else {
            panel.addStyleName(drillDialogResources.css().center());
        }

        setPopupPosition(left, top);
        setZIndex();
    }


    /**
     * @param left
     * @param width
     * @return
     */
    private boolean moveRight(int left, int width) {
        if (left < 0) {
            return true;
        }
        return false;
    }

    private boolean moveLeft(int left, int width) {
        if ((left + width) > Window.getClientWidth()) {
            return true;
        }
        return false;
    }

    private boolean showTop(int top) {
        return top > 0;
    }

    private void setZIndex() {
        getElement().getStyle().setZIndex(ZIndexHelper.getHighestZIndex());
    }

    public void setPopUpTitle(String text) {
        panel.setDialogTitle(text);
    }

}
