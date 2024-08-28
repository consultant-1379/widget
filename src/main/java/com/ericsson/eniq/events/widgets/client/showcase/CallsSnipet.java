package com.ericsson.eniq.events.widgets.client.showcase;

import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 08/05/13
 * Time: 15:31
 * Calls the Example Widget
 */
public class CallsSnipet {
    AbsolutePanel examplePanel = new AbsolutePanel();

    public AbsolutePanel callClass(final String id) {
        final String className = id + "Snipet";

        examplePanel.clear();
        if (id.equals("breadcrumb")) {
            final BreadcrumbSnipet caller = new BreadcrumbSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("calendar")) {
            final CalendarSnipet caller = new CalendarSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("cogButton")) {
            final CogButtonSnipet caller = new CogButtonSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("collapsePanel")) {
            final CollapsePanelSnipet caller = new CollapsePanelSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("drillDown")) {
            final DrillDownSnipet caller = new DrillDownSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("dropDownMenu")) {
            final DropDownMenuSnipet caller = new DropDownMenuSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("extendedTextBox")) {
            final ExtendedTextBoxSnipet caller = new ExtendedTextBoxSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("extendedSuggestBox")) {
            final ExtendedSuggestBoxSnipet caller = new ExtendedSuggestBoxSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("floatingWindow")) {
            final FloatingWindowSnipet caller = new FloatingWindowSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("imageButton")) {
            final ImageButtonSnipet caller = new ImageButtonSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("mapContextMenu")) {
            final MapContextMenuSnipet caller = new MapContextMenuSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("messageDialog")) {
            final MessageDialogSnipet caller = new MessageDialogSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("messagePanel")) {
            final MessagePanelSnipet caller = new MessagePanelSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("popCell")) {
            final PopCellSnipet caller = new PopCellSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("slideBar")) {
            final SlideBarSnipet caller = new SlideBarSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("textButton")) {
            final TextButtonSnipet caller = new TextButtonSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("thresholdWidget")) {
            final ThresholdWidgetSnipet caller = new ThresholdWidgetSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("titleWindow")) {
            final TitleWindowSnipet caller = new TitleWindowSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("toggleButtonMiniOnOff")) {
            final ToggleButtonMiniOnOffSnipet caller = new ToggleButtonMiniOnOffSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("toggleButtonOnOff")) {
            final ToggleButtonOnOffSnipet caller = new ToggleButtonOnOffSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("toggleRail")) {
            final ToggleRailSnipet caller = new ToggleRailSnipet();
            examplePanel.add(caller.createWidget());
        }
        if (id.equals("grid")) {
            final GWTGridSnipet caller = new GWTGridSnipet();
            examplePanel.add(caller.createWidget());
        }
        return examplePanel;
    }

}
