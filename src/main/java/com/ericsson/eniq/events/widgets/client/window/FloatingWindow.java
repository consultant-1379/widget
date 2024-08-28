package com.ericsson.eniq.events.widgets.client.window;

import com.ericsson.eniq.events.common.client.CommonConstants;
import com.ericsson.eniq.events.common.client.CommonMain;
import com.ericsson.eniq.events.common.client.mvp.BaseView;
import com.ericsson.eniq.events.widgets.client.WidgetsResourceBundle;
import com.ericsson.eniq.events.widgets.client.WidgetsResourceBundleHelper;
import com.ericsson.eniq.events.widgets.client.button.ImageButton;
import com.ericsson.eniq.events.widgets.client.mask.MaskHelper;
import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.ericsson.eniq.events.widgets.client.utilities.ZIndexUpdatedEvent;
import com.ericsson.eniq.events.widgets.client.window.MinimizeWindowWidget.Handlers;
import com.ericsson.eniq.events.widgets.client.window.events.BoundaryUpdatedEvent;
import com.ericsson.eniq.events.widgets.client.window.events.BoundaryUpdatedEventHandler;
import com.ericsson.eniq.events.widgets.client.window.toolbar.AbstractToolbarItem;
import com.ericsson.eniq.events.widgets.client.window.toolbar.WindowToolbar;
import com.ericsson.eniq.events.widgets.client.window.toolbar.WindowToolbar.Direction;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.HandlerRegistration;

import java.util.Date;

/**
 * Simple floating window GWT based, no GXT code at all. Can be used as
 * container for any other components.
 * <p/>
 * To make this window draggable call setBoundaryPanel and then
 * dragController.makeDraggable(window, window.getDraggable());
 * 
 * 1px top border + 24px window header + 1px border + window body height + 1px +
 * 21px footer + 1px bottom border
 * 
 * @author evyagrz
 * @author ekurshi
 * @since 02 2012
 */
public class FloatingWindow extends Composite implements BoundaryUpdatedEventHandler {

    @UiField
    FlowPanel windowContainer;

    @UiField
    Label windowTitle;

    @UiField
    FlowPanel windowBody;

    @UiField
    FlowPanel windowFooter;

    @UiField
    FocusPanel draggable;

    @UiField
    ImageButton closeIcon;

    @UiField
    Image headerIcon;

    @UiField
    Label footerLabel;

    @UiField
    ResizePanel resizePanel;

    @UiField
    Image maximizeRestoreButton;

    @UiField
    ImageButton minimizeButton;

    @UiField
    WindowToolbar windowToolbar;

    MinimizeWindowWidget minimizeWindowWidget;

    private ImageResource iconResource;

    private final String windowId;

    private boolean alwaysBringToFront=true;

    @UiField(provided = true)
    WidgetsResourceBundle resourceBundle = WidgetsResourceBundleHelper.getBundle();// TODO need to create separate ResourceBundle for

    @UiField
    SimplePanel footerWidgetHolder;

    private static DivElement glassPanel;

    private final String SELENIUM_FLOATING_WINDOW         = "FLOATWIN";
    private final String SELENIUM_FLOATING_WINDOW_TITLE   = "FLOATWIN_TITLE";
    private final String SELENIUM_FLOATING_WINDOW_TOOLBAR = "FLOATWIN_TOOLBAR";
    private final String SELENIUM_FLOATING_WINDOW_BODY    = "FLOATWIN_BODY";
    private final String SELENIUM_FLOATING_WINDOW_FOOTER  = "FLOATWIN_FOOTER";

    // This is used for maximize and restore functionality
    private static enum WindowState {
        SAVED, MAXIMIZED;

        private static int width, height, left, top;

        @SuppressWarnings({ "static-access" })
        public void saveState(final int width, final int height, final int left, final int top) {
            this.width = width;
            this.height = height;
            this.left = left;
            this.top = top;
        }

