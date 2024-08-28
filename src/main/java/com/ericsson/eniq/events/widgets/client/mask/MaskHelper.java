/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.widgets.client.mask;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.DOM;

/**
 * Mask helper for when porlet is loading data
 * @author edavboj
 * @since October 2011
 */
public class MaskHelper {

    private String parentPreviousPosition;

    private Element maskElement;

    private static final MaskHelperResourceBundle bundle;

    private static final MaskTemplate TEMPLATE;

    static {
        bundle = GWT.create(MaskHelperResourceBundle.class);
        bundle.style().ensureInjected();

        TEMPLATE = GWT.create(MaskTemplate.class);
    }

    public MaskHelper() {
    }

    /**
     * Apply loading animation to provided element
     *
     * @param el           Element to which animation is applied
     * @param message      Message to show under progress bar
     * @param portalHeight height of portlet
     */
    public void mask(final Element el, final String message, final int portalHeight) {
        if (maskElement != null) {
            return;
        }

        parentPreviousPosition = el.getStyle().getPosition();
        el.getStyle().setPosition(Style.Position.RELATIVE);

        // Make sure that el is container type (like div)
        maskElement = createProgressBarElement(message, portalHeight);
        el.appendChild(maskElement);
    }

    /**
     * Use to mask widget which may resize i.e FloatingWindow. No need to provide height. Mask element take height 100%. 
     * @param el
     * @param message
     */
    public void mask(final Element el, final String message) {
        if (maskElement != null) {
            return;
        }

        parentPreviousPosition = el.getStyle().getPosition();
        el.getStyle().setPosition(Style.Position.RELATIVE);

        // Make sure that el is container type (like div)
        maskElement = createResizableProgressBarElement(message);
        el.appendChild(maskElement);
        maskElement.getStyle().setHeight(100, Style.Unit.PCT);
        maskElement.addClassName(bundle.style().portletMask());
    }

    public void unmask() {
        if (maskElement == null) {
            return;
        }

        if (parentPreviousPosition == null || parentPreviousPosition.isEmpty()) {
            maskElement.getParentElement().getStyle().clearPosition();
        } else {
            maskElement.getParentElement().getStyle()
                    .setPosition(Style.Position.valueOf(parentPreviousPosition.toUpperCase()));
        }

        maskElement.removeFromParent();

        parentPreviousPosition = null;
        maskElement = null;
    }

    public void updateMessage(final String message) {
        if (maskElement != null) {
            Element.as(maskElement.getChild(0).getChild(0)).setInnerText(message);
        }
    }

    private Element createResizableProgressBarElement(final String message) {
        final Element maskElement = DOM.createDiv();
        final SafeStylesBuilder textContainerStyle = new SafeStylesBuilder();
        final SafeHtml maskHtml = TEMPLATE.mask(bundle.style().portletMaskText(), textContainerStyle.toSafeStyles(),
                message, bundle.style().portletMaskBackground());

        maskElement.setInnerHTML(maskHtml.asString());

        return maskElement;
    }

    private Element createProgressBarElement(final String message, final int portalHeight) {
        final Element maskElement = DOM.createDiv();
        maskElement.getStyle().setHeight(portalHeight, Style.Unit.PX);
        maskElement.addClassName(bundle.style().portletMask());

        final int offset = 1; // offset from center - because we have nice animated image in the center
        final int messageHeight = (portalHeight / 2) - offset - 10; // extra 10 to not go over the border of portlet

        final SafeStylesBuilder textContainerStyle = new SafeStylesBuilder();
        textContainerStyle.appendTrustedString("height: " + messageHeight + "px;");
        textContainerStyle.appendTrustedString("margin-top: " + offset + "px;");

        final SafeHtml maskHtml = TEMPLATE.mask(bundle.style().portletMaskText(), textContainerStyle.toSafeStyles(),
                message, bundle.style().portletMaskBackground());

        maskElement.setInnerHTML(maskHtml.asString());

        return maskElement;
    }

    public interface MaskTemplate extends SafeHtmlTemplates {
        @Template("<div class=\"{0}\" style=\"{1}\"><div>{2}</div></div><div class=\"{3}\"></div>")
        SafeHtml mask(String textContainerClass, SafeStyles textContainerStyle, String message,
                String maskBackgroundStyle);
    }
}
