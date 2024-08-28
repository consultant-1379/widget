/*
 * *
 *  * -----------------------------------------------------------------------
 *  *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 *  * -----------------------------------------------------------------------
 *
 */

package com.ericsson.eniq.events.widgets.client.tree;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Custom Implementation of GWT Tree
 * @author ekurshi
 * @since November 2011
 */
public class ETree extends Tree {

    private static final String IMAGE_TAG = "IMG";

    private static final int IMAGE_SIZE = 9;

    private static final int ITEM_HEIGHT = 22;

    private static final int OTHER_KEY_DOWN = 63233;

    private static final int OTHER_KEY_LEFT = 63234;

    private static final int OTHER_KEY_RIGHT = 63235;

    private static final int OTHER_KEY_UP = 63232;

    private boolean arrowKeyDown;

    public @UiConstructor
    ETree(final Resources treeResources) {
        super(treeResources);
    }

    @Override
    public void onBrowserEvent(final Event event) {
        final int eventType = DOM.eventGetType(event);

        switch (eventType) {
        case Event.ONMOUSEDOWN: {
            if ((DOM.eventGetCurrentTarget(event) == getElement()) && (event.getButton() == Event.BUTTON_LEFT)) {
                final TreeItem item = findClickedItem(null, event.getClientX(), event.getClientY());
                final Element clickedElem = DOM.eventGetTarget(event);
                if (isImageElement(clickedElem)) {
                    break;
                }
                if (item == null) {
                    //ignoring click else where i.e on right side of tree, above or below the image
                    return;
                }
                if (item.getChildCount() > 0 && DOM.isOrHasChild(item.getElement(), clickedElem)
                        && !isImageElement(clickedElem)) {
                    item.setState(!item.getState(), true);
                }
            }
            break;
        }
        case Event.ONKEYDOWN: {
            if (!(DOM.eventGetAltKey(event) || DOM.eventGetMetaKey(event)) && isArrowKey(DOM.eventGetKeyCode(event))) {
                arrowKeyDown = true;
            } else if (DOM.eventGetKeyCode(event) == KeyCodes.KEY_ENTER) {
                final TreeItem item = getSelectedItem();
                if (item != null) {
                    SelectionEvent.fire(this, item);
                }
            }

        }
        }
        super.onBrowserEvent(event);
    }

    private boolean isImageElement(final Element clickedElem) {
        return IMAGE_TAG.equalsIgnoreCase(clickedElem.getTagName());
    }

    public boolean isArrowKeyDown() {
        return arrowKeyDown;
    }

    public void setArrowKeyDown(final boolean arrowKeyDown) {
        this.arrowKeyDown = arrowKeyDown;
    }

    private TreeItem findClickedItem(final TreeItem item, final int x, final int y) {
        if (item == null) {
            final int count = getItemCount();
            for (int i = 0; i < count; i++) {
                final TreeItem selected = findClickedItem(getItem(i), x, y);
                if (selected != null) {
                    return selected;
                }
            }
            return null;
        }
        final int count = item.getChildCount();
        for (int i = 0; i < count; i++) {
            final TreeItem selected = findClickedItem(item.getChild(i), x, y);
            if (selected != null) {
                return selected;
            }
        }
        return itemClicked(item, x, y);
    }

    /**
     * To check whether item is clicked or not
     *
     * @param item to be checked
     * @param x    x coordinate of mouse
     * @param y    y coordinate of mouse
     * @return item if it is clicked, null otherwise
     */
    private TreeItem itemClicked(final TreeItem item, final int x, final int y) {
        final int itemTop = item.getAbsoluteTop();
        final int itemLeft = item.getAbsoluteLeft();
        final int itemOffsetHeight = ITEM_HEIGHT;//item's is assumed to be 22 pixal to avoid parent item to span all its child
        final int itemOffsetWidth = item.getOffsetWidth();
        //left is started after image to ignore click above/below the image
        if (y >= itemTop && y <= itemTop + itemOffsetHeight && x >= itemLeft + IMAGE_SIZE
                && x <= itemLeft + itemOffsetWidth) {
            return item;
        }
        return null;
    }

    private static boolean isArrowKey(final int code) {
        switch (code) {
        case OTHER_KEY_DOWN:
        case OTHER_KEY_RIGHT:
        case OTHER_KEY_UP:
        case OTHER_KEY_LEFT:
        case KeyCodes.KEY_DOWN:
        case KeyCodes.KEY_RIGHT:
        case KeyCodes.KEY_UP:
        case KeyCodes.KEY_LEFT:
            return true;
        default:
            return false;
        }
    }

}
