package com.ericsson.eniq.events.widgets.client.dropdown;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.PopupImpl;

public class DropDownPopup extends SimplePanel implements HasCloseHandlers<DropDownPopup> {

    private final FlowPanel popupParent;

    // Stripped down version from PopuPanel, to be able to place it in the DropDown element rather than RootPanel
    // TODO: Review what else can be cleaned up

    /**
     * A callback that is used to set the position of a {@link DropDownPopup} right
     * before it is shown.
     */
    public interface PositionCallback {

        /**
         * Provides the opportunity to set the position of the PopupPanel right
         * before the PopupPanel is shown. The offsetWidth and offsetHeight values
         * of the PopupPanel are made available to allow for positioning based on
         * its size.
         *
         * @param offsetWidth  the offsetWidth of the PopupPanel
         * @param offsetHeight the offsetHeight of the PopupPanel
         * @see DropDownPopup#setPopupPositionAndShow(PositionCallback)
         */
        void setPosition(int offsetWidth, int offsetHeight);
    }

    /**
     * An {@link com.google.gwt.animation.client.Animation} used to enlarge the popup into view.
     */
    static class ResizeAnimation {
        /**
         * The {@link DropDownPopup} being affected.
         */
        private DropDownPopup curPanel = null;

        /**
         * Indicates whether or not the {@link DropDownPopup} is in the process of
         * unloading. If the popup is unloading, then the animation just does
         * cleanup.
         */
        private boolean isUnloading;

        /**
         * A boolean indicating whether we are showing or hiding the popup.
         */
        private boolean showing;

        /**
         * Create a new {@link ResizeAnimation}.
         *
         * @param panel the panel to affect
         */
        public ResizeAnimation(DropDownPopup panel) {
            this.curPanel = panel;
        }

        /**
         * Open or close the content. This method always called immediately after
         * the DropDownPopup showing state has changed, so we base the animation on the
         * current state.
         *
         * @param showing     true if the popup is showing, false if not
         * @param isUnloading isUnloading
         */
        public void setState(boolean showing, boolean isUnloading) {
            // Immediately complete previous open/close animation
            this.isUnloading = isUnloading;

            // Update the logical state.
            curPanel.showing = showing;
            curPanel.updateHandlers();

            // Open the new item
            this.showing = showing;
            onInstantaneousRun();
        }

        private void onInstantaneousRun() {
            if (showing) {
                // Set the position attribute, and then attach to the DOM. Otherwise,
                // the DropDownPopup will appear to 'jump' from its static/relative
                // position to its absolute position (issue #1231).
                DOM.setStyleAttribute(curPanel.getElement(), "position", "absolute");
                if (curPanel.topPosition != -1) {
                    curPanel.setPopupPosition(curPanel.leftPosition, curPanel.topPosition);
                }

                if (curPanel.getPopupParent() != null) {
                    curPanel.getPopupParent().insert(curPanel, 0);
                } else {
                    RootPanel.get().add(curPanel);
                }
                impl.onShow(curPanel.getElement());
            } else {
                if (!isUnloading) {
                    if (curPanel.getPopupParent() != null) {
                        curPanel.getPopupParent().remove(curPanel);
                    } else {
                        RootPanel.get().remove(curPanel);
                    }
                }

                impl.onHide(curPanel.getElement());
            }

            DOM.setStyleAttribute(curPanel.getElement(), "overflow", "visible");
        }
    }

    /**
     * The default style name.
     */
    private static final String DEFAULT_STYLENAME = "gwt-DropDownPopup";

    private static final PopupImpl impl = GWT.create(PopupImpl.class);

    private final boolean autoHide, modal;

    private boolean showing;

    private final boolean autoHideOnHistoryEvents;

    private List<Element> autoHidePartners;

    // Used to track requested size across changing child widgets
    private String desiredHeight;

    private String desiredWidth;

    // the left style attribute in pixels
    private int leftPosition = -1;

    private HandlerRegistration nativePreviewHandlerRegistration;

    private HandlerRegistration historyHandlerRegistration;

    /**
     * The {@link ResizeAnimation} used to open and close the {@link DropDownPopup}s.
     */
    private final ResizeAnimation resizeAnimation = new ResizeAnimation(this);

    // The top style attribute in pixels
    private int topPosition = -1;

    /**
     * Creates an empty popup panel. A child widget must be added to it before it
     * is shown.
     *
     * @param popupParent Panel widget where popup will be located once shown
     */
    public DropDownPopup(FlowPanel popupParent) {
        super();

        this.popupParent = popupParent;

        super.getContainerElement().appendChild(impl.createElement());

        // Default position of popup should be in the upper-left corner of the
        // window. By setting a default position, the popup will not appear in
        // an undefined location if it is shown before its position is set.
        setPopupPosition(0, 0);
        setStyleName(DEFAULT_STYLENAME);
        setStyleName(getContainerElement(), "popupContent");

        autoHide = true;
        autoHideOnHistoryEvents = true;

        modal = false;
    }

