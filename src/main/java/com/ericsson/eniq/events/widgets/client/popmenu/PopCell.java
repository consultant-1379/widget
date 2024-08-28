/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.popmenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author eeikbe
 * @since 12/2012
 */
public class PopCell extends FocusPanel{

   static final PopCellResourceBundle resources;

   static{
       resources = GWT.create(PopCellResourceBundle.class);
       resources.css().ensureInjected();
   }
   FlowPanel innerPanel = new FlowPanel();
   private double height = 0.0;

   public PopCell(final double height, final double width){
       this.add(innerPanel);
       this.height = height;
       innerPanel.addStyleName(resources.css().content());
       this.getElement().getStyle().setHeight(height, Style.Unit.PX);
       this.getElement().getStyle().setWidth(width, Style.Unit.PX);
       this.getElement().getStyle().setBackgroundColor("transparent");


       this.addMouseOverHandler( new MouseOverHandler() {
           @Override
           public void onMouseOver(MouseOverEvent event) {
               onMouseOverHandler();
           }
       });

       this.addClickHandler(new ClickHandler() {
           @Override
           public void onClick(ClickEvent event) {
                onClickHandler();
           }
       });

       this.addMouseOutHandler(new MouseOutHandler() {
           @Override
           public void onMouseOut(MouseOutEvent event) {
               onMouseOutHandler();
           }
       });
   }

    public void addWidget(final Widget widget){
        if(setBackgroundTransparent()){
            widget.getElement().getStyle().setBackgroundColor("transparent");
        }
        widget.getElement().getStyle().setFloat(Style.Float.LEFT);
        widget.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        innerPanel.add(widget);
    }


    /**
     * Override this method to provide your own onMouseOverHandler.
     */
   public void onMouseOverHandler(){
       //Override this method to provide your own onMouseOverHandler.
   }

    /**
     * Override this method to provide your own onMouseOutHandler.
     */
    public void onMouseOutHandler(){
        //Override this method to provide your own onMouseOutHandler.
    }

    /**
     * Override this method to provide your own onClickHandler.
     */
   public void onClickHandler(){
       //Override this method to provide your own onClickHandler.
   }

    /**
     * Override this method if you don't want the background color of the added widget to be set to transparent.
     * Added widgets are set to transparent so that the user can see mouse over events (cell background changes color)
     * @return
     */
   public boolean setBackgroundTransparent(){
       return true;
   }


    /**
     * Get the height in Unit.PX of the cell.
     * @return double
     */
   public double getHeight(){
        return height;
   }
}