        @SuppressWarnings("static-access")
        public void saveLocation(final int left, final int top) {
            this.left = left;
            this.top = top;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getLeft() {
            return left;
        }

        public int getTop() {
            return top;
        }
    }

    public static final int WINDOW_BORDER_WIDTH = 1; // in px

    public static final int FOOTER_HEIGHT = 21; // without top border

    private static final int RESIZE_PANEL_OFFSET = (ResizePanel.getPadding() + WINDOW_BORDER_WIDTH) * 2;

    private static final int TOOLBAR_HEIGHT = 30;// without top border

    private static final int HEADER_HEIGHT = 24;// without bottom border

    protected static int layerOfTheTopWindow = getTopZIndex();

    protected static FloatingWindow topWindow;

    private static final FloatingWindowUiBinder UI_BINDER = GWT.create(FloatingWindowUiBinder.class);

    private final MaskHelper maskHelper;

    private boolean resizable;

    private String title;

    private int top;

    private int left;

    private int width;

    private int height;

    private EDragController dragController;

    private AbsolutePanel boundaryPanel;

    private WindowState windowState = WindowState.SAVED;

    /**
     * Count used to position the location of a new window, offsetting based on
     * number of current active windows, to give a cascade the windows as they
     * are created.
     */
    private static int windowPositioningCount;

    /**
     * Allow for a new window to be offset i.e. if one window was a menu window
     * which opens further windows, can offset these windows so they dont appear
     * on top of the menu window.
     */
    private int startLeftOffset = 0, startTopOffset = 0;

    private HandlerRegistration handlerRegistration;

    private boolean isGlassEnabled;

	private String minimizedWindowTitle = null;

    private static final String glassStyleName = "gwt-PopupPanelGlass";
    static {
        glassPanel = Document.get().createDivElement();
        glassPanel.setClassName(glassStyleName);
        glassPanel.getStyle().setHeight(100, Unit.PCT);
        glassPanel.getStyle().setWidth(100, Unit.PCT);
        glassPanel.getStyle().setPosition(Position.ABSOLUTE);
        glassPanel.getStyle().setLeft(0, Unit.PX);
        glassPanel.getStyle().setTop(0, Unit.PX);
    }

    public FloatingWindow(final AbsolutePanel boundaryPanel, final int width, final int height, final String title) {
        this.windowId = "FLOATING_WINDOW_" + new Date().getTime();
        this.width = Math.min(width, boundaryPanel.getOffsetWidth());
        this.height = Math.min(height, boundaryPanel.getOffsetHeight());
        this.boundaryPanel = boundaryPanel;
        maskHelper = new MaskHelper();
        this.title = title;
        initWidget(UI_BINDER.createAndBindUi(this));
        setUpCloseImageButton();
        headerIcon.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
        sinkEvents(Event.ONMOUSEDOWN);
        init();
    }

    public FloatingWindow(final AbsolutePanel boundaryPanel, final ImageResource icon, final int width,
            final int height, final String title) {
        this(boundaryPanel, width, height, title);
        this.iconResource = icon;
        final Element element = headerIcon.getElement();
        element.getParentElement().getStyle().clearDisplay();
        element.getStyle().setDisplay(Display.BLOCK);//TODO not working, need to check
        headerIcon.setResource(icon);
    }

    @UiHandler("closeIcon")
    void onCloseClick(final ClickEvent event) {
        doClose();
    }

    public void setMinimizeVisible(final boolean visible) {
        minimizeButton.setVisible(visible);
    }

    @UiHandler("maximizeRestoreButton")
    void onMaximizeRestoreClick(final ClickEvent event) {
        if (WindowState.SAVED.equals(windowState)) {
            doMaximize();
        } else {
            doRestore();
        }
        resizeChilds();
    }

