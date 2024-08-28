/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.togglebuttons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.impl.FocusImpl;

/**
 * @author eeikbe
 * @since 07/2012
 */
public class ToggleButtonMiniOnOff extends ButtonBase {

    static final ToggleButtonMiniOnOffResourceBundle resources;

    static {
        resources = GWT.create(ToggleButtonMiniOnOffResourceBundle.class);
        resources.css().ensureInjected();
    }

    static final FocusImpl impl = FocusImpl.getFocusImplForPanel();

    private boolean isToggledOn = true;

    private String toolTip = null;
    private int tabIndex = 0;

    private MyColor toggleColor = null;

    public @UiConstructor
    ToggleButtonMiniOnOff(final String color, final String toolTip){
        super(impl.createFocusable());
        this.toggleColor = new MyColor(color);
        this.toolTip = toolTip;

        this.setStyleName(resources.css().toggleMini());

        this.setTitle(this.toolTip);
        getElement().getStyle().setBackgroundColor(this.toggleColor.getHexColor());

        this.addClickHandler( new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                toggleClicked();
            }
        });

        this.addMouseOverHandler( new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                toggleOnMouseOver();
            }
        });

        this.addMouseOutHandler( new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                toggleOnMouseOut();            }
        });

        this.addKeyPressHandler( new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER){
                    toggleKeyPress();
                }
            }
        });
     }

    /**
     * Set the color of a toggle button after it's created.
     * @param color
     */
    public void setToggleColor(final String color){
        this.toggleColor = new MyColor(color);
        if (isToggledOn){
            setToggleOn();
        }else{
            setToggleOff();
        }
    }

    //This is called onClick event from Mouse.
    private void toggleClicked(){
        if(this.isEnabled()){
            if(isToggledOn){
                setToggleOff();
            }else{
                setToggleOn();
            }
        }
    }

    //This is called onMouseOver.
    private void toggleOnMouseOver(){
        if(this.isEnabled()){
            if(!isToggledOn){
                getElement().getStyle().setBackgroundColor(this.toggleColor.createRGBA(".60"));
            }
        }
    }

    //This is called onMouseOut.
    private void toggleOnMouseOut(){
        if(this.isEnabled()){
            if(!isToggledOn){
                getElement().getStyle().setBackgroundColor(this.toggleColor.createRGBA("0"));
            }
        }
    }

    //This is called onKeyPress event for the EnterKey.
    private void toggleKeyPress(){
        if(this.isEnabled()){
            if(isToggledOn){
                setToggleOff();
            }else{
                setToggleOn();
            }
        }
    }

    @Override
    public void setTabIndex(int index) {
        if(index > 0){
            //set the tabindex only if it's not -1.
            this.tabIndex = index;
        }else if(index == 0){
            super.setTabIndex(-1);
        }else{
            super.setTabIndex(index);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled){
            //set the button enabled...
            //reset the original tab index.
            setTabIndex(this.tabIndex);
            if (isToggledOn){
                setToggleOn();
            }else if (!isToggledOn){
                setToggleOff();
            }
        }else{
            //set the tab index on the button to -1. Tabbing is now skipped for this button.
            this.setTabIndex(-1);
            if (isToggledOn){
                setToggleOnDisabled();
            }else if (!isToggledOn){
                setToggleOffDisabled();
            }
        }
        super.setEnabled(enabled);
    }

    /**
     * Get the status of the toggle.
     * @return  true: on, false: off
     */
    public boolean getIsToggleOn(){
        return isToggledOn;
    }

    /**
     * The the Hex color of the toggle.
     * @return
     */
    public String getColor(){
        return toggleColor.getHexColor();
    }

    /**
     * Set the toggle button on.
     * This does not fire an event on the status change of the button.
     */
    public void setToggleOn(){
        this.removeStyleName(resources.css().toggleMiniOff());
        this.addStyleName(resources.css().toggleMiniOn());
        getElement().getStyle().setBackgroundColor(this.toggleColor.createRGBA("1"));
        this.isToggledOn = true;
        this.getElement().setAttribute("checked", "on");
    }

    /**
     * Set the toggle button off.
     * This does not fire an event on the status change of the button.
     */
    public void setToggleOff(){
        this.removeStyleName(resources.css().toggleMiniOn());
        this.addStyleName(resources.css().toggleMiniOff());
        getElement().getStyle().setBackgroundColor(this.toggleColor.createRGBA("0"));
        this.isToggledOn = false;
        this.getElement().setAttribute("checked", "off");
    }

    private void setToggleOnDisabled(){
        //Toggle button disabled and toggle set to true
        this.removeStyleName(resources.css().toggleMiniOn());
        this.removeStyleName(resources.css().toggleMiniOffDisabled());
        this.addStyleName(resources.css().toggleMiniOnDisabled());
        getElement().getStyle().setBackgroundColor(this.toggleColor.createRGBA(".50"));
        this.isToggledOn = true;        
    }

    public void setToggleOffDisabled(){
        //Toggle button disabled and toggle set to false
        this.removeStyleName(resources.css().toggleMiniOff());
        this.removeStyleName(resources.css().toggleMiniOnDisabled());
        this.addStyleName(resources.css().toggleMiniOffDisabled());
        getElement().getStyle().setBackgroundColor(this.toggleColor.createRGBA("0"));
        this.isToggledOn = false;
    }


    public void setToggledOn(boolean value){
        if(this.isEnabled()) {
            if (value){
                //Toggle button enabled and toggle set to true
                setToggleOn();
            }else{
                //Toggle button enabled and toggle set to false
                setToggleOff();
            }
        } else{
            if (value){
                //Toggle button disabled and toggle set to true
                setToggleOnDisabled();
            }else{
                //Toggle button disabled and toggle set to false
                setToggleOffDisabled();
            }
        }
    }

    public void setSize(final double height, final double width, final Style.Unit unit) {
        getElement().getStyle().setHeight(height, unit);
        getElement().getStyle().setWidth(width, unit);
    }

    private class MyColor {
        
        private String hexColor = null;
        Integer r = 0;
        Integer g = 0;
        Integer b = 0;
        
        MyColor(final String hexColor){
            this.hexColor = hexColor;
            
            r = Integer.valueOf(this.hexColor.substring( 1, 3 ), 16 );
            g = Integer.valueOf(this.hexColor.substring( 3, 5 ), 16 );
            b = Integer.valueOf(this.hexColor.substring( 5, 7 ), 16 );
        }

        /**
         * Get the Red value.
         * @return Integer red value.
         */
        public Integer getRed(){
            return r;
        }

        /**
         * Get the Green value.
         * @return Integer green value.
         */
        public Integer getGreen(){
            return g;
        }

        /**
         * Get the Blue value.
         * @return Integer blue value.
         */
        public Integer getBlue(){
            return b;
        }

        /**
         * Get the rgb and set the alpha.
         * This method will return the following String:
         * rgba(red_value,green_value,blue_value,alpha)
         * @param alpha
         * @return
         */
        public String createRGBA(String alpha){
            StringBuilder stringBuilder = new StringBuilder("rgba(");
            stringBuilder.append(getRed());
            stringBuilder.append(",");
            stringBuilder.append(getGreen());
            stringBuilder.append(",");
            stringBuilder.append(getBlue());
            stringBuilder.append(",");
            stringBuilder.append(alpha);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
        
        public String getHexColor(){
            return this.hexColor;
        }
    }
}
