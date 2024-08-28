package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.popmenu.ClusterPopCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 09:54
 * To change this template use File | Settings | File Templates.
 */
public class PopCellSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        FlowPanel flowPanel = new FlowPanel();

        for (int j = 0; j < 10; j++){
            ClusterPopCell popCell = new ClusterPopCell(24, 200, "#e1de5a", "Test text...");
            final LinkedHashMap<String, String> popupData = new LinkedHashMap<String, String>();
            popupData.put("Cell ID ", "1234567");
            popupData.put("KPI ", "This is a useless KPI");
            popupData.put("Value ", "100"+"%");
            popCell.setPopupData(popupData);
            flowPanel.add(popCell);
        }
        final String source = wrapCode(sourceBundle.textPopCell().getText());
        return wrapWidgetAndSource(flowPanel, source);
    }
}
