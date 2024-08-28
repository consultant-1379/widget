/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.popmenu;

import com.ericsson.eniq.events.widgets.client.popup.DetailsPopup;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import java.util.LinkedHashMap;

/**
 * @author eeikbe
 * @since 12/2012
 */
public class ClusterPopCell extends PopCell{
    private double height = 0.0;
    private double width  = 0.0;
    final private double LEFT_ICON_WIDTH = 30;
    LinkedHashMap<String, String> popupData;

    private String cellColor;
    private String detailsPopupTitle = "Cell Details";
    
    DetailsPopup detailsPopup;

    public ClusterPopCell(final double height, final double width, final String color, final String name) {
        super(height, width);
        this.height = height;
        this.width = width;
        this.cellColor = color;
        this.addCellImage(color);
        this.addCellName(name);
    }

    /**
     * Set the title of the details popup (popup that's displayed on mouse over).
     * @param detailsPopupTitle
     */
    public void setDetailsPopupTitle(final String detailsPopupTitle){
        this.detailsPopupTitle = detailsPopupTitle;    
    }

    /**
     * Get the title of the details popup (popup that's displayed on mouse over).
     * @return
     */
    public String getDetailsPopupTitle(){
        return this.detailsPopupTitle;
    }

    private void addCellImage(final String color){
        final HTMLPanel htmlPanel = new HTMLPanel("<svg xmlns='http://www.w3.org/2000/svg' version='1.1'>" +
                "<circle cx='15' cy='12' r='6' stroke='#333333' stroke-width='1' fill='" + color+ "'/>" +
                "</svg>");
        htmlPanel.getElement().getStyle().setHeight(100, Style.Unit.PCT);
        htmlPanel.getElement().getStyle().setWidth(LEFT_ICON_WIDTH, Style.Unit.PX);
        this.addWidget(htmlPanel);
    }
    
    private void addCellName(final String cellName){
        Label lb = new Label(cellName);
        lb.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
        lb.getElement().getStyle().setWidth(width - LEFT_ICON_WIDTH, Style.Unit.PX);
        lb.getElement().getStyle().setPaddingTop(5, Style.Unit.PX);
        this.addWidget(lb);
    }


    /**
     * Set the popup data for the hovered KPI.
     * @param popupData
     */
    public void setPopupData(final LinkedHashMap<String, String> popupData){
        this.popupData = popupData;
    }


    /**
     * Override this method to provide your own onClickHandler.
     */
    @Override
    public void onClickHandler() {
        //Do nothing right now.
    }

    /**
     * Override this method to provide your own onMouseOverHandler.
     */
    @Override
    public void onMouseOverHandler() {

        try {
            detailsPopup = new DetailsPopup(this.cellColor, getDetailsPopupTitle(), popupData);
            detailsPopup.setPopupPosition(getAbsoluteLeft() + ((int)width-10), getAbsoluteTop() - 10);
            detailsPopup.showWithDelay();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Override this method to provide your own onMouseOutHandler.
     */
    @Override
    public void onMouseOutHandler() {
        try {
            if(detailsPopup!=null || detailsPopup.isShowing()){
               detailsPopup.hide();
               detailsPopup.clear();
                //clear any memory, don't leave the details popup loitering around in memory.
               detailsPopup = null;
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
