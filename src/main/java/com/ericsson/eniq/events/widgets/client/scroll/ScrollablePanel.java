package com.ericsson.eniq.events.widgets.client.scroll;

import static com.ericsson.eniq.events.widgets.client.scroll.ScrollConstants.*;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author evyagrz
 * @author ealeerm - Alexey Ermykin
 * @author ekurshi
 * @since 02 2012
 */
class ScrollablePanel extends FocusPanel implements IScrollable {

    protected final FlowPanel contentPanel = new FlowPanel();

    private int maxVertThumbPosition = 0;

    private int maxHorzThumbPosition = 0;

    private double vScrollerFactor;

    private double hScrollerFactor;

    private int maxVContentScrollPosition;

    private int pageHeight;// visibleHeight

    private int vThumbHeight;

    private int innerVTrackLength;// visibleHeight-2*arrowHeightWithBorder-trackBorderHeight*2;

    private int innerHTrackLength;// visibleWidth-2*arrowWidthWithBorder-trackBorderHeight*2;

    private boolean verticalScrollNeeded;

    private boolean horizontalScrollNeeded;

    private boolean showHorizontalScroll;

    private int pageWidth;// visibleWidth

    private int hThumbWidth;

    private boolean arrowEnabled = true;

    private int minimumThumbSize = MIN_THUMB_SIZE;

    private int maxHContentScrollPosition;

    private int cornerSize;

    private boolean alwaysVisible = true;

    @Override
    public int getPageHeight() {
        return pageHeight;
    }

    void setMinimumThumbSize(final int height) {
        minimumThumbSize = height > minimumThumbSize ? height : minimumThumbSize;
    }

    ScrollablePanel() {
        super.setWidget(contentPanel);
        getElement().getStyle().setFloat(Float.LEFT);
        getElement().getStyle().setProperty("outline", "none");
        contentPanel.getElement().getStyle().setOverflow(Overflow.HIDDEN);
    }

    /**
     * Add native scroll handler to scrollable element
     */
    @Override
    public HandlerRegistration addScrollHandler(final ScrollHandler handler) {
        return contentPanel.addDomHandler(handler, ScrollEvent.getType());
    }

    @Override
    public int getMaximumVerticalScrollPosition() {
        return maxVContentScrollPosition;
    }

    @Override
    public boolean isArrowEnabled() {
        return arrowEnabled;
    }

    @Override
    public int getMinimumVerticalScrollPosition() {
        return MIN_VERTICAL_CONTENT_POSITION;
    }

    @Override
    public int getVerticalScrollPosition() {
        return DOM.getElementPropertyInt(contentPanel.getElement(), "scrollTop");
    }

    @Override
    public void setVerticalScrollPosition(final int position) {
        DOM.setElementPropertyInt(contentPanel.getElement(), "scrollTop", position);
    }

    @Override
    public void add(final IsWidget child) {
        //can't add more than one widget, setWidget() is used to add only widget
    }

    @Override
    public void add(final Widget w) {
        //can't add more than one widget, setWidget() is used to add only widget
    }

    @Override
    public void setWidget(final IsWidget w) {
        setWidget(asWidgetOrNull(w));
    }

    @Override
    public void setWidget(final Widget w) {
        clear();
        contentPanel.add(w);
    }

    @Override
    public boolean remove(final Widget w) {
        return contentPanel.remove(w);
    }

    @Override
    public boolean remove(final IsWidget child) {
        return contentPanel.remove(child);
    }

    @Override
    public void clear() {
        contentPanel.clear();
    }

    @Override
    public Widget getWidget() {
        return contentPanel.getWidget(0);
    }

    @Override
    public String getStyleName() {
        return contentPanel.getStyleName();
    }

    @Override
    public void addStyleName(final String style) {
        contentPanel.addStyleName(style);
    }

    @Override
    public void removeStyleName(final String style) {
        contentPanel.removeStyleName(style);
    }

    @Override
    public void setStyleName(final String style) {
        contentPanel.setStyleName(style);
    }

    @Override
    public void setStyleName(final String style, final boolean add) {
        contentPanel.setStyleName(style, add);
    }

    @Override
    public int getOffsetHeight() {
        return contentPanel.getOffsetHeight();
    }

    @Override
    public int getOffsetWidth() {
        return contentPanel.getOffsetWidth();
    }

    @Override
    public void setHeight(final String height) {
        super.setHeight(height);
        contentPanel.setHeight(height);
    }

    @Override
    public void setWidth(final String width) {
        super.setWidth(width);
        contentPanel.setWidth(width);
    }

    public Element getScrollableElement() {
        return contentPanel.getElement();
    }

    @Override
    public int getMaxVertThumbPosition() {
        return maxVertThumbPosition;
    }

    @Override
    public int getMaxHoriThumbPosition() {
        return maxHorzThumbPosition;
    }

    @Override
    public int getVerticalThumbHeight() {
        return vThumbHeight;
    }

    @Override
    public int getVerticalTrackHeight() {
        return innerVTrackLength;
    }

    @Override
    public int getHorizontalTrackWidth() {
        return innerHTrackLength;
    }

    @Override
    public double getVScrollerFactor() {
        return vScrollerFactor;
    }

    @Override
    public double getHScrollerFactor() {
        return hScrollerFactor;
    }

