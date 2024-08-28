/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.widgets.client.monitor;

import com.ericsson.eniq.events.common.client.CommonMain;
import com.ericsson.eniq.events.common.client.events.JsonParseFailEvent;
import com.ericsson.eniq.events.common.client.events.JsonParseFailEventHandler;
import com.ericsson.eniq.events.common.client.gin.CommonInjector;
import com.ericsson.eniq.events.widgets.client.dialog.MessageDialog;
import com.google.web.bindery.event.shared.EventBus;

import static com.ericsson.eniq.events.widgets.client.dialog.MessageDialog.DialogType;

/**
 * Special class for widgets to monitor any events fired by any module to reduce coupling
 * and dependencies.
 * !IMPORTANT!
 * All events are monitored on Common EventBus instance only!
 */
public class EventsMonitor {

    private static CommonInjector commonInjector = CommonMain.getCommonInjector();
    public static final String  SERVER_ERROR =  "Server Error";
    private static final String CONTACT_SYS_ADMIN ="If issue persists contact system administrator";
    private static final String UNEXPECTED_SERVER_RESPONSE = "Unexpected server response, please login again.  " +
            "\n" +CONTACT_SYS_ADMIN;


    // No instance needed of this class
    private EventsMonitor() {
    }

    public static void initMonitor() {
        handleJsonParseFailEvent();
    }

    private static void handleJsonParseFailEvent() {
        EventBus eventBus = commonInjector.getEventBus();
        eventBus.addHandler(JsonParseFailEvent.TYPE, new JsonParseFailEventHandler() {
            @Override
            public void onFail() {
                final MessageDialog messageDialog = new MessageDialog();
                messageDialog.setGlassEnabled(true);
                messageDialog.show(SERVER_ERROR, UNEXPECTED_SERVER_RESPONSE, DialogType.ERROR);
            }
        });
    }
}
