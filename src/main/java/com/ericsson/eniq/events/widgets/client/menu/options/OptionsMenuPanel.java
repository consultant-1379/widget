package com.ericsson.eniq.events.widgets.client.menu.options;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/** @param <D> type of the data we work with */
public class OptionsMenuPanel<D> implements HasSelectionHandlers<D>, HasCloseHandlers<OptionsMenuPanel<D>> {

    private static final OptionsMenuResourceBundle resources = GWT.create(OptionsMenuResourceBundle.class);

    private final OptionsMenuResourceBundle.OptionsMenuStyle style;

    private final OptionsMenuItemTranslator<D> translator;

    private final HandlerManager handlerManager = new HandlerManager(this);

    private final FlowPanel content = new FlowPanel();

    private final PopupPanel popupPanel = new PopupPanel(true, false);

    private ItemClickHandler itemClickHandler;

    private final HashMap<D, Boolean> itemsVisibility = new HashMap<D, Boolean>();

    private Set<Separator> dependantSeparators;

    // Used to remove autohide partner when the popup is detached
    private Element autohidePartner;

    private boolean hasMouseFocus = false;

    public OptionsMenuPanel() {
        // By default we just have item toString() for translator
        this(new OptionsMenuItemTranslator<D>() {
            @Override
            public String getText(final D item) {
                return item.toString();
            }
        });
    }

    public OptionsMenuPanel(OptionsMenuItemTranslator<D> translator) {
        this(translator, OptionsMenuAlignment.LEFT);
    }

