/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.suggestbox;

import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.google.gwt.user.client.ui.*;

/**
 * @author eeoiobr
 * @since 06/2013
 * This class overrides SuggestBox and fixes issue with z-index
 */
public class ExtendedSuggestDisplay extends SuggestBox.DefaultSuggestionDisplay  implements HasAnimation {

    private  PopupPanel suggestionPopup;

    @Override
    protected PopupPanel createPopup() {
        suggestionPopup=super.createPopup();
        bringToFront();
        return suggestionPopup;
    }

    //Brings the Dropdown to the Front
    public void bringToFront(){
        if (suggestionPopup==null){
            return;
        }
        int zIndex=ZIndexHelper.getHighestZIndex();
        suggestionPopup.getElement().getStyle().setZIndex(zIndex);
    }

    public boolean isSuggestionListShowing() {
        return suggestionPopup.isShowing();
    }
}
