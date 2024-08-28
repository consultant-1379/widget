package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.mapcontextmenu.MapContextMenu;
import com.ericsson.eniq.events.widgets.client.popmenu.ClusterPopCell;
import com.ericsson.eniq.events.widgets.client.popmenu.PopCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 09:52
 * To change this template use File | Settings | File Templates.
 */
public class MapContextMenuSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final MapContextMenu mcm = new MapContextMenu();
        List<PopCell> widgets = new ArrayList<PopCell>();

        for (int j = 0; j < 10; j++) {
            ClusterPopCell popCell = new ClusterPopCell(24, 200, "#e1de5a", "Test text..." + j);
            final LinkedHashMap<String, String> popupData = new LinkedHashMap<String, String>();
            popupData.put("Cell ID ", "1234567");
            popupData.put("KPI ", "This is a useless KPI");
            popupData.put("Value ", "100" + "%");
            popCell.setPopupData(popupData);
            popCell.setDetailsPopupTitle("My Title");
            widgets.add(popCell);
        }
        mcm.populate(widgets);

        Button textContext = new Button("Show Map Context Menu");
        textContext.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                // Reposition the popup relative to the button
                com.google.gwt.user.client.ui.Widget source = (com.google.gwt.user.client.ui.Widget) event.getSource();
                int left = source.getAbsoluteLeft() + 10;
                int top = source.getAbsoluteTop() + 10;
                mcm.setPopupPosition(left, top);
                mcm.show();
            }
        });

        final String source = wrapCode(sourceBundle.mapContextMenu().getText());
        return wrapWidgetAndSource(textContext, source);
    }
}