    @UiHandler("minimizeButton")
    void onMinimizeButtonClick(final ClickEvent event) {
        if (minimizeWindowWidget == null) {
            final Handlers handlers = new Handlers() {

                @Override
                public void onRestoreClick() {
                    setVisible(true);
                }

                @Override
                public void onCloseClick() {
                	setVisible(true); //SONVIS-358: prevent problem with reusing FW after closing minimised window
                    doClose();
                }
            };
            if (iconResource == null) {
                minimizeWindowWidget = new MinimizeWindowWidget(title, handlers);
            } else {
                minimizeWindowWidget = new MinimizeWindowWidget(title, handlers, iconResource);
            }
        }
        this.setVisible(false);
        minimizeWindowWidget.setTitle(minimizedWindowTitle!=null?minimizedWindowTitle:title); //SONVIS-357: if we have set a minimised title, use it else use normal title 
        boundaryPanel.add(minimizeWindowWidget);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        if (isGlassEnabled) {
            glassPanel.removeFromParent();
        }
        handlerRegistration.removeHandler();
    }

    public FocusPanel getDraggable() {
        return draggable;
    }

    public EDragController getDragController() {
        return dragController;
    }

    public void mask(final String message) {
        windowToolbar.setMask(true);
        maskHelper.mask(windowBody.getElement(), message);
    }

    public void unmask() {
        windowToolbar.setMask(false);
        maskHelper.unmask();
    }

    public void setBoundaryPanel(final AbsolutePanel boundaryPanel) {
        this.boundaryPanel = boundaryPanel;
    }

    public void setGlassEnabled(final boolean isEnabled) {
        this.isGlassEnabled = isEnabled;
        if (isEnabled && glassPanel == null) {
            glassPanel.getStyle().setZIndex(layerOfTheTopWindow - 1);
        }
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
        windowTitle.setText(title);
    }

    public void setFooterText(final String footerText) {
        footerLabel.setText(footerText);
    }

    /**
     * Used in CSS eval
     * 
     * @return border width
     */
    public static String getBorderWidth() {
        return WINDOW_BORDER_WIDTH + "px";
    }

    /**
     * Used in CSS eval
     * 
     * @return footer height
     */
    public static String getFooterHeight() {
        return FOOTER_HEIGHT + "px";
    }

    /**
     * Used in CSS eval
     * 
     * @return header height
     */
    public static String getHeaderHeight() {
        return HEADER_HEIGHT + "px";
    }

    public int getContentWidth() {
        return width - RESIZE_PANEL_OFFSET;
    }

    public int getContentHeight() {
        final int toolbarHeight = windowToolbar.isVisible() ? TOOLBAR_HEIGHT + WINDOW_BORDER_WIDTH : 0;
        return (height - (HEADER_HEIGHT + toolbarHeight + FOOTER_HEIGHT + 2 * WINDOW_BORDER_WIDTH + RESIZE_PANEL_OFFSET));
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        if (isGlassEnabled) {
            Document.get().getBody().appendChild(glassPanel);
        }
        windowPositioningCount++;
        final int displacement = windowPositioningCount * HEADER_HEIGHT;
        setWindowLocation(startLeftOffset + displacement, startTopOffset + displacement);
        bringToFront();
        CommonMain.getCommonInjector().getEventBus().fireEvent(new ZIndexUpdatedEvent(windowId, layerOfTheTopWindow));
        initResizeContainer();
        setupMaximizeRestoreButton();
        handlerRegistration = CommonMain.getCommonInjector().getEventBus().addHandler(BoundaryUpdatedEvent.TYPE, this);
    }

    private void init() {
        setTitle(title);
        resizable = true;
        setupDragController();
        resizePanel.setMinHeight(100);
        resizePanel.setMinWidth(150);
        updateBoundaries();
        left = resizePanel.getAbsoluteLeft();
        top = resizePanel.getAbsoluteTop();
        setSeleniumTags();
        updateSize();
    }

