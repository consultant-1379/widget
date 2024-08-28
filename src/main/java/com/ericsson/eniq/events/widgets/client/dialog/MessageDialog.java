package com.ericsson.eniq.events.widgets.client.dialog;

import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DialogBox;

public class MessageDialog extends DialogBox {

    private static final MessageDialogResourceBundle resources = GWT.create(MessageDialogResourceBundle.class);

    protected final DialogPanel panel;

    private static final int draggableAreaHeight = 20;

    public static enum DialogType {
        INFO, ERROR, WARNING
    }

    private static final DialogType DEFAULT_DIALOG_TYPE = DialogType.INFO;

//    private static final int GLASSPANEL_ZINDEX = 19999;//defined in gwt-PopupPanelGlass

    public static MessageDialog get() {
        return new MessageDialog();
    }

    public MessageDialog() {
        this("", "");
    }

    public void setElementId(final String id) {
        getElement().setId(id);
    }

    public MessageDialog(final String title, final String content) {
        this(title, content, DEFAULT_DIALOG_TYPE);
    }

    public MessageDialog(final String title, final String content, final DialogType type) {
        super(false, false);

        panel = createPanel();

        resources.css().ensureInjected();
        setStyleName(resources.css().dialogBox());

        // Set top padding of the center element of DecoratorPanel, that will be replaced by caption is you make me draggable
        getCellElement(1, 1).getStyle().setPaddingTop(draggableAreaHeight, Style.Unit.PX);

        setDialogType(type);
        setTitle(title);
        setMessage(content);

        setWidget(panel);
    }


    public void makeMeDraggable() {
        final Style style = getCaption().asWidget().getElement().getStyle();
        style.setCursor(Style.Cursor.MOVE);
        style.setHeight(draggableAreaHeight, Style.Unit.PX);
        style.setWidth(100, Style.Unit.PCT);

        // Set also caption parent to be of 20px height
        getCellElement(0, 1).getStyle().setHeight(draggableAreaHeight, Style.Unit.PX);

        // Reset top padding of the center element of DecoratorPanel
        getCellElement(1, 1).getStyle().setPaddingTop(0, Style.Unit.PX);

    }

    public DialogPanel createPanel() {
        final ClickHandler closeHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                hide();
            }
        };

        return new MessageDialogPanel(closeHandler);
    }

    public void show(final String title, final String message) {
        show(title, message, DialogType.INFO);
    }

    public void show(final String title, final String message, final DialogType type) {
        setTitle(title);
        setMessage(message);
        setDialogType(type);
        center();

        //Set the highest ZIndex for the message dialog.
        int zindex = ZIndexHelper.getHighestZIndex();
        if (getGlassElement()!=null){
            getGlassElement().getStyle().setZIndex(zindex);
        }
        getElement().getStyle().setZIndex(zindex+3);
    }

    @Override
    public void center() {
        super.center();
        //as we using max-height, max-width property, visible height/width will different from the offsetHeight/offsetWidth. So we need to centre the dialog according to visible height/width 
        boolean center = false;
        int width = getOffsetWidth();
        int height = getOffsetHeight();
        if (width > 413) {
            width = 413;
            center = true;
        }
        if (height > 400) {
            height = 400;
            center = true;
        }
        if (center) {
            doCenter(width, height, com.google.gwt.user.client.Window.getClientWidth(),
                    com.google.gwt.user.client.Window.getClientHeight(), 0, 0);
        }
    }

    /**
     * @param width
     * @param height
     * @param parentWidth
     * @param parentHeight
     */
    protected void doCenter(final int width, final int height, final int parentWidth, final int parentHeight,
            final int leftOffset, final int topOffset) {
        final int left = (parentWidth - width) >> 1;
        final int top = (parentHeight - height) >> 1;
        setPopupPosition(leftOffset + left, topOffset + top);
    }

    @Override
    public String getTitle() {
        return panel.getTitle();
    }

    @Override
    public void setTitle(final String title) {
        panel.setTitle(title);
    }

    public String getMessage() {
        return panel.getMessage();
    }

    public void setMessage(final String message) {
        panel.setMessage(message);
    }

    public void setDialogType(final DialogType type) {
        panel.setDialogType(type);
        if (type == DialogType.ERROR || type == DialogType.WARNING) {
            setGlassEnabled(true);
        }
    }

    @Override
    protected void onPreviewNativeEvent(final Event.NativePreviewEvent event) {
        if (event.getTypeInt() == Event.ONKEYDOWN) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                hide();
            }
        }
        super.onPreviewNativeEvent(event);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.DialogBox#hide()
     */
    @Override
    public void hide() {
        super.hide();
    }


}
