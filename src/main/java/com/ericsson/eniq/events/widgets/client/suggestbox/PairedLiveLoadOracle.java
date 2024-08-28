package com.ericsson.eniq.events.widgets.client.suggestbox;

import com.ericsson.eniq.events.common.client.service.IServiceProperties;
import com.ericsson.eniq.events.common.client.url.Url;

/*
* This Oracle takes a parameter from another component set with setQueryParam, set name type
*
* */
public class PairedLiveLoadOracle extends LiveLoadOracle {

    private String paramName = "";

    private final GetParameter parameterProvider;

    public PairedLiveLoadOracle(final String url, final String liveloadRoot, IServiceProperties serviceProperties,
            GetParameter parameterProvider) {
        super(url, liveloadRoot, serviceProperties);
        this.parameterProvider = parameterProvider;
    }

    public void setParamName(final String paramName) {
        this.paramName = paramName;
    }

    private String getParamName() {
        return this.paramName;
    }

    @Override
    protected Url getRequestData(String query) {
        Url url = super.getRequestData(query);
        url.setParameter(getParamName(), parameterProvider.getParameterValue());

        return url;
    }

    public interface GetParameter {
        String getParameterValue();
    }

}