    private void setSeleniumTags() {
        //set selenium tag on window.   (.//*[@id='selenium_tag_FLOATWIN'])
        getElement().setId(CommonConstants.SELENIUM_TAG+SELENIUM_FLOATING_WINDOW);

        //set selenium tag on window title. (.//*[@id='selenium_tag_FLOATWIN_TITLE'])
        windowTitle.getElement().setId(CommonConstants.SELENIUM_TAG+SELENIUM_FLOATING_WINDOW_TITLE);

        //set the selenium tag on window toolbar. (.//*[@id='selenium_tag_FLOATWIN_TOOLBAR'])
        windowToolbar.getElement().setId(CommonConstants.SELENIUM_TAG+SELENIUM_FLOATING_WINDOW_TOOLBAR);

        //set selenium tag on window body.   (.//*[@id='selenium_tag_FLOATWIN_BODY'])
        windowBody.getElement().setId(CommonConstants.SELENIUM_TAG+SELENIUM_FLOATING_WINDOW_BODY);

        //set selenium tag on window footer.    (.//*[@id='selenium_tag_FLOATWIN_FOOTER'])
        windowFooter.getElement().setId(CommonConstants.SELENIUM_TAG+SELENIUM_FLOATING_WINDOW_FOOTER);
    }

    public void addToolbarItem(final AbstractToolbarItem item, final Direction direction) {
        windowToolbar.addItem(item, direction);
    }

    public void addToolbarItem(final AbstractToolbarItem item, final Direction direction, final int width,
            final boolean paired) {
        windowToolbar.addItem(item, direction, width, paired);
    }

    public WindowToolbar getToolbar() {
        return windowToolbar;
    }

    public void setResizable(final boolean resizable) {
        this.resizable = resizable;
        resizePanel.setStopResize(!resizable);
    }

    private void initResizeContainer() {
        resizePanel.addResizeHandler(new ResizePanel.ResizeHandler() {
            @Override
            public void onResize(final int w, final int h) {
                FloatingWindow.this.width = Math.min(boundaryPanel.getOffsetWidth(), w);
                FloatingWindow.this.height = Math.min(boundaryPanel.getOffsetHeight(), h);

                updateWindowSize(width - RESIZE_PANEL_OFFSET, height - RESIZE_PANEL_OFFSET);
                left = boundaryPanel.getWidgetLeft(FloatingWindow.this);
                top = boundaryPanel.getWidgetTop(FloatingWindow.this);
                windowState.saveState(width, height, left, top);
                FloatingWindow.this.resizeChilds();
            }

        });
    }

    /**
     * Update window size
     * 
     * @param w
     * @param h
     */
    private void updateWindowSize(final int w, final int h) {
        windowContainer.setSize(w + "px", h + "px");
        final int contentWidth = getContentWidth();
        final int contentHeight = getContentHeight();
        windowBody.setSize(contentWidth + "px", contentHeight + "px");
    }

    /**
     * Update resize panel size and window size
     */
    private void updateSize() {
        final int windowWidth = width - RESIZE_PANEL_OFFSET;
        final int windowHeight = height - RESIZE_PANEL_OFFSET;
        updateWindowSize(windowWidth, windowHeight);
        resizePanel.setSize(windowWidth + "px", windowHeight + "px");
    }

    public void resizeChilds() {
        // resizing child widgets, which require resize events
        for (int i = 0; i < windowBody.getWidgetCount(); i++) {
            if (windowBody.getWidget(i) instanceof RequiresResize) {
                ((RequiresResize) windowBody.getWidget(i)).onResize();
            }
        }
    }

    private void setUpCloseImageButton() {
        closeIcon.setHoverImage(resourceBundle.closeIconHover());
    }

    private void setupMaximizeRestoreButton() {
        maximizeRestoreButton.setResource(resourceBundle.maximizeButton());
        maximizeRestoreButton.getElement().getStyle().setCursor(Cursor.POINTER);
    }