    /**
     * @param translator - translator to get item text for presentation
     * @param alignment  - when alignment is right, right corner will be straight (90 degrees) and the same for left
     */
    public OptionsMenuPanel(OptionsMenuItemTranslator<D> translator, OptionsMenuAlignment alignment) {
        this.translator = translator;
        this.itemClickHandler = new ItemClickHandler();

        style = resources.style();
        style.ensureInjected();

        popupPanel.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(final CloseEvent<PopupPanel> popupPanelCloseEvent) {
                CloseEvent.fire(OptionsMenuPanel.this, OptionsMenuPanel.this);
            }
        });

        popupPanel.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(final AttachEvent event) {
                // We are interested only in detach to clean up when popup is closed/removed
                if (!event.isAttached() && autohidePartner != null) {
                    popupPanel.removeAutoHidePartner(autohidePartner);
                    autohidePartner = null;
                }
            }
        });

        popupPanel.addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(final MouseOverEvent event) {
                hasMouseFocus = true;
            }
        }, MouseOverEvent.getType());

        popupPanel.setStyleName(style.menu());
        popupPanel.setWidget(content);

        setAlignMent(alignment);
    }

    public boolean hasMouseFocus() {
        return hasMouseFocus;
    }

    public void setHasMouseFocus(final boolean hasMouseFocus) {
        this.hasMouseFocus = hasMouseFocus;
    }

    public void add(final D item) {
        add(item, true);
    }

    public void add(final D item, final boolean visible) {
        final OptionsMenuItem<D> menuItem = new OptionsMenuItem<D>(translator.getText(item), item);
        menuItem.addStyleName(style.item());
        menuItem.addClickHandler(itemClickHandler);

        menuItem.setVisible(visible);
        itemsVisibility.put(item, visible);

        handleSeparatorDependency();

        content.add(menuItem);
    }

    public void addSeparator(final D... items) {
        final Separator separator = new Separator(items);

        if (items != null && items.length > 0) {
            if (dependantSeparators == null) {
                dependantSeparators = new HashSet<Separator>();
            }
            dependantSeparators.add(separator);
        }

        handleSeparatorDependency();

        content.add(separator);
    }

    public void setVisible(final D item, final boolean visible) {
        final Widget itemWidget = getItemWidget(item);

        if (itemWidget != null) {
            itemWidget.setVisible(visible);
            itemsVisibility.put(item, visible);

            handleSeparatorDependency();
        }
    }

    private void handleSeparatorDependency() {
        if (dependantSeparators != null) {
            for (Separator separator : dependantSeparators) {
                final List<D> dependOnItems = separator.getItems();
                if (dependOnItems == null) {
                    continue;
                }

                boolean isVisible = false;
                for (D item : dependOnItems) {
                    if (isVisible(item)) {
                        isVisible = true;
                        break;
                    }
                }

                separator.setVisible(isVisible);
            }
        }
    }

    public boolean isVisible(final D item) {
        final Boolean isVisible = itemsVisibility.get(item);
        return isVisible != null && isVisible;
    }

    /**
     * Used only in specific case, when Logout label has to be updated with username
     *
     * @param item  item for which you need to change label
     * @param label text for the new label
     */
    public void updateLabel(final D item, final String label) {
        final OptionsMenuItem<D> itemWidget = getItemWidget(item);
        itemWidget.setText(label);
    }

    public void clear() {
        content.clear();
        itemsVisibility.clear();

        if (dependantSeparators != null) {
            dependantSeparators.clear();
        }
    }

    public void showRelativeTo(final Element target) {
        showRelativeTo(target, 0, 0);
    }

    public void setPopupZIndex(int zIndex) {
        popupPanel.getElement().getStyle().setZIndex(zIndex);
    }

    public void showRelativeTo(final Element target, final int offsetTop, final int offsetLeft) {
        popupPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                position(target, offsetWidth, offsetHeight, offsetTop, offsetLeft);
            }
        });
    }

    public void hide() {
        setHasMouseFocus(false);
        popupPanel.hide();
    }

    // TODO: Probably remove this and make it handled by position()
    public void setAlignMent(final OptionsMenuAlignment alignment) {
        // Remove left & right alignments
        popupPanel.removeStyleName(style.left());
        popupPanel.removeStyleName(style.right());

        if (OptionsMenuAlignment.LEFT.equals(alignment)) {
            popupPanel.addStyleName(style.left());
        } else if (OptionsMenuAlignment.RIGHT.equals(alignment)) {
            popupPanel.addStyleName(style.right());
        }
    }

    public void setAutohidePartner(final Element partner) {
        this.autohidePartner = partner;

        popupPanel.addAutoHidePartner(partner);
    }

    @Override
    public HandlerRegistration addSelectionHandler(final SelectionHandler<D> handler) {
        return handlerManager.addHandler(SelectionEvent.getType(), handler);
    }

    @Override
    public void fireEvent(final GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    public boolean isShowing() {
        return popupPanel.isShowing();
    }

    @Override
    public HandlerRegistration addCloseHandler(final CloseHandler<OptionsMenuPanel<D>> handler) {
        return handlerManager.addHandler(CloseEvent.getType(), handler);
    }

    private OptionsMenuItem<D> getItemWidget(final D item) {
        for (Widget widget : content) {
            if (widget instanceof OptionsMenuItem) {
                final OptionsMenuItem<D> itemWidget = (OptionsMenuItem<D>) widget;
                if (itemWidget.getData().equals(item)) {
                    return itemWidget;
                }
            }
        }

        return null;
    }

    /**
     * Copied from PopupPanel
     *
     * @param relativeObject Element, relative to which should show this menu
     * @param offsetWidth    width of menu
     * @param offsetHeight   height of menu
     * @param offsetTop      tweak top position
     * @param offsetLeft     tweak left position
     *
     * @see com.google.gwt.user.client.ui.PopupPanel
     */
    private void position(final Element relativeObject, final int offsetWidth, final int offsetHeight,
            final int offsetTop, final int offsetLeft) {
        // Calculate left position for the popup. The computation for
        // the left position is bidi-sensitive.

        final int textBoxOffsetWidth = relativeObject.getOffsetWidth();

        // Compute the difference between the popup's width and the
        // textbox's width
        final int offsetWidthDiff = offsetWidth - textBoxOffsetWidth;

        int left;

        if (LocaleInfo.getCurrentLocale().isRTL()) { // RTL case

            final int textBoxAbsoluteLeft = relativeObject.getAbsoluteLeft();

            // Right-align the popup. Note that this computation is
            // valid in the case where offsetWidthDiff is negative.
            left = textBoxAbsoluteLeft - offsetWidthDiff;

            // If the suggestion popup is not as wide as the text box, always
            // align to the right edge of the text box. Otherwise, figure out whether
            // to right-align or left-align the popup.
            if (offsetWidthDiff > 0) {

                // Make sure scrolling is taken into account, since
                // box.getAbsoluteLeft() takes scrolling into account.
                final int windowRight = Window.getClientWidth() + Window.getScrollLeft();
                final int windowLeft = Window.getScrollLeft();

                // Compute the left value for the right edge of the textbox
                final int textBoxLeftValForRightEdge = textBoxAbsoluteLeft + textBoxOffsetWidth;

                // Distance from the right edge of the text box to the right edge
                // of the window
                final int distanceToWindowRight = windowRight - textBoxLeftValForRightEdge;

                // Distance from the right edge of the text box to the left edge of the
                // window
                final int distanceFromWindowLeft = textBoxLeftValForRightEdge - windowLeft;

                // If there is not enough space for the overflow of the popup's
                // width to the right of the text box and there IS enough space for the
                // overflow to the right of the text box, then left-align the popup.
                // However, if there is not enough space on either side, stick with
                // right-alignment.
                if (distanceFromWindowLeft < offsetWidth && distanceToWindowRight >= offsetWidthDiff) {
                    // Align with the left edge of the text box.
                    left = textBoxAbsoluteLeft;
                }
            }
        } else { // LTR case

            // Left-align the popup.
            left = relativeObject.getAbsoluteLeft();

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
        }

        // Calculate top position for the popup

        int top = relativeObject.getAbsoluteTop();

        // Make sure scrolling is taken into account, since
        // box.getAbsoluteTop() takes scrolling into account.
        final int windowTop = Window.getScrollTop();
        final int windowBottom = Window.getScrollTop() + Window.getClientHeight();

        // Distance from the top edge of the window to the top edge of the
        // text box
        final int distanceFromWindowTop = top - windowTop;

        // Distance from the bottom edge of the window to the bottom edge of
        // the text box
        final int distanceToWindowBottom = windowBottom - (top + relativeObject.getOffsetHeight());

        // If there is not enough space for the popup's height below the text
        // box and there IS enough space for the popup's height above the text
        // box, then then position the popup above the text box. However, if there
        // is not enough space on either side, then stick with displaying the
        // popup below the text box.
        if (distanceToWindowBottom < offsetHeight && distanceFromWindowTop >= offsetHeight) {
            top -= offsetHeight;
        } else {
            // Position above the text box
            top += relativeObject.getOffsetHeight();
        }

        // TODO: Revisit this, as in case when panel is shown on top of widget, the values might change
        top -= offsetTop;
        left -= offsetLeft;

        popupPanel.setPopupPosition(left, top);
    }

    class ItemClickHandler implements ClickHandler {
        @Override
        public void onClick(final ClickEvent event) {
            final OptionsMenuItem<D> source = (OptionsMenuItem<D>) event.getSource();
            SelectionEvent.fire(OptionsMenuPanel.this, source.getData());

            hide();

            event.stopPropagation();
        }
    }

    class Separator extends SimplePanel {

        private List<D> items;

        public Separator(D... items) {
            if (items != null && items.length > 0) {
                this.items = new ArrayList<D>();
                Collections.addAll(this.items, items);
            }

            addStyleName(style.separator());
        }

        public List<D> getItems() {
            return items;
        }
    }
}
