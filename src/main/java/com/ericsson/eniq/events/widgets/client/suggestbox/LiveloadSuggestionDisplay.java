/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.suggestbox;

import java.util.Collection;

import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionCallback;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author ecarsea
 * @since 2012
 *
 */
public class LiveloadSuggestionDisplay extends DefaultSuggestionDisplay {

    public interface LiveloadSuggestionsReceivedCallback {
        void onSuggestionsReceived(Collection<? extends Suggestion> suggestions);
    }

    private final LiveloadSuggestionsReceivedCallback callback;

    public LiveloadSuggestionDisplay(LiveloadSuggestionsReceivedCallback callback) {
        this.callback = callback;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay#showSuggestions(com.google.gwt.user.client.ui.SuggestBox, java.util.Collection, boolean, boolean, com.google.gwt.user.client.ui.SuggestBox.SuggestionCallback)
     */
    @Override
    protected void showSuggestions(final SuggestBox suggestBox, Collection<? extends Suggestion> suggestions,
            boolean isDisplayStringHTML, boolean isAutoSelectEnabled, final SuggestionCallback callback) {
        super.showSuggestions(suggestBox, suggestions, isDisplayStringHTML, isAutoSelectEnabled, callback);
        this.callback.onSuggestionsReceived(suggestions);
    }
}
