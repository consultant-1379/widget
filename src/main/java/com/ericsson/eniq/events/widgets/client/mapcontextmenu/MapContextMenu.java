/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.widgets.client.mapcontextmenu;

import com.ericsson.eniq.events.widgets.client.popmenu.PopCell;
import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

import java.util.List;

/**
 * @author egallou
 * @since 12/2012
 */

public class MapContextMenu extends PopupPanel {

    private boolean scrollingRequired = false;

    private static MapContextMenuResourceBundle resources;

    private int maxNumVisibleCells = 5; //default

    private int stopPosition = 0;

    private int top = 0;

    private int cellHeight = 0;

    private SimplePanel mainPopupPanel = new SimplePanel();

    private FlowPanel body = new FlowPanel();

    private Button topButton = new Button();

    private SimplePanel middlePanel = new SimplePanel();

    private Button bottomButton = new Button();

    private FocusPanel scrollingSimplePanel = new FocusPanel();

    private int wheelCount;

    private boolean mouseIsOverUpButton;

    private boolean mouseIsOverDownButton;

    private Timer scrollTimer;

    static {
        resources = GWT.create(MapContextMenuResourceBundle.class);
        resources.style().ensureInjected();
    }

    public MapContextMenu() {
        super(true);
        setWidget(createContextWidget());
        setStyleName(resources.style().popupPanel());  /*remove default GWT styling*/
        setScrollEnabled(scrollingRequired);
    }

    private void setScrollEnabled(boolean scrollEnabled) {
        this.scrollingRequired = scrollEnabled;
        if (scrollEnabled) {
            enableButtonsBasedOnPosition();
        } else {
            setTopButtonEnabled(topButton, scrollEnabled);
            setBottomButtonEnabled(bottomButton, scrollEnabled);
        }
    }

    private void setTopButtonEnabled(Button button, boolean enable) {
        button.setEnabled(enable);
        if (enable) {
            button.setEnabled(true);
            button.removeStyleName(resources.style().arrowUpDisabledIcon());
        } else {
            button.addStyleName(resources.style().arrowUpDisabledIcon());
            button.setEnabled(false);
        }
    }

    private void setBottomButtonEnabled(Button button, boolean enable) {
        button.setEnabled(enable);
        if (enable) {
            button.setEnabled(true);
            button.removeStyleName(resources.style().arrowDownDisabledIcon());
        } else {
            button.addStyleName(resources.style().arrowDownDisabledIcon());
            button.setEnabled(false);
        }
    }

    private void enableButtonsBasedOnPosition() {
        /*at bottom*/
        if ((top == 0) && (scrollingRequired)) {
            setTopButtonEnabled(topButton, false);
            setBottomButtonEnabled(bottomButton, true);
            mouseIsOverUpButton=false;
        }
        /*at top*/
        else if ((top <= stopPosition) && (scrollingRequired)) {
            setTopButtonEnabled(topButton, true);
            setBottomButtonEnabled(bottomButton, false);
            mouseIsOverDownButton=false; /*in case we are hovering to scroll*/
        }
        /*some where in middle*/
        else if (scrollingRequired) {
            setTopButtonEnabled(topButton, true);
            setBottomButtonEnabled(bottomButton, true);
        }
    }

    private Widget createContextWidget() {
        setUpMenuStyles();
        addEventHandlers();

        body.add(topButton);
        body.add(scrollingSimplePanel);
        body.add(bottomButton);

        mainPopupPanel.add(body);
        return mainPopupPanel;
    }

    private void setUpMenuStyles() {
        topButton.setStyleName(resources.style().arrowUpIcon());
        topButton.addStyleName(resources.style().scrollButton());

        bottomButton.setStyleName(resources.style().arrowDownIcon());
        bottomButton.addStyleName(resources.style().scrollButton());

        scrollingSimplePanel.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        scrollingSimplePanel.add(middlePanel); /*hides the overflow of the middlePanel, holding the grid*/

        middlePanel.setStyleName(resources.style().middleGrid());/*Holds the grid*/
    }

