package com.ericsson.eniq.events.widgets.client.utilities;

import com.ericsson.eniq.events.widgets.client.WidgetsResourceBundle;
import com.ericsson.eniq.events.widgets.client.WidgetsResourceBundleHelper;
import com.ericsson.eniq.events.widgets.client.window.FloatingWindow;
import com.ericsson.eniq.events.widgets.client.window.toolbar.ToolbarItem;
import com.ericsson.eniq.events.widgets.client.window.toolbar.WindowToolbar.Direction;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * 
 * @author ekurshi
 * @since 2012
 * 
 */
public class PrintHelper {
    private static String FRAME_ID = "__printingFrame";

    public static void printWithoutPreview(final String title, final IPrintable printable) {
        final Frame f = new Frame();
        f.removeStyleName("gwt-Frame");
        f.setSize("100%", "100%");
        f.setVisible(false);
        RootPanel.get().add(f);
        f.getElement().setId(FRAME_ID);
        prepareIFrame(title, printable.getPrintableElement());
        f.getElement().getStyle().setBorderWidth(0, Unit.PX);
        doPrint(FRAME_ID);
        RootPanel.get().remove(f);
    }

    public static native void doPrint() /*-{
		$wnd.print();
    }-*/;

    private static HeadElement getHead() {
        final Element element = Document.get().getElementsByTagName("head").getItem(0);
        assert element != null : "HTML Head element required";
        return HeadElement.as(element);
    }

    public static void printPreview(final AbsolutePanel boundaryPanel, final String title, final IPrintable printable,
            final int width, final int height) {
        final Element printableElement = printable.getPrintableElement();
        final ImageResource printPreviewIcon = WidgetsResourceBundleHelper.getBundle().printPreviewIcon();

        final int windowWidth = width + 14;//10px padding,4px border
        final FloatingWindow window = new FloatingWindow(boundaryPanel, printPreviewIcon, windowWidth, height,
                "Print Preview");
        final WidgetsResourceBundle bundle = WidgetsResourceBundleHelper.getBundle();
        final ToolbarItem item = new ToolbarItem("print", new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                doPrint(FRAME_ID);
                window.doClose();
            }
        }, bundle.printIcon());
        item.setToolTip("Print");
        item.setHoverImage(bundle.printHover());
        item.setDisabledImage(bundle.printDisable());
        window.addToolbarItem(item, Direction.LEFT);
        final Frame f = new Frame();
        f.removeStyleName("gwt-Frame");
        f.setSize("100%", "100%");
        window.add(f);
        window.setResizable(false);
        f.getElement().setId(FRAME_ID);
        window.setGlassEnabled(true);
        window.show();
        window.centerWindow();
        window.setMaximizeVisible(false);
        prepareIFrame(title, printableElement);
        f.getElement().getStyle().setBorderWidth(0, Unit.PX);
    }

    public static native String getWindowLocation() /*-{
		var url = $wnd.location.protocol + "//" + $wnd.location.host
				+ $wnd.location.pathname + $wnd.location.search;
		return url;
    }-*/;

    private static void showTraces(final Exception e, final String methodName) {
        final StringBuffer buff = new StringBuffer();
        for (final StackTraceElement el : e.getStackTrace()) {
            buff.append(el.getClassName() + "," + el.getFileName() + "," + el.getLineNumber() + ","
                    + el.getMethodName() + "\n");
        }
        Window.alert(methodName + " :: " + e.getMessage() + ",trace:" + buff.toString());

    }

    private static void prepareIFrame(final String pTitle, final Element printableElement) {
        final String title = (pTitle == null || pTitle.isEmpty()) ? "Untitled" : pTitle;
        final String html = "<html><head><title>"
                + title
                + "</title>"
                + getStylesFromHeadTag()
                + "</head><body style='margin:0px;background-image:none!important;height:auto !important'><div style='width:100%;height:100%;position:absolute;z-index:10;background:transparent;left:0px;right:0px;'></div>"
                + getTrustedHTML(printableElement) + "</body></html>";
        injectHtmlInToFrame(FRAME_ID, html);
    }

    private static String getTrustedHTML(final Element e) {
        final Element tempDiv = DOM.createDiv();
        tempDiv.appendChild(e);
        return SafeHtmlUtils.fromSafeConstant(tempDiv.getInnerHTML()).asString();
    }

    private static native void injectHtmlInToFrame(String frameId, String html) /*-{
		var frame = $doc.getElementById(frameId);
		if (!frame) {
			$wnd.alert("Error: Can't find printing element.");
			return;
		}
		frame = frame.contentWindow;
		var doc = frame.document;
		//doc.clear(); depreciated in ie9
		doc.open();
		doc.write(html);
		doc.close();

    }-*/;

    private static native void doPrint(String frameId) /*-{
		var frame = $doc.getElementById(frameId);
		if (!frame) {
			$wnd.alert("Error: Can't find printing element.");
			return;
		}
		var window = frame.contentWindow;
		window.focus();
		window.print();

    }-*/;

    private static native void logToConsole(String message) /*-{
		if (console != null) {
			console.log(message);
		}
    }-*/;

    private static native int getDocumentStyleCount() /*-{
		return $doc.styleSheets.length;
    }-*/;

    public static void copyStylesForIE(final StringBuilder builder) {
        final int count = getDocumentStyleCount();
        builder.append("<style>");
        for (int i = 0; i < count; i++) {
            final StyleElement styleSheetElement = getDocumentStyleSheet(i);
            final String cssText = styleSheetElement.getCssText();
            builder.append(cssText);
        }
        builder.append("</style>");
    }

    private static native StyleElement getDocumentStyleSheet(int index) /*-{
		return $doc.styleSheets[index];
    }-*/;

    private static native int getDocumentStyleSheetLength(int index) /*-{
		return $doc.styleSheets[index].cssText.length;
    }-*/;

    public native void appendContents(StyleElement style, String contents) /*-{
		style.cssText += contents;
    }-*/;

    /**
     * Copy styles from head tag
     * 
     * @return
     */
    public static String getStylesFromHeadTag() {
        final StringBuilder builder = new StringBuilder();
        final HeadElement head = getHead();
        final NodeList<Element> styleElements = head.getElementsByTagName("style");
        if (getUserAgent().contains("msie")) {//adding this hack because on IE we can't getting injected css's access through head tag.
            copyStylesForIE(builder);
        } else {
            copyStyles(builder, styleElements);
        }
        return builder.toString();
    }

    public static native String getUserAgent() /*-{
		return navigator.userAgent.toLowerCase();
    }-*/;

    private static void copyStyles(final StringBuilder builder, final NodeList<Element> elementsByTagName) {
        builder.append("<style>");
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            final StyleElement item = StyleElement.as(elementsByTagName.getItem(i));
            builder.append(item.getInnerHTML());//item.getCssText() is not working
        }
        builder.append("</style>");
    }

}
