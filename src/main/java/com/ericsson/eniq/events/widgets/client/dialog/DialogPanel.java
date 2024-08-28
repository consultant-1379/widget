package com.ericsson.eniq.events.widgets.client.dialog;

import com.google.gwt.user.client.ui.IsWidget;

public interface DialogPanel extends IsWidget {
    String getTitle();

    void setTitle(String title);

    String getMessage();

    void setMessage(String message);

    void setDialogType(MessageDialog.DialogType type);
}