    public void doRestore() {
        maximizeRestoreButton.setResource(resourceBundle.maximizeButton());
        windowState = WindowState.SAVED;
        width = windowState.getWidth();
        height = windowState.getHeight();
        resizePanel.setStopResize(!resizable);
        final int oldHeight = height;
        final int oldWidth = width;
        this.width = Math.min(width, boundaryPanel.getOffsetWidth());
        this.height = Math.min(height, boundaryPanel.getOffsetHeight());
        updateSize();
        final int newLeft = oldWidth != width ? 0 : windowState.getLeft();
        final int newTop = oldHeight != height ? 0 : windowState.getTop();
        setWindowLocation(newLeft, newTop);
    }

    public void doMaximize() {
        maximizeRestoreButton.setResource(resourceBundle.restoreButton());
        // saving state before maximise, only if it was not maximised before
        if (!isMaximized()) {
            windowState.saveState(width, height, left, top);
        }
        windowState = WindowState.MAXIMIZED;
        width = boundaryPanel.getOffsetWidth();
        height = boundaryPanel.getOffsetHeight();
        resizePanel.setStopResize(true);
        updateSize();
        setWindowLocation(0, 0);
    }

    public void doClose() {
    	boundaryPanel.remove(this);
        windowPositioningCount = Math.max(0, --windowPositioningCount);
        cleanUpContent();
        if (minimizeWindowWidget != null) { //if closing window from code, we should close the widget too
        	minimizeWindowWidget.close();
        }
    }

    /**
     * Utility method to clean up any content on window close, as the content
     * widget cannot use onDetach due to dragging. Could possibly fire an event
     * to a handler here, and let the handler clean up, but this saves adding
     * events and handlers
     */
    private void cleanUpContent() {
        for (int i = 0; i < windowBody.getWidgetCount(); i++) {
            if (windowBody.getWidget(i) instanceof BaseView<?>) {
                ((BaseView<?>) windowBody.getWidget(i)).getPresenter().unbind();
            }
        }
    }

    private void setupDragController() {
        dragController = new EDragController(this, boundaryPanel.getElement());
    }

    public void centerWindow() {
        final int clientWidth = Window.getClientWidth();
        final int clientHeight = Window.getClientHeight();
        final int displacement = windowPositioningCount * HEADER_HEIGHT;
        final int x = (clientWidth - width) / 2 - WINDOW_BORDER_WIDTH + displacement;
        final int y = (clientHeight - height) / 2 - WINDOW_BORDER_WIDTH + displacement;
        setWindowLocation(x, y);
        windowState.saveLocation(x, y);//fixing issue: restore to defalt when window opened as maximized.
    }

    /**
     * @see FlowPanel#add(com.google.gwt.user.client.ui.Widget)
     */
    public void add(final Widget content) {
        windowBody.add(content);
    }

    public Widget get(final int index) {
        return windowBody.getWidget(index);
    }

    public void setDraggable(final boolean enabled) {
        dragController.setDraggable(enabled);
    }

    /**
     * IFrame for Window
     * 
     * @param url
     * @return
     */
    public Frame setUrl(final String url) {
        final Frame f = new Frame(url);
        f.getElement().setPropertyInt("frameBorder", 0);
        // f.setSize(contentWidth + "px", contentHeight + "px");
        f.setSize("100%", "100%");
        windowBody.clear();
        windowBody.add(f);
        return f;
    }

