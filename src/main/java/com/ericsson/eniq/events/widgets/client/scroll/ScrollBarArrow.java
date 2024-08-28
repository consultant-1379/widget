package com.ericsson.eniq.events.widgets.client.scroll;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;

/**
 * @author ekurshi
 * @since 14/06/12
 */
public class ScrollBarArrow extends Image {
    private boolean runTask;

    private final Timer timer;

    private PerformTask task;

    private final MyNativePreviewHandler handler = new MyNativePreviewHandler();

    private HandlerRegistration handlerRegistration;

    private boolean enabled = true;

    private static final double ARROW_DISABLE_OPACITY = 0.6;

    public ScrollBarArrow() {
        super();
        //workaround for scrolling until mouse is down an arrow
        timer = new Timer() {

            @Override
            public void run() {
                if (runTask && enabled) {
                    task.doTask();
                    schedule(100);
                } else {
                    timer.cancel();
                }
            }
        };
    }

    @Override
    public void onBrowserEvent(final Event event) {
        switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEDOWN:
            final int nativeButton = event.getButton();
            if (nativeButton == NativeEvent.BUTTON_LEFT || nativeButton == NativeEvent.BUTTON_MIDDLE) {
                runTask = true;
                timer.run();
            }
            handlerRegistration = Event.addNativePreviewHandler(handler);
            event.preventDefault();//To stop image drag
            break;
        default:
            super.onBrowserEvent(event);
        }
    }

    public void setTask(final PerformTask performTask) {
        task = performTask;
    }

    protected void cancelTask() {
        runTask = false;
        handlerRegistration.removeHandler();
    }

    interface PerformTask {
        void doTask();
    }

    public void setEnable(final boolean enable) {
        this.enabled = enable;
        double opacity = 1;
        if (!enable) {
            opacity = ARROW_DISABLE_OPACITY;
        }
        getElement().getStyle().setOpacity(opacity);
    }

    private class MyNativePreviewHandler implements Event.NativePreviewHandler {

        @Override
        public void onPreviewNativeEvent(final Event.NativePreviewEvent event) {
            final int type = event.getTypeInt();
            if (enabled && (type == Event.ONMOUSEUP || type == Event.ONMOUSEOUT)) {
                final NativeEvent nativeEvent = event.getNativeEvent();
                final int button = nativeEvent.getButton();
                if (button == NativeEvent.BUTTON_LEFT || button == NativeEvent.BUTTON_MIDDLE) {
                    if (type == Event.ONMOUSEUP) {
                        cancelTask();
                        event.getNativeEvent().preventDefault();//stop bubbling
                    } else if (type == Event.ONMOUSEOUT) {
                        cancelTask();
                        event.getNativeEvent().preventDefault();//stop bubbling
                    }
                }
            }
        }
    }

}