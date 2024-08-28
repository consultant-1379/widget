package com.ericsson.eniq.events.widgets.client.utilities;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class ZIndexHelper {
    private static int zIndex = 1;

    public static void setzIndex(final int zIndex) {
        ZIndexHelper.zIndex = Math.max(zIndex, ZIndexHelper.zIndex);
    }

    public static int getHighestZIndex() {
        return getHighestZIndex(null);
    }

    public static int getHighestZIndex(final JavaScriptObject jso) {
        return Math.max(zIndex, (int) calculateHighestZIndex(jso));
    }

    private static native double calculateHighestZIndex(JavaScriptObject obj) /*-{
                                                                              var highestIndex = 0;
                                                                              try {
                                                                              var currentIndex = 0;
                                                                              var elArray = Array();
                                                                              if(obj){
                                                                              elArray = obj.getElementsByTagName('*');
                                                                              }else{
                                                                              elArray = $doc.getElementsByTagName('*');
                                                                              }
                                                                              for ( var i = 0; i < elArray.length; i++) {
                                                                              if (elArray[i].currentStyle) {
                                                                              currentIndex = parseInt(elArray[i].currentStyle['zIndex']);
                                                                              } else if (window.getComputedStyle) {
                                                                              currentIndex = parseInt($doc.defaultView.getComputedStyle(
                                                                              elArray[i], null).getPropertyValue('z-index'));
                                                                              }
                                                                              if (!isNaN(currentIndex) && currentIndex > highestIndex) {
                                                                              highestIndex = currentIndex;
                                                                              }
                                                                              }
                                                                              } catch (err) {
                                                                              console.log(err);
                                                                              }
                                                                              return (highestIndex+1);
                                                                              }-*/;

}