    /*Add mouse wheel event to scrollingPanel, click events to buttons*/
    private void addEventHandlers() {

       topButton.addMouseOverHandler(new MouseOverHandler() {
           @Override
           public void onMouseOver(MouseOverEvent event) {
               mouseIsOverUpButton=true;
               scrollUpOnHover();
           }
        });

        topButton.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
               mouseIsOverUpButton=false;
            }
        });

        bottomButton.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                mouseIsOverDownButton=true;
                scrollDownOnHover();
            }
        });

        bottomButton.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                mouseIsOverDownButton=false;
            }
        });

        scrollingSimplePanel.addMouseWheelHandler(new MouseWheelHandler() {
            @Override
            public void onMouseWheel(MouseWheelEvent event) {
                DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
                wheelCount = event.getDeltaY();
                if (wheelCount < 0) {
                    scrollUp();
                } else {
                    scrollDown();
                }
            }
        });
    }

    /*required to slow scrolling down and check for mouse out*/
    private void scrollUpOnHover() {
        scrollTimer = new Timer() {
            public void run() {
                if (mouseIsOverUpButton) {
                    scrollUp();
                } else {
                    cancel();
                }
            }
        };
        scrollTimer.scheduleRepeating(200);
    }

    private void scrollDownOnHover() {
        scrollTimer = new Timer() {
            public void run() {
                if (mouseIsOverDownButton) {
                    scrollDown();
                } else {
                    cancel();
                }
            }
        };
        scrollTimer.scheduleRepeating(200);
    }

    public void populate(List<PopCell> widgets) {
        middlePanel.clear(); //remove everything

        if (widgets.size() > 0) {
            cellHeight = (int) widgets.get(0).getHeight();
        }

        Grid propertiesGrid = setUpCellGrid(widgets);

        for (int row = 0; row < widgets.size(); ++row) {
            propertiesGrid.getRowFormatter().getElement(row).getStyle().setHeight(cellHeight, Style.Unit.PX);
            propertiesGrid.getRowFormatter().addStyleName(row, resources.style().gridRow());
            propertiesGrid.setWidget(row, 0, widgets.get(row));
            if (row % 2 == 0) {
                propertiesGrid.getRowFormatter().addStyleName(row, resources.style().oddRow());
            }
        }
        middlePanel.add(propertiesGrid);

        int fullHeight = widgets.size() * cellHeight;
        int maxVisibleHeight = maxNumVisibleCells * cellHeight;

        stopPosition = -(fullHeight - maxVisibleHeight);
        scrollingSimplePanel.getElement().getStyle().setProperty("maxHeight", maxVisibleHeight + "px");

        checkIfScrollingRequired(widgets);
    }

    /*Grid to hold list of widgets, styled*/
    private Grid setUpCellGrid(List<PopCell> widgets) {
        Grid cellWidgetsGrid = new Grid(widgets.size(), 1);
        cellWidgetsGrid.addStyleName(resources.style().grid());
        cellWidgetsGrid.setCellSpacing(0);
        cellWidgetsGrid.setCellPadding(0);
        return cellWidgetsGrid;
    }

    /*scrolling only enabled if we have more widgets than maxNumVisibleCells */
    private void checkIfScrollingRequired(List<PopCell> widgets) {
        if (maxNumVisibleCells < widgets.size()) {
            scrollingRequired = true;
            setScrollEnabled(true);
        }
    }

    private void scrollUp() {
        if (!(top == 0)) {
            top = top + cellHeight;
            middlePanel.getElement().getStyle().setTop(top, Style.Unit.PX);
            enableButtonsBasedOnPosition();
        }
    }

    private void scrollDown() {
        if (!(top <= stopPosition)) {
            top = top - cellHeight;
            middlePanel.getElement().getStyle().setTop(top, Style.Unit.PX);
            enableButtonsBasedOnPosition();
        }
    }

    public int getHeight(){
        if (cellHeight==0) {
           cellHeight = 22; }
        int height =  (cellHeight * maxNumVisibleCells) +30; /*30 for top and bottom scroll buttons*/
        return height;
    }
    
    public int getMaxNumVisibleRows(){
        return maxNumVisibleCells;
    }

    /**
     * To set default number of rows that are visible without scrolling, default = 5
     **/
    public void setMaxNumVisibleCells(final int maxNum) {
        this.maxNumVisibleCells = maxNum;
    }

    /**
     * Show Context Menu with new List of widgets
    **/
    public void show(List<PopCell> cellWidgets) {
        populate(cellWidgets);
        getElement().getStyle().setZIndex(ZIndexHelper.getHighestZIndex());
        super.show();
    }
}
