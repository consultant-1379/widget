/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;

/**
 * Show a message dialog in the window which created it. Required for workspace management where multiple windows are launched resulting in multiple error/info
 * dialogs. Show each dialog in the center of its associated window with the glasspanel only over that window.
 * Its a bit nasty. Because the Dialog has to be added to the RootPanel, it would be seen in every tab, so we need to hide it when switching from the current tab
 * and show it when back in the current tab.
 * @author ecarsea
 * @since 2012
 *
 */
public class WindowDialog extends MessageDialog {
    public static WindowDialog get(String tabId) {
        return new WindowDialog(tabId);
    }

    private static Map<String, List<WindowDialog>> dialogMap = new HashMap<String, List<WindowDialog>>();

    private DivElement windowGlass;

    private Window window;

    private final String tabId;

    private static final String windowGlassStyleName = "gwt-PopupPanelGlass";

    public WindowDialog(String tabId) {
        super();
        this.tabId = tabId;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Widget#onAttach()
     */
    @Override
    protected void onAttach() {
        super.onAttach();
        if (window != null) {
            windowGlass.getStyle().setHeight(window.getOffsetHeight(), Unit.PX);
            windowGlass.getStyle().setWidth(window.getOffsetWidth(), Unit.PX);
            window.getElement().appendChild(windowGlass);
            final int left = window.getAbsoluteLeft() + ((window.getOffsetWidth() - getOffsetWidth()) >> 1);
            final int top = window.getAbsoluteTop() + ((window.getOffsetHeight() - getOffsetHeight()) >> 1);
            getElement().getStyle().setLeft(left, Unit.PX);
            getElement().getStyle().setTop(top, Unit.PX);
        }
    }

    public void show(final Window window, final String title, final String message, final DialogType type) {
        this.window = window;
        windowGlass = Document.get().createDivElement();
        windowGlass.setClassName(windowGlassStyleName);
        windowGlass.getStyle().setPosition(Position.ABSOLUTE);
        windowGlass.getStyle().setLeft(0, Unit.PX);
        windowGlass.getStyle().setTop(0, Unit.PX);
        windowGlass.getStyle().setZIndex(XDOM.getTopZIndex());
        getElement().getStyle().setZIndex(XDOM.getTopZIndex());
        getElement().getStyle().setPosition(Position.ABSOLUTE);
        setTitle(title);
        setMessage(message);
        setDialogType(type);
        setGlassEnabled(false);
        show();
        addToDialogMap();
    }

    private void addToDialogMap() {
        if (dialogMap.containsKey(tabId)) {
            dialogMap.get(tabId).add(this);
        } else {
            dialogMap.put(tabId, new ArrayList<WindowDialog>() {
                {
                    add(WindowDialog.this);
                }
            });

        }
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.DialogBox#hide()
     */
    @Override
    public void hide() {
        super.hide();
        if (windowGlass != null) {
            windowGlass.removeFromParent();
        }
        dialogMap.get(tabId).remove(this);
    }

    public static void showDialogsForTabChange(String newTabId) {
        for (String tabId : dialogMap.keySet()) {
            for (WindowDialog dialog : dialogMap.get(tabId)) {
                Display display = null;
                if (tabId.equals(newTabId)) {
                    display = Display.BLOCK;
                    // GXT updates all window Zindex for every new window
                    dialog.getElement().getStyle().setZIndex(XDOM.getTopZIndex());
                } else {
                    display = Display.NONE;
                }
                dialog.getElement().getStyle().setDisplay(display);
            }
        }
    }
}
