package com.ericsson.eniq.events.widgets.client.suggestbox;

import com.ericsson.eniq.events.common.client.json.IJSONArray;
import com.ericsson.eniq.events.common.client.json.IJSONObject;
import com.ericsson.eniq.events.common.client.json.JsonDataParserUtils;
import com.ericsson.eniq.events.common.client.json.JsonObjectWrapper;
import com.ericsson.eniq.events.common.client.service.*;
import com.ericsson.eniq.events.common.client.url.Url;
import com.ericsson.eniq.events.common.client.url.UrlUtils;
import com.ericsson.eniq.events.widgets.client.dialog.MessageDialog;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ericsson.eniq.events.common.client.CommonConstants.*;

public class LiveLoadOracle extends MultiWordSuggestOracle {

    private static final int SUGGESTION_TIMER_DELAY = 500;

    protected static final String WHITESPACE_STRING = " ";

    private static final String NORMALIZE_TO_SINGLE_WHITE_SPACE = "\\s+";

    public final static String UNDEFINED_SERVER_ERROR = "An undefined error message was received!";

    private String defaultOption = null; //for example "All Cells" - only available if there are valid options available

    protected String liveLoadRoot;

    private String url;

    private Timer t;

    protected Request request;

    protected Callback callback;

    private final IDataService dataService;

    private final MessageDialog errorDialog = new MessageDialog();

    private final IServiceProperties serviceProperties;

    /** Flag to prevent user from clicking on liveload multiple times while loading i.e. prevent popup from showing and hiding multiple times **/
    private boolean isLoading;

    public LiveLoadOracle(final IServiceProperties serviceProperties) {
        this("", "", serviceProperties);
    }

    public LiveLoadOracle(final String url, final String liveloadRoot, final IServiceProperties serviceProperties) {
        super();
        this.liveLoadRoot = liveloadRoot;
        this.url = url;
        this.serviceProperties = serviceProperties;
        dataService = new DataServiceImpl(serviceProperties);
    }

    public void init(final String url, final String liveloadRoot) {
        this.liveLoadRoot = liveloadRoot;
        this.url = url;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.MultiWordSuggestOracle#requestSuggestions(com.google.gwt.user.client.ui.SuggestOracle.Request, com.google.gwt.user.client.ui.SuggestOracle.Callback)
     */
    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        final String query = normalizeSearch(request.getQuery());
        this.request = request;
        request.setLimit(getMaxLiveLoadLimit());
        this.callback = callback;
        isLoading = true;
        if (t != null) {
            t.cancel();
        }
        t = new Timer() {

            @Override
            public void run() {
                final Url urlParams = getRequestData(query);
                dataService.performRemoteOperation(RestfulRequestBuilder.State.GET, url, UrlUtils.build(urlParams),
                        new LiveLoadRequestCallback());
            }
        };
        t.schedule(SUGGESTION_TIMER_DELAY);
    }