    FlowPanel getPopupParent() {
        return popupParent;
    }

    /**
     * Mouse events that occur within an autoHide partner will not hide a panel
     * set to autoHide.
     *
     * @param partner the auto hide partner to add
     */
    public void addAutoHidePartner(Element partner) {
        assert partner != null : "partner cannot be null";
        if (autoHidePartners == null) {
            autoHidePartners = new ArrayList<Element>();
        }

        autoHidePartners.add(partner);
    }

    @Override
    public HandlerRegistration addCloseHandler(CloseHandler<DropDownPopup> handler) {
        return addHandler(handler, CloseEvent.getType());
    }

    /**
     * Centers the popup in the browser window and shows it. If the popup was
     * already showing, then the popup is centered.
     */
    public void center() {
        boolean initiallyShowing = showing;

        if (!initiallyShowing) {
            setVisible(false);
            show();
        }

        int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
        int top = (Window.getClientHeight() - getOffsetHeight()) >> 1;
        setPopupPosition(Math.max(Window.getScrollLeft() + left, 0), Math.max(Window.getScrollTop() + top, 0));

        if (!initiallyShowing) {
            setVisible(true);
        }
    }

    /**
     * Gets the panel's offset height in pixels. Calls to
     * {@link #setHeight(String)} before the panel's child widget is set will not
     * influence the offset height.
     *
     * @return the object's offset height
     */
    @Override
    public int getOffsetHeight() {
        return super.getOffsetHeight();
    }

    /**
     * Gets the panel's offset width in pixels. Calls to {@link #setWidth(String)}
     * before the panel's child widget is set will not influence the offset width.
     *
     * @return the object's offset width
     */
    @Override
    public int getOffsetWidth() {
        return super.getOffsetWidth();
    }

    /**
     * Hides the popup and detaches it from the page. This has no effect if it is
     * not currently showing.
     */
    public void hide() {
        hide(false);
    }

    /**
     * Hides the popup and detaches it from the page. This has no effect if it is
     * not currently showing.
     *
     * @param autoClosed the value that will be passed to
     *                   {@link CloseHandler#onClose(CloseEvent)} when the popup is closed
     */
    public void hide(boolean autoClosed) {
        if (!isShowing()) {
            return;
        }
        resizeAnimation.setState(false, false);
        CloseEvent.fire(this, this, autoClosed);
    }

    /**
     * Determines whether or not this popup is showing.
     *
     * @return <code>true</code> if the popup is showing
     * @see #show()
     * @see #hide()
     */
    public boolean isShowing() {
        return showing;
    }

    /**
     * Determines whether or not this popup is visible. Note that this just checks
     * the <code>visibility</code> style attribute, which is set in the
     * {@link #setVisible(boolean)} method. If you want to know if the popup is
     * attached to the page, use {@link #isShowing()} instead.
     *
     * @return <code>true</code> if the object is visible
     * @see #setVisible(boolean)
     */
    @Override
    public boolean isVisible() {
        return !"hidden".equals(getElement().getStyle().getProperty("visibility"));
    }

    /**
     * Sets the height of the panel's child widget. If the panel's child widget
     * has not been set, the height passed in will be cached and used to set the
     * height immediately after the child widget is set.
     * <p/>
     * <p>
     * Note that subclasses may have a different behavior. A subclass may decide
     * not to change the height of the child widget. It may instead decide to
     * change the height of an internal panel widget, which contains the child
     * widget.
     * </p>
     *
     * @param height the object's new height, in CSS units (e.g. "10px", "1em")
     */
    @Override
    public void setHeight(String height) {
        desiredHeight = height;
        maybeUpdateSize();
        // If the user cleared the size, revert to not trying to control children.
        if (height.length() == 0) {
            desiredHeight = null;
        }
    }

    /**
     * Sets the popup's position relative to the browser's client area. The
     * popup's position may be set before calling {@link #show()}.
     *
     * @param left the left position, in pixels
     * @param top  the top position, in pixels
     */
    public void setPopupPosition(int left, int top) {
        // Save the position of the popup
        leftPosition = left;
        topPosition = top;

        // Account for the difference between absolute position and the
        // body's positioning context.
        left -= Document.get().getBodyOffsetLeft();
        top -= Document.get().getBodyOffsetTop();

        // Set the popup's position manually, allowing setPopupPosition() to be
        // called before show() is called (so a popup can be positioned without it
        // 'jumping' on the screen).
        Element elem = getElement();
        elem.getStyle().setPropertyPx("left", left);
        elem.getStyle().setPropertyPx("top", top);
    }

