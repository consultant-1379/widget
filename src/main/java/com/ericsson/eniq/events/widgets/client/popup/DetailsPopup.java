/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.popup;

import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author eeikbe
 * @since 12/2012
 */
public class DetailsPopup extends PopupPanel {

    public enum CellType{
        CELL, CONTROLLER
    };

    private CellType type = CellType.CELL;

    static final DetailsPopupResourceBundle resources;

    static {
        resources = GWT.create(DetailsPopupResourceBundle.class);
        resources.css().ensureInjected();
    }

    /**
     * The timer used to delay the show.
     */
    private Timer showTimer;
    
    private FlowPanel header;
    private FlowPanel content;
    private Grid grid;

    public DetailsPopup(final String color, final String title, final LinkedHashMap<String, String> popupData){
        super(true);
        header = new FlowPanel();
        content = new FlowPanel();
        grid = new Grid(3,2);
        grid.addStyleName(resources.css().contents());
        header.addStyleName(resources.css().header());
        setCell(color);
        setPopupTitle(title);
        setContents(popupData);
        content.add(header);
        content.add(grid);
        this.addStyleName(resources.css().gwtPopupPanel());
        setWidget(content);
        getElement().getStyle().setZIndex(ZIndexHelper.getHighestZIndex());
    }

    public void setType(CellType type){
        this.type = type;
    }
    /**
     * Set the cell color.
     * @param color
     */
    public void setCell(final String color){
        final HTMLPanel htmlPanel;
        if(type == CellType.CELL){
            htmlPanel = new HTMLPanel("<svg xmlns='http://www.w3.org/2000/svg' version='1.1'>" +
                    "<circle cx='15' cy='12' r='6' stroke='#333333' stroke-width='1' fill='" + color+ "'/>" +
                    "</svg>");
        }else{
            htmlPanel = new HTMLPanel("<svg xmlns='http://www.w3.org/2000/svg' version='1.1'>" +
                    "<circle cx='15' cy='12' r='6' stroke='#333333' stroke-width='1' fill='" + color+ "'/>" +
                    "<polygon style='fill:#333333;fill-opacity:0.3;stroke:white;stroke-width:1' points='15,8 18,15 12,15'>"+
                    "</svg>");
        }

        htmlPanel.addStyleName(resources.css().iconHolder());
        header.add(htmlPanel);
    }

    /**
     * Set the title of the popup.
     * @param title
     */
    public void setPopupTitle(final String title){
        Label lb = new Label(title);
        lb.addStyleName(resources.css().titleLabel());
        header.add(lb);
    }

    /**
     * Add the contents of the popup.
     * @param popupData
     */
    public void setContents(final LinkedHashMap<String, String> popupData){
        int row    = 0;
        for(Map.Entry<String, String> entry: popupData.entrySet()){
            grid.setText(row, 0, entry.getKey());
            grid.setText(row++, 1, entry.getValue());
        }
    }

    /**
     * Shows the popup and attach it to the page. It must have a child widget
     * before this method is called.
     */
    public void showWithDelay() {
            if (showTimer != null) {
                showTimer.cancel();
                showTimer = null;
            }
            showTimer = new Timer() {
                @Override
                public void run() {
                    show();
                }
            };

            //.5 second delay before the popup is shown, why? because it looks better ;)
            showTimer.schedule(500);
    }

    /**
     * Hides the popup and detaches it from the page. This has no effect if it is
     * not currently showing.
     */
    @Override
    public void hide() {
        try {
            //Cancel the timer...
            if(showTimer != null){
                showTimer.cancel();
                showTimer = null;
            }

            //Hide the popup.
            super.hide();
        } catch (Exception e) {
           //test fix for IE - debugging
        }
    }
}
