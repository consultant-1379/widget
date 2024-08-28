package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.drill.DrillCategoryType;
import com.ericsson.eniq.events.widgets.client.drill.DrillDialog;
import com.ericsson.eniq.events.widgets.client.drill.IDrillCallback;
import com.ericsson.eniq.events.widgets.client.drill.Point;
import com.ericsson.eniq.events.widgets.client.mapcontextmenu.MapContextMenu;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:20
 * To change this template use File | Settings | File Templates.
 */
public class DrillDownSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final MapContextMenu drillExample = new MapContextMenu();
        final DrillDialog drillDialog = new DrillDialog();
        final DrillCategoryType drillType= new DrillCategoryType("Example","Drill Down Option 1");
        final DrillCategoryType drillType2= new DrillCategoryType("Example","Drill Down Option 2");
        final DrillCategoryType drillType3= new DrillCategoryType("Example","Drill Down Option 3");
        final IDrillCallback callback = new IDrillCallback() {
            @Override
            public void onDrillDownSelected(String drillDownTargetId) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        drillDialog.addDrillOption(drillType, callback);
        drillDialog.addDrillOption(drillType2, callback);
        drillDialog.addDrillOption(drillType3, callback);
        callback.onDrillDownSelected("Option 1");

        Button textContext = new Button("Show Drill Down Example");
        textContext.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int xint = event.getClientX()+ Window.getScrollLeft();
                int yint = event.getClientY()+ Window.getScrollTop()+5;
                Point point = new Point(xint,yint);
//                        drillDialog.createPanel();
//                        drillDialog.alignDrillDialog(point);
                drillDialog.show(point);
            }
        });

        final String source = wrapCode(sourceBundle.drillDown().getText());
        return wrapWidgetAndSource(textContext, source);
    }


}
