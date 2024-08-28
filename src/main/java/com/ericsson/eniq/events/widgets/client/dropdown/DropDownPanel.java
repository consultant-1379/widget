package com.ericsson.eniq.events.widgets.client.dropdown;

import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class DropDownPanel {

    private static final int OFFSET_TOP = 5;

    private DropDownPopup popup;

    private final DropDownResourceBundle.DropDownPanelStyle popupStyle;

    private final FlowPanel popupParent;

    public DropDownPanel(DropDownResourceBundle resourceBundle, FlowPanel popupParent) {
        this(resourceBundle, popupParent, null);
    }

    public DropDownPanel(DropDownResourceBundle resourceBundle, FlowPanel popupParent,
            CloseHandler<DropDownPopup> closeHandler) {
        this.popupParent = popupParent;

        popupStyle = resourceBundle.popupStyle();
        popupStyle.ensureInjected();

        if (closeHandler != null) {
            getPopup().addCloseHandler(closeHandler);
        }

        // Ensure that styles for items are also initialised
        resourceBundle.itemStyle().ensureInjected();
    }

    public void setContent(final Widget content) {
        getPopup().setWidget(content);
    }

    public void show(final Widget relativeToWidget) {
        final int width = relativeToWidget.getOffsetWidth();
        getPopup().getElement().getStyle().setPropertyPx("minWidth", width);
        int zIndex= ZIndexHelper.getHighestZIndex();
        getPopup().getStyleElement().getStyle().setZIndex(zIndex);

        getPopup().setPopupPositionAndShow(new DropDownPopup.PositionCallback() {
            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                // TODO: Fix positioning, to be correct after moved to DropDownPopup
                // position(relativeToWidget, offsetWidth, offsetHeight);
                if (popupParent != null) {
                    popupSizeAndPosition(relativeToWidget, 0, 20);
                } else {
                    popupSizeAndPosition(relativeToWidget, relativeToWidget.getAbsoluteLeft(),
                            relativeToWidget.getAbsoluteTop() + 20);
                }
            }

            /**
             * Set the size and position of the popup(dropdown). This method takes the size of the window
             * used to display the dropdown. If it's too small to show the full size, the dropdown will
             * resize and show a scrollbar.
             * @param relativeObject
             * @param offsetWidth
             * @param offsetHeight
             */
            private void popupSizeAndPosition(final Widget relativeObject, final int offsetWidth, final int offsetHeight){
                getPopup().setHeight("");//reset the size...

                //get the length of the relativeObject's popup/the dropdown.
                int popupHeight = getPopup().getOffsetHeight();
                
                //Not used yet...
                //int popupWidth  = getPopup().getOffsetWidth();

                //get the height of the display area in the browser window.
                int browserHeight = Window.getClientHeight();

                //get the location of the bottom of the dropdown (from the top of the browser).
                int popupBottom = relativeObject.getAbsoluteTop() + offsetHeight + popupHeight;

                //if the location is > than the window height, then shorten the height of the dropdown.
                if(popupBottom + 10 > browserHeight){
                    int popupNewHeight = browserHeight - (relativeObject.getAbsoluteTop() + offsetHeight + 20);
                    String height = String.valueOf(popupNewHeight)+"px";
                    getPopup().setHeight(height);
                }
                getPopup().setPopupPosition(offsetWidth, offsetHeight);
            }

            private void position(Widget relativeObject, int offsetWidth, int offsetHeight) {
                int textBoxOffsetWidth = relativeObject.getOffsetWidth();
                // Compute the difference between the popup's width and the
                // textbox's width
                final int offsetWidthDiff = offsetWidth - textBoxOffsetWidth;

                int left;

                // Left-align the popup.
                left = 0;

                // If the suggestion popup is not as wide as the text box, always align to
                // the left edge of the text box. Otherwise, figure out whether to
                // left-align or right-align the popup.
                if (offsetWidthDiff > 0) {
                    // Make sure scrolling is taken into account, since
                    // box.getAbsoluteLeft() takes scrolling into account.
                    final int windowRight = Window.getClientWidth() + Window.getScrollLeft();
                    final int windowLeft = Window.getScrollLeft();

                    // Distance from the left edge of the text box to the right edge
                    // of the window
                    final int distanceToWindowRight = windowRight - left;

                    // Distance from the left edge of the text box to the left edge of the
                    // window
                    final int distanceFromWindowLeft = left - windowLeft;

                    // If there is not enough space for the overflow of the popup's
                    // width to the right of hte text box, and there IS enough space for the
                    // overflow to the left of the text box, then right-align the popup.
                    // However, if there is not enough space on either side, then stick with
                    // left-alignment.
                    if (distanceToWindowRight < offsetWidth && distanceFromWindowLeft >= offsetWidthDiff) {
                        // Align with the right edge of the text box.
                        left -= offsetWidthDiff;
                    }
                }

                // Calculate top position for the popup
                int top = relativeObject.getOffsetHeight();

                // Make sure scrolling is taken into account, since
                // box.getAbsoluteTop() takes scrolling into account.
                final int windowTop = Window.getScrollTop();
                final int windowBottom = Window.getScrollTop() + Window.getClientHeight();

                // Distance from the top edge of the window to the top edge of the
                // text box
                final int distanceFromWindowTop = top - windowTop;

                // Distance from the bottom edge of the window to the bottom edge of
                // the text box
                int distanceToWindowBottom = windowBottom - (top + relativeObject.getOffsetHeight());

                // If there is not enough space for the popup's height below the text
                // box and there IS enough space for the popup's height above the text
                // box, then then position the popup above the text box. However, if there
                // is not enough space on either side, then stick with displaying the
                // popup below the text box.

                top += OFFSET_TOP;
                getPopup().addStyleName(popupStyle.reverse());

                if (distanceToWindowBottom < offsetHeight && distanceFromWindowTop >= offsetHeight) {
                    top -= offsetHeight;
                } else {
                    // Position above the text box
                    top += relativeObject.getOffsetHeight();
                    top -= OFFSET_TOP * 2;

                    getPopup().removeStyleName(popupStyle.reverse());
                }

                getPopup().setPopupPosition(left, top);
            }
        });
    }

    public void hide() {
        getPopup().hide();
    }

    public void setAutohidePartner(final Widget partner) {
        getPopup().addAutoHidePartner(partner.getElement());
    }

    public boolean isShowing() {
        return getPopup().isShowing();
    }

    public void setMaxHeight(final int maxHeight) {
        getPopup().getContainerElement().getStyle().setPropertyPx("maxHeight", maxHeight);
    }

    private DropDownPopup getPopup() {
        if (popup == null) {
            popup = new DropDownPopup(popupParent);
            popup.setStyleName(popupStyle.popupPanel());
        }

        return popup;
    }

    public int getAbsoluteTop() {
        return popupParent.getAbsoluteTop();
    }

    public int getAbsoluteLeft() {
        return popupParent.getAbsoluteLeft();
    }

    public void setZIndex(int zIndex) {
        getPopup().getElement().getStyle().setZIndex(zIndex);
    }

}