    /**
     * Sets the popup's position using a {@link PositionCallback}, and shows the
     * popup. The callback allows positioning to be performed based on the
     * offsetWidth and offsetHeight of the popup, which are normally not available
     * until the popup is showing. By positioning the popup before it is shown,
     * the the popup will not jump from its original position to the new position.
     *
     * @param callback the callback to set the position of the popup
     * @see PositionCallback#setPosition(int offsetWidth, int offsetHeight)
     */
    public void setPopupPositionAndShow(PositionCallback callback) {
        setVisible(false);
        show();
        callback.setPosition(getOffsetWidth(), getOffsetHeight());
        setVisible(true);
    }

    @Override
    public void setTitle(String title) {
        Element containerElement = getContainerElement();
        if (title == null || title.length() == 0) {
            containerElement.removeAttribute("title");
        } else {
            containerElement.setAttribute("title", title);
        }
    }

    /**
     * Sets whether this object is visible. This method just sets the
     * <code>visibility</code> style attribute. You need to call {@link #show()}
     * to actually attached/detach the {@link DropDownPopup} to the page.
     *
     * @param visible <code>true</code> to show the object, <code>false</code> to
     *                hide it
     * @see #show()
     * @see #hide()
     */
    @Override
    public void setVisible(boolean visible) {
        // We use visibility here instead of UIObject's default of display
        // Because the panel is absolutely positioned, this will not create
        // "holes" in displayed contents and it allows normal layout passes
        // to occur so the size of the DropDownPopup can be reliably determined.
        DOM.setStyleAttribute(getElement(), "visibility", visible ? "visible" : "hidden");

        // If the PopupImpl creates an iframe shim, it's also necessary to hide it
        // as well.
        impl.setVisible(getElement(), visible);
    }

    @Override
    public void setWidget(Widget w) {
        super.setWidget(w);
        maybeUpdateSize();
    }

    /**
     * Sets the width of the panel's child widget. If the panel's child widget has
     * not been set, the width passed in will be cached and used to set the width
     * immediately after the child widget is set.
     * <p/>
     * <p>
     * Note that subclasses may have a different behavior. A subclass may decide
     * not to change the width of the child widget. It may instead decide to
     * change the width of an internal panel widget, which contains the child
     * widget.
     * </p>
     *
     * @param width the object's new width, in CSS units (e.g. "10px", "1em")
     */
    @Override
    public void setWidth(String width) {
        desiredWidth = width;
        maybeUpdateSize();
        // If the user cleared the size, revert to not trying to control children.
        if (width.length() == 0) {
            desiredWidth = null;
        }
    }

    /**
     * Shows the popup and attach it to the page. It must have a child widget
     * before this method is called.
     */
    public void show() {
        if (showing) {
            return;
        } else if (isAttached()) {
            // The popup is attached directly to another panel, so we need to remove
            // it from its parent before showing it. This is a weird use case, but
            // since DropDownPopup is a Widget, its legal.
            this.removeFromParent();
        }
        resizeAnimation.setState(true, false);
    }

    @Override
    protected com.google.gwt.user.client.Element getContainerElement() {
        return impl.getContainerElement(getPopupImplElement()).cast();
    }

    @Override
    protected com.google.gwt.user.client.Element getStyleElement() {
        return impl.getStyleElement(getPopupImplElement()).cast();
    }

    @Override
    protected void onUnload() {
        super.onUnload();

        // Just to be sure, we perform cleanup when the popup is unloaded (i.e.
        // removed from the DOM). This is normally taken care of in hide(), but it
        // can be missed if someone removes the popup directly from the RootPanel.
        if (isShowing()) {
            resizeAnimation.setState(false, true);
        }
    }

    /**
     * We control size by setting our child widget's size. However, if we don't
     * currently have a child, we record the size the user wanted so that when we
     * do get a child, we can set it correctly. Until size is explicitly cleared,
     * any child put into the popup will be given that size.
     */
    void maybeUpdateSize() {
        // For subclasses of DropDownPopup, we want the default behavior of setWidth
        // and setHeight to change the dimensions of DropDownPopup's child widget.
        // We do this because DropDownPopup's child widget is the first widget in
        // the hierarchy which provides structure to the panel. DialogBox is
        // an example of this. We want to set the dimensions on DialogBox's
        // FlexTable, which is DropDownPopup's child widget. However, it is not
        // DialogBox's child widget. To make sure that we are actually getting
        // DropDownPopup's child widget, we have to use super.getWidget().
        Widget w = super.getWidget();
        if (w != null) {
            if (desiredHeight != null) {
                w.setHeight(desiredHeight);
            }
            if (desiredWidth != null) {
                w.setWidth(desiredWidth);
            }
        }
    }

