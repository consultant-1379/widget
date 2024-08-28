package com.ericsson.eniq.events.widgets.client.scroll;

import static com.ericsson.eniq.events.widgets.client.scroll.ScrollConstants.*;

import com.ericsson.eniq.events.widgets.client.scroll.ScrollBarArrow.PerformTask;
import com.ericsson.eniq.events.widgets.client.scroll.events.CustomScrollEvent.EventType;
import com.ericsson.eniq.events.widgets.client.scroll.events.CustomScrollHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalScrolling;
import com.google.gwt.user.client.ui.HasVerticalScrolling;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ealeerm - Alexey Ermykin
 * @author ekurshi
 * @since 04 2012
 */
public class ScrollPanel extends Composite implements HasVerticalScrolling, HasHorizontalScrolling, RequiresResize,
        ProvidesResize {

    @UiField(provided = true)
    ScrollBarResourceBundle resources;

    @UiField
    ScrollablePanel scrollableContainer;

    @UiField
    FlowPanel verticalScrollContainer;

    @UiField
    ScrollTrack verticalScrollTrack;

    @UiField
    ScrollBarArrow upArrow;

    @UiField
    ScrollBarArrow downArrow;

    @UiField
    ScrollBarArrow leftArrow;

    @UiField
    ScrollBarArrow rightArrow;

    @UiField
    FlowPanel corner;

    @UiField
    FlowPanel horizontalScrollContainer;

    @UiField
    ScrollTrack horizontalScrollTrack;

    private VerticalThumb verticalBar;

    private HorizontalThumb horizontalBar;

    ScrollPanelUiBinder binder = GWT.create(ScrollPanelUiBinder.class);

    private int lineSize = V_LINE_SIZE;

    private boolean alwaysVisible = true;

    static ScrollBarResourceBundle DEFAULT_RESOURCE = GWT.create(ScrollBarResourceBundle.class);

    private static ScrollBarResourceBundle getDefaultResource() {
        return DEFAULT_RESOURCE;

    }

    interface ScrollPanelUiBinder extends UiBinder<Widget, ScrollPanel> {

    }

    public interface ScrollBarResourceBundle extends ClientBundle {

        @Source("ScrollWidget.css")
        ScrollWidgetStyle style();

        @Source("images/scroll_bar_arrow_up.png")
        ImageResource upArrowImage();

        @Source("images/scroll_bar_arrow_down.png")
        ImageResource downArrowImage();

        @Source("images/scroll_bar_arrow_left.png")
        ImageResource leftArrowImage();

        @Source("images/scroll_bar_arrow_right.png")
        ImageResource rightArrowImage();

    }

    public interface ScrollWidgetStyle extends CssResource {

        String vScroller();

        String hScroller();

        String verticalScrollTrack();

        String scrollPanel();

        String upArrow();

        String downArrow();

        String rightArrow();

        String leftArrow();

        String innerTrackWidth();

        String vArrowHeight();

        String verticalScrollBar();

        String horizontalScrollBar();

        String horizontalScrollTrack();

        String corner();
    }

    public ScrollPanel() {
        this(getDefaultResource());
    }

    public ScrollPanel(final ScrollBarResourceBundle resource) {
        resources = resource;
        resources.style().ensureInjected();
        initWidget(binder.createAndBindUi(this));
        init();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        redraw();
    }

    private void init() {
        applyStyles();
        horizontalBar = new HorizontalThumb(scrollableContainer, horizontalScrollTrack);
        horizontalBar.addStyleName(resources.style().hScroller());
        verticalBar = new VerticalThumb(scrollableContainer, verticalScrollTrack);
        verticalBar.addStyleName(resources.style().vScroller());
        downArrow.setTask(new PerformTask() {
            @Override
            public void doTask() {
                if (scrollableContainer.isVerticalScrollNeeded()) {
                    final int contentPos = scrollableContainer.getVerticalScrollPosition();
                    final int newContentPos = contentPos + getLineSize();
                    verticalBar.setContentPosition(newContentPos);
                    verticalBar.fireScrollEvent(EventType.LINE_CHANGE);
                }
            }

            ;
        });
        upArrow.setTask(new PerformTask() {
            @Override
            public void doTask() {
                if (scrollableContainer.isVerticalScrollNeeded()) {
                    final int contentPos = scrollableContainer.getVerticalScrollPosition();
                    final int newContentPos = contentPos - getLineSize();
                    verticalBar.setContentPosition(newContentPos);
                    verticalBar.fireScrollEvent(EventType.LINE_CHANGE);
                }
            }

            ;
        });
        leftArrow.setTask(new PerformTask() {

            @Override
            public void doTask() {
                final int contentPos = scrollableContainer.getHorizontalScrollPosition();
                final int newContentPos = contentPos - getLineSize();
                horizontalBar.setContentPosition(newContentPos);
                horizontalBar.fireScrollEvent(EventType.LINE_CHANGE);
            }
        });
        rightArrow.setTask(new PerformTask() {

            @Override
            public void doTask() {
                final int contentPos = scrollableContainer.getHorizontalScrollPosition();
                final int newContentPos = contentPos + getLineSize();
                horizontalBar.setContentPosition(newContentPos);
                horizontalBar.fireScrollEvent(EventType.LINE_CHANGE);
            }
        });

    }

    private void applyStyles() {
        //This size already defined in ui.xml, but does not take effects
        final ScrollWidgetStyle widgetStyle = resources.style();
        final String w = widgetStyle.innerTrackWidth();
        final String h = widgetStyle.vArrowHeight();
        upArrow.setSize(w, h);
        upArrow.addStyleName(resources.style().upArrow());
        downArrow.addStyleName(resources.style().downArrow());
        leftArrow.addStyleName(resources.style().leftArrow());
        rightArrow.addStyleName(resources.style().rightArrow());
        rightArrow.setSize(h, w);
        leftArrow.setSize(h, w);
        verticalScrollContainer.getElement().getStyle().setWidth(14, Unit.PX);
        horizontalScrollTrack.getElement().getStyle().setFloat(Float.LEFT);
        Style style = downArrow.getElement().getStyle();
        style.setFloat(Float.LEFT);
        downArrow.setSize(w, h);
        style.setProperty("backgroundPosition", "center 1px");
        style = upArrow.getElement().getStyle();
        style.setFloat(Float.LEFT);
        style.setProperty("backgroundPosition", "center 5px");
        style = leftArrow.getElement().getStyle();
        style.setFloat(Float.LEFT);
        style.setProperty("backgroundPosition", "5px center");
        style = rightArrow.getElement().getStyle();
        style.setProperty("backgroundPosition", "1px center");
        style.setFloat(Float.LEFT);
        corner.getElement().getStyle().setFloat(Float.LEFT);
        final int scrollBarWidth = convertToInt(w) + TRACK_BORDER_SIZE * 2;
        scrollableContainer.setCornerSize(scrollBarWidth);
        //width and height will be same, as this block will be square
        corner.setSize(scrollBarWidth + "px", scrollBarWidth + "px");

    }

    public boolean isArrowEnabled() {
        return scrollableContainer.isArrowEnabled();
    }

    public void setArrowEnabled(final boolean arrowEnabled) {
        scrollableContainer.setArrowEnabled(arrowEnabled);
    }

    public void setLineSize(final int lineSize) {
        this.lineSize = lineSize;
    }

    public int getLineSize() {
        return lineSize;
    }

    public void showHorizontalScrollBar(final boolean show) {
        scrollableContainer.showHorizontalScrollBar(show);
    }

    private void setVerticalScrollVisible(final boolean visible) {
        verticalBar.setVisible(visible);
        final boolean showTrack = visible || alwaysVisible;
        verticalScrollContainer.setVisible(showTrack);
        if (isArrowEnabled()) {
            upArrow.setEnable(visible);
            downArrow.setEnable(visible);
            upArrow.setVisible(showTrack);
            downArrow.setVisible(showTrack);
        }
    }

    private void setHorizontalScrollVisible(final boolean visible) {
        horizontalScrollContainer.setVisible(visible);
        if (isArrowEnabled()) {
            leftArrow.setVisible(visible);
            rightArrow.setVisible(visible);
        }
    }

    public void scrollToBottom() {
        setVerticalScrollPosition(getMaximumVerticalScrollPosition());
    }

    public void scrollToTop() {
        setVerticalScrollPosition(getMinimumVerticalScrollPosition());
    }

    public void set(final Widget w) {
        scrollableContainer.setWidget(w);
        redraw();
    }

    @Override
    protected Widget getWidget() {
        return scrollableContainer.getWidget();
    }

    /**
     * @return is scroll visible
     */
    public boolean isScrollVisible() {
        return alwaysVisible || scrollableContainer.isVerticalScrollNeeded();
    }

    /**
     * default is true
     */
    public void setVerticalScrollAlwaysVisible(final boolean visible) {
        alwaysVisible = visible;
        scrollableContainer.setVerticalScrollAlwaysVisible(visible);
    }

    /**
     * Adds a {@link com.google.gwt.event.dom.client.ScrollEvent} handler to scrollable element.
     *
     * @param handler the handler
     * @return returns the handler registration
     */
    public HandlerRegistration addNativeScrollHandler(final ScrollHandler handler) {
        return scrollableContainer.addScrollHandler(handler);
    }

    public void addCustomScrollHandler(final CustomScrollHandler handler) {
        verticalBar.addCustomScrollHandler(handler);
        horizontalBar.addCustomScrollHandler(handler);
    }

    public void addContentPanelStyleName(final String style) {
        scrollableContainer.addStyleName(style);
    }

    /**
     * Removes all child widgets from content panel.
     */
    public void clear() {
        scrollableContainer.clear();
        redraw();
    }

    public void redraw() {
        redraw(false);
    }

    /**
     * Calculates the dimensions of scrollbar's components.Should be called first time and whenever height or width of scrollbar's inner widget changes.
     */
    public void redraw(final boolean persistPosition) {
        if (isAttached()) {
            final Element element = getElement();
            scrollableContainer.calculateParams(element.getClientHeight(), element.getClientWidth());
            if (scrollableContainer.isVerticalScrollNeeded() || alwaysVisible) {
                verticalBar.redraw(persistPosition);
            } else {
                verticalBar.resetScroll();
            }
            if (scrollableContainer.isHorizontalScrollNeeded()) {
                horizontalBar.redraw(persistPosition);
            } else {
                horizontalBar.resetScroll();
            }
            updateContentSize();
        }
    }

    private void updateContentSize() {
        final boolean vScrollVisible = scrollableContainer.isVerticalScrollNeeded();
        final boolean hScrollVisible = scrollableContainer.isHorizontalScrollNeeded();
        scrollableContainer.updateSize();
        setVerticalScrollVisible(vScrollVisible);
        setHorizontalScrollVisible(hScrollVisible);
        corner.setVisible(hScrollVisible && (vScrollVisible || alwaysVisible));
    }

    @Override
    public int getMaximumVerticalScrollPosition() {
        return scrollableContainer.getMaximumVerticalScrollPosition();
    }

    @Override
    public int getMinimumVerticalScrollPosition() {
        return scrollableContainer.getMinimumVerticalScrollPosition();
    }

    @Override
    public int getVerticalScrollPosition() {
        return scrollableContainer.getVerticalScrollPosition();
    }

    @Override
    public void setVerticalScrollPosition(final int position) {
        verticalBar.setContentPosition(position);
    }

    public void setMinimumThumbHeight(final int height) {
        scrollableContainer.setMinimumThumbSize(height);
    }

    @Override
    public int getHorizontalScrollPosition() {
        return scrollableContainer.getHorizontalScrollPosition();
    }

    @Override
    public int getMaximumHorizontalScrollPosition() {
        return scrollableContainer.getMaximumHorizontalScrollPosition();
    }

    @Override
    public int getMinimumHorizontalScrollPosition() {
        return scrollableContainer.getMinimumHorizontalScrollPosition();
    }

    @Override
    public void setHorizontalScrollPosition(final int position) {
        scrollableContainer.setHorizontalScrollPosition(position);
    }

    public void onResize(final String width, final String height) {
        setSize(width, height);
        redraw(true);
    }

    /**
     * Should be called only when it is only widget inside parent widget, otherwise call {@link #onResize(String, String)}
     */
    @Override
    public void onResize() {
        final Widget parent = getParent();
        onResize(parent.getOffsetWidth() + "px", parent.getOffsetHeight() + "px");
        final Widget widget = getWidget();
        if (widget != null && widget instanceof RequiresResize) {
            ((RequiresResize) widget).onResize();
        }
    }

    public int getScrollBarWidth() {
        return convertToInt(resources.style().innerTrackWidth()) + TRACK_BORDER_SIZE * 2;
    }
}