    /**
     * @return
     */
    protected Integer getMaxLiveLoadLimit() {
        return Integer.valueOf(serviceProperties.getMaxRowsLiveLoadValue());
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.MultiWordSuggestOracle#requestDefaultSuggestions(com.google.gwt.user.client.ui.SuggestOracle.Request, com.google.gwt.user.client.ui.SuggestOracle.Callback)
     */
    @Override
    public void requestDefaultSuggestions(final Request request, final Callback callback) {
        this.request = request;
        request.setLimit(getMaxLiveLoadLimit());
        this.callback = callback;
        isLoading = true;
        /** Execute in deferred command due to height calculation incorrect in current event loop **/
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                final Url urlParams = getRequestData("");

                dataService.performRemoteOperation(RestfulRequestBuilder.State.GET, url, UrlUtils.build(urlParams),
                        new LiveLoadRequestCallback());
            }
        });
    }

    protected Url getRequestData(final String query) {
        final Url url = new Url();
        url.setParameter("callback", "transId0");
        if (query != null && !query.isEmpty()) {
            url.setParameter("query", query);
        }
        url.setParameter("maxRows", getMaxRowsProperty());
        url.setParameter("sortField", "null");
        url.setParameter("sortDir", "NONE");
        url.setParameter("offset", "0");
        url.setParameter("start", "0");
        url.setParameter("limit", CELL_LIST_OFFSET_LIMIT + "");
        url.setParameter("tzOffset", DateTimeFormat.getFormat(CommonParamUtil.TIME_ZONE_DATE_FORMAT).format(new Date()));

        return url;
    }

    /**
     * Normalize the search key by making it lower case, removing multiple spaces,
     * apply whitespace masks, and make it lower case.
     */
    private String normalizeSearch(String search) {
        search = search.replaceAll(NORMALIZE_TO_SINGLE_WHITE_SPACE, WHITESPACE_STRING);
        return search.toLowerCase().trim();
    }

    String getMaxRowsProperty() {
        return serviceProperties.getMaxRowsLiveLoadValue();
    }

    public void setDefaultOption(final String defaultOption) {
        this.defaultOption = defaultOption;
    }

    private String getDefaultOption() {
        return this.defaultOption;
    }

    private class LiveLoadRequestCallback implements RequestCallback {
        @Override
        public void onResponseReceived(final com.google.gwt.http.client.Request httpRequest,
                                       final com.google.gwt.http.client.Response httpResponse) {
            isLoading = false;
            final JSONValue jsonValue;
            final String data = httpResponse.getText();
            if (data.contains("transId0"))  {
                final String subStr = data.replace("transId0(", "");
                final String jsonStr = subStr.substring(0, subStr.lastIndexOf(")"));
                jsonValue = JsonDataParserUtils.parse(jsonStr, true);
            } else  {
                jsonValue = JsonDataParserUtils.parse(data, true);
            }

            if (jsonValue != null && checkData(jsonValue)) {
                parseResponseJson(jsonValue);
            }
        }

        @Override
        public void onError(final com.google.gwt.http.client.Request request, final Throwable exception) {
            isLoading = false;
            errorDialog.show("Liveload Failure", exception.getMessage(), MessageDialog.DialogType.ERROR);
        }

        /**
         * Copied from JSON utils with changes due to the fact that the expected message format is only when there is an error for this
         * liveload, otherwise the successful result JSON is in GXT format
         * @param jsonValue
         * @return
         */
        private boolean checkData(final JSONValue jsonValue) {
            final JsonObjectWrapper metaData = new JsonObjectWrapper(jsonValue.isObject());

            final String success = metaData.getString(SUCCESS);
            /** No success parameter in response, assume response is correct and in the GXT expected format **/
            if (success == null || success.isEmpty()) {
                return true;
            }
            if (!TRUE.equalsIgnoreCase(success)) {

                String error = metaData.getString(ERROR_DESCRIPTION);
                // server has been known to pass success false with no error message
                if (error.length() == 0) {
                    error = UNDEFINED_SERVER_ERROR; // success flag failed
                }
                final MessageDialog errorDialog = new MessageDialog("Error", error, MessageDialog.DialogType.ERROR);
                errorDialog.show();
                errorDialog.center();
                return false;

            }
            return true;
        }
    }

    protected void parseResponseJson(final JSONValue jsonValue) {
        final JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.isObject());
        final IJSONArray searchObjectArray = json.getArray(liveLoadRoot);

        final List<String> candidates = new ArrayList<String>();

        if (searchObjectArray.size() > 0 && getDefaultOption() != null) {
            candidates.add("All Cells");
        }

        for (int i = 0; i < searchObjectArray.size(); i++) {
            final IJSONObject obj = searchObjectArray.get(i);
            final String candidate = obj.getString("id");
            if (candidate.length() == 0 || candidate.matches(WHITESPACE_STRING)) {
                continue;
            }
            candidates.add(candidate);
        }

        // Respect limit for number of choices.
        final int numberTruncated = Math.max(0, candidates.size() - request.getLimit());
        for (int i = candidates.size() - 1; i > request.getLimit(); i--) {
            candidates.remove(i);
        }

        // Convert candidates to suggestions.
        final List<Suggestion> suggestions = new ArrayList<Suggestion>();
        for (final String candidate : candidates) {
            suggestions.add(getSuggestion(candidate));
        }

        final Response suggestionsResponse = new Response(suggestions);
        suggestionsResponse.setMoreSuggestionsCount(numberTruncated);
        callback.onSuggestionsReady(request, suggestionsResponse);
    }

    protected MultiWordSuggestion getSuggestion(final String candidate) {
        return new MultiWordSuggestion(candidate, candidate);
    }

    /**
     * @return
     */
    public boolean isLoading() {
        return isLoading;
    }
}