    /**
     * Remove focus from an Element.
     *
     * @param elt The Element on which <code>blur()</code> will be invoked
     */
    private native void blur(Element elt) /*-{
                                          // Issue 2390: blurring the body causes IE to disappear to the background
                                          if (elt.blur && elt != $doc.body) {
                                          elt.blur();
                                          }
                                          }-*/;

    /**
     * Does the event target one of the partner elements?
     *
     * @param event the native event
     * @return true if the event targets a partner
     */
    private boolean eventTargetsPartner(NativeEvent event) {
        if (autoHidePartners == null) {
            return false;
        }

        EventTarget target = event.getEventTarget();
        if (Element.is(target)) {
            for (Element elem : autoHidePartners) {
                if (elem.isOrHasChild(Element.as(target))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Does the event target this popup?
     *
     * @param event the native event
     * @return true if the event targets the popup
     */
    private boolean eventTargetsPopup(NativeEvent event) {
        EventTarget target = event.getEventTarget();
        return Element.is(target) && getElement().isOrHasChild(Element.as(target));
    }

    /**
     * Get the element that {@link PopupImpl} uses. PopupImpl creates an element
     * that goes inside of the outer element, so all methods in PopupImpl are
     * relative to the first child of the outer element, not the outer element
     * itself.
     *
     * @return the Element that {@link PopupImpl} creates and expects
     */
    private com.google.gwt.user.client.Element getPopupImplElement() {
        return DOM.getFirstChild(super.getContainerElement());
    }

    /**
     * Preview the {@link com.google.gwt.user.client.Event.NativePreviewEvent}.
     *
     * @param event the {@link com.google.gwt.user.client.Event.NativePreviewEvent}
     */
    private void previewNativeEvent(Event.NativePreviewEvent event) {
        // If the event has been canceled or consumed, ignore it
        if (event.isCanceled()) {
            // We need to ensure that we cancel the event even if its been consumed so
            // that popups lower on the stack do not auto hide
            if (modal) {
                event.cancel();
            }
            return;
        }

        switch (event.getTypeInt()) {
        case Event.ONKEYDOWN:
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                hide();
            }

            break;
        }

        if (event.isCanceled()) {
            return;
        }

        // If the event targets the popup or the partner, consume it
        Event nativeEvent = Event.as(event.getNativeEvent());
        boolean eventTargetsPopupOrPartner = eventTargetsPopup(nativeEvent) || eventTargetsPartner(nativeEvent);
        if (eventTargetsPopupOrPartner) {
            event.consume();
        }

        // Cancel the event if it doesn't target the modal popup. Note that the
        // event can be both canceled and consumed.
        if (modal) {
            event.cancel();
        }

        // Switch on the event type
        int type = nativeEvent.getTypeInt();
        switch (type) {
        case Event.ONMOUSEDOWN:
            // Don't eat events if event capture is enabled, as this can
            // interfere with dialog dragging, for example.
            if (DOM.getCaptureElement() != null) {
                event.consume();
                return;
            }

            if (!eventTargetsPopupOrPartner && autoHide) {
                hide(true);
                return;
            }
            break;
        case Event.ONMOUSEUP:
        case Event.ONMOUSEMOVE:
        case Event.ONCLICK:
        case Event.ONDBLCLICK: {
            // Don't eat events if event capture is enabled, as this can
            // interfere with dialog dragging, for example.
            if (DOM.getCaptureElement() != null) {
                event.consume();
                return;
            }
            break;
        }

        case Event.ONFOCUS: {
            Element target = nativeEvent.getTarget();
            if (modal && !eventTargetsPopupOrPartner && (target != null)) {
                blur(target);
                event.cancel();
                return;
            }
            break;
        }
        }
    }

    /**
     * Register or unregister the handlers used by {@link DropDownPopup}.
     */
    private void updateHandlers() {
        // Remove any existing handlers.
        if (nativePreviewHandlerRegistration != null) {
            nativePreviewHandlerRegistration.removeHandler();
            nativePreviewHandlerRegistration = null;
        }
        if (historyHandlerRegistration != null) {
            historyHandlerRegistration.removeHandler();
            historyHandlerRegistration = null;
        }

        // Create handlers if showing.
        if (showing) {
            nativePreviewHandlerRegistration = Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
                @Override
                public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                    previewNativeEvent(event);
                }
            });
            historyHandlerRegistration = History.addValueChangeHandler(new ValueChangeHandler<String>() {
                @Override
                public void onValueChange(ValueChangeEvent<String> event) {
                    if (autoHideOnHistoryEvents) {
                        hide();
                    }
                }
            });
        }
    }
}