    public void calculateParams(final int height, final int width) {
        final Element elem = getScrollableElement();
        int contentWidth, contentHeight;
        if (elem.hasChildNodes()) {
            final Element child = (Element) elem.getChild(0);
            final Style style = child.getStyle();
            final int marginTop, marginBottom, marginLeft, marginRight;
            marginBottom = convertToInt(style.getMarginBottom());
            marginTop = convertToInt(style.getMarginTop());
            marginRight = convertToInt(style.getMarginRight());
            marginLeft = convertToInt(style.getMarginLeft());
            contentHeight = marginTop + DOM.getElementPropertyInt(child, "offsetHeight") + marginBottom;//child.getScrollHeight();
            contentWidth = marginLeft + DOM.getElementPropertyInt(child, "offsetWidth") + marginRight;//child.getScrollWidth();
        } else {
            contentHeight = elem.getScrollHeight();
            contentWidth = elem.getScrollWidth();
        }
        pageWidth = width;
        pageHeight = height;
        checkForScroll(contentHeight, contentWidth);
        if (verticalScrollNeeded || alwaysVisible) {
            maxVContentScrollPosition = contentHeight - pageHeight;
            // track height not including decoration such as border, margin, and padding.
            innerVTrackLength = calculateInnerTrackSize(pageHeight);
            final int actualThumbSize = (int) Math.floor(innerVTrackLength
                    * ((double) pageHeight / (double) contentHeight));
            // thumb height including decoration such as border, margin, and padding.
            vThumbHeight = Math.max(actualThumbSize, minimumThumbSize);
            vScrollerFactor = (double) (innerVTrackLength - vThumbHeight) / (double) (contentHeight - pageHeight);
            maxVertThumbPosition = innerVTrackLength - vThumbHeight;
        } else {
            vThumbHeight = 0;
            vScrollerFactor = 0d;
            maxVertThumbPosition = 0;
        }
        if (horizontalScrollNeeded) {
            maxHContentScrollPosition = contentWidth - pageWidth;
            // track width not including decoration such as border, margin, and padding.
            innerHTrackLength = calculateInnerTrackSize(pageWidth);
            final int actualThumbSize = (int) Math.floor(innerHTrackLength
                    * ((double) pageWidth / (double) contentWidth));
            // thumb width including decoration such as border, margin, and padding.
            hThumbWidth = Math.max(actualThumbSize, minimumThumbSize);
            hScrollerFactor = (double) (innerHTrackLength - hThumbWidth) / (double) (contentWidth - pageWidth);
            maxHorzThumbPosition = innerHTrackLength - hThumbWidth;
        } else {
            hThumbWidth = 0;
            hScrollerFactor = 0d;
            maxHorzThumbPosition = 0;
        }
    }

    private void checkForScroll(final int contentHeight, final int contentWidth) {
        verticalScrollNeeded = pageHeight < contentHeight;
        if (verticalScrollNeeded || alwaysVisible) {
            pageWidth = pageWidth - cornerSize;
        }
        horizontalScrollNeeded = showHorizontalScroll ? pageWidth < contentWidth : showHorizontalScroll;
        if (horizontalScrollNeeded) {
            pageHeight = pageHeight - cornerSize;
        }
        if (!(verticalScrollNeeded || alwaysVisible) && horizontalScrollNeeded) {
            verticalScrollNeeded = pageHeight < contentHeight;
            if (verticalScrollNeeded) {
                pageWidth = pageWidth - cornerSize;
            }
        }
    }

    void setCornerSize(final int size) {
        cornerSize = size;
    }

    @Override
    public int getHorizontalThumbWidth() {
        return hThumbWidth;
    }

    private int calculateInnerTrackSize(final int pageSize) {
        int size = pageSize;
        if (isArrowEnabled()) {
            size = pageSize - ARROW_HEIGHT * 2;
        }
        return size - (TRACK_BORDER_SIZE * 2);
    }

    @Override
    public boolean isVerticalScrollNeeded() {
        return verticalScrollNeeded;
    }

    @Override
    public boolean isHorizontalScrollNeeded() {
        return horizontalScrollNeeded;
    }

    void setArrowEnabled(final boolean arrowEnabled) {
        this.arrowEnabled = arrowEnabled;
    }

    @Override
    public int getHorizontalScrollPosition() {
        return DOM.getElementPropertyInt(contentPanel.getElement(), "scrollLeft");
    }

    @Override
    public int getMaximumHorizontalScrollPosition() {
        return maxHContentScrollPosition;
    }

    @Override
    public int getMinimumHorizontalScrollPosition() {
        return MIN_HORIZONTAL_CONTENT_POSITION;
    }

    @Override
    public void setHorizontalScrollPosition(final int position) {
        DOM.setElementPropertyInt(contentPanel.getElement(), "scrollLeft", position);
    }

    @Override
    public int getPageWidth() {
        return pageWidth;
    }

    public void updateSize() {
        setHeight(pageHeight + "px");
        setWidth(pageWidth + "px");
    }

    public void showHorizontalScrollBar(final boolean show) {
        showHorizontalScroll = show;
    }

    public void setVerticalScrollAlwaysVisible(final boolean visible) {
        this.alwaysVisible = visible;
    }

}