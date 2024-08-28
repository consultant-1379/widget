/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.widgets.client.threshold;

import com.ericsson.eniq.events.common.client.threshold.events.ThresholdDialogHideEvent;
import com.ericsson.eniq.events.common.client.threshold.events.ThresholdDialogHideEventHandler;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.google.gwt.event.shared.HandlerManager;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class ThresholdsDialog {

    private final Dialog dialog = new Dialog();
    private final HandlerManager handlerManager = new HandlerManager(this);

    public ThresholdsDialog() {
        dialog.setHeading("Edit Thresholds");
        dialog.setButtons("");
		dialog.setWidth(552);
        dialog.setAutoHeight(true);
        dialog.addListener(Events.Hide, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(final BaseEvent event) {
                if (Events.Hide.equals(event.getType())) {
                    handlerManager.fireEvent(new ThresholdDialogHideEvent());
                }
            }
        });
    }

    public void show() {
        dialog.show();

    }

    public void hide() {
        dialog.hide();
    }

    public void clear() {
        dialog.removeAll();
    }

    public void setContent(final Widget content) {
        dialog.removeAll();
        dialog.add(content);
    }

    public HandlerRegistration addHideEventHandler(final ThresholdDialogHideEventHandler handler) {
        return handlerManager.addHandler(ThresholdDialogHideEvent.TYPE, handler);
    }
}