    /**
     * EventListener
     */
    @Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        try {
            if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
                event.stopPropagation();//not using preventDefault because it prevent the focus event to propagate to inner elements
                if (topWindow != this) {
                    if(isAlwaysBringToFront())
                        bringToFront();
                }
            }
        } catch (final Exception e) {
            //Oh No!!
        }
    }

    protected void bringToFront() {
        DOM.setIntStyleAttribute(getElement(), "zIndex", layerOfTheTopWindow = getTopZIndex());
        CommonMain.getCommonInjector().getEventBus().fireEvent(new ZIndexUpdatedEvent(windowId, layerOfTheTopWindow));
        topWindow = this;
        if (isGlassEnabled) {
            glassPanel.getStyle().setZIndex(layerOfTheTopWindow - 1);
        }
    }

    private static int getTopZIndex() {
        return ZIndexHelper.getHighestZIndex();

    }

    public void setWindowLocation(final int windowLeft, final int windowTop) {
        this.left = windowLeft;
        this.top = windowTop;

        if (left + this.getOffsetWidth() > boundaryPanel.getElement().getClientWidth()) {
            left = boundaryPanel.getElement().getClientWidth() - this.getOffsetWidth();
        }

        if (top + this.getOffsetHeight() > boundaryPanel.getElement().getClientHeight()) {
            top = boundaryPanel.getElement().getClientHeight() - this.getOffsetHeight();
        }

        this.left = Math.max(0, left);
        this.top = Math.max(0, top);

        boundaryPanel.setWidgetPosition(this, left, top);
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    /**
     * Reset window positioning so next window will be launched at the starting
     * position for windows;
     */
    public void resetWindowPositioning() {
        windowPositioningCount = 0;
    }

    /**
     * Offset the default left starting position of the window
     * 
     * @param startLeftOffset
     */
    public void setStartLeftOffset(final int startLeftOffset) {
        this.startLeftOffset = startLeftOffset;
    }

    /**
     * Offset the default left starting position of the window
     * 
     * @param startTopOffset
     */
    public void setStartTopOffset(final int startTopOffset) {
        this.startTopOffset = startTopOffset;
    }

    public boolean isMaximized() {
        return WindowState.MAXIMIZED == windowState;
    }

    public int getVisibleHeight() {
        return height;
    }

    public int getVisibleWidth() {
        return width;
    }

    @Override
    public void onBoundaryUpdated(final String componentId) {
        if (componentId.equals(boundaryPanel.getElement().getId())) {
            updateBoundaries();
            if (isMaximized()) {
                doMaximize();
            } else {
                this.width = Math.min(width, boundaryPanel.getOffsetWidth());
                this.height = Math.min(height, boundaryPanel.getOffsetHeight());
                updateSize();
            }
            resizeChilds();
        }
    }

    private void updateBoundaries() {
        final int offsetHeight = boundaryPanel.getOffsetHeight();
        final int offsetWidth = boundaryPanel.getOffsetWidth();
        final int minY = boundaryPanel.getAbsoluteTop();
        final int minX = boundaryPanel.getAbsoluteLeft();
        resizePanel.setMaxHeight(offsetHeight);
        resizePanel.setMaxWidth(offsetWidth);
        resizePanel.setBoundries(minX, minY, minX + offsetWidth, minY + offsetHeight);
    }

    public void setMaximizeVisible(final boolean visible) {
        maximizeRestoreButton.setVisible(visible);
    }

    public void show() {
        boundaryPanel.add(this);
    }

    interface FloatingWindowUiBinder extends UiBinder<Widget, FloatingWindow> {
    }

    public void setFooterWidget(final Widget footerWidget) {
        footerWidgetHolder.add(footerWidget);
    }
    /*Utility Method so that we can turn off the "bring to front" functionality (this is "on" by default)*/
    public void setAlwaysBringToFront(boolean alwaysBringToFront) {
        this.alwaysBringToFront = alwaysBringToFront;
    }

    private boolean isAlwaysBringToFront() {
        return alwaysBringToFront;
    }

    /** Set the title of the minimised window (limited space) */
	public void setMinimizedTitle(String title) {
		minimizedWindowTitle = title;
		if (minimizeWindowWidget!=null) { //possible to call setMinimizedTitle while window is minimised, when this happens we should update immediately
			minimizeWindowWidget.setTitle(title);
		}
	}

}