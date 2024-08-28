package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import static com.ericsson.eniq.events.widgets.client.common.Constants.INVALID_NUMERIC_GRID_FILTER_INFO_MSG;
import static com.ericsson.eniq.events.widgets.client.common.Constants.INVALID_NUMERIC_GRID_FILTER_TOOL_TIP;
import com.ericsson.eniq.events.widgets.client.grids.filter.DoubleFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class DoubleEditFilterView extends NumberEditFilterView<DoubleFilter, Double> {

    public DoubleEditFilterView(final FilterType[] filterTypes, final FilterType selectedFilterType, final Double filter) {
        super(filterTypes, selectedFilterType, filter);
        
        final TextBox editFilterTextox = getEditFilterTextBox();
        editFilterTextox.addKeyUpHandler(new KeyUpHandler() {
            
            @Override
            public void onKeyUp(KeyUpEvent event) {
                try {
                    Double.parseDouble(editFilterTextox.getText());
                    editFilterTextox.removeStyleName(getResourceBundle().style().invalidText());
                    editFilterTextox.setTitle("");
                } catch (NumberFormatException e) {
                    editFilterTextox.addStyleName(getResourceBundle().style().invalidText());
                    editFilterTextox.setTitle(INVALID_NUMERIC_GRID_FILTER_TOOL_TIP);
                }
                
            }
        });
    }

    @Override
    public DoubleFilter getFilter() throws NumberFormatException{
        try{
        return new DoubleFilter(lastSelectedType, Double.parseDouble(getEditFilterTextBox().getText().trim()));
        }catch(NumberFormatException e){
            throw new NumberFormatException(INVALID_NUMERIC_GRID_FILTER_INFO_MSG);
        }
    }

}