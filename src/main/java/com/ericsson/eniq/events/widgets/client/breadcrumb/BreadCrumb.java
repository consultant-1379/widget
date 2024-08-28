package com.ericsson.eniq.events.widgets.client.breadcrumb;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

import java.util.ArrayList;
import java.util.List;

public class BreadCrumb extends Composite {

    static {
        final BreadCrumbResourceBundle resources = GWT.create(BreadCrumbResourceBundle.class);
        resources.css().ensureInjected();
    }

    private final HorizontalPanel breadCrumbHolderPanel = new HorizontalPanel();
    private final List<BreadCrumbButton> breadCrumbButtonsList = new ArrayList<BreadCrumbButton>();

    private BreadCrumbButton currentSelection;

    /**
     * Create Empty Bread Crumb to add crumbs to
     */
    public BreadCrumb() {
        breadCrumbHolderPanel.getElement().getStyle().setMarginLeft(25, Style.Unit.PX);  //off set the overlapping negative margins
        initWidget(breadCrumbHolderPanel);
    }

    /**
     * Add Breadcrumb Element to the breadcrumb - text only
     *
     * @param breadcrumbText - text for Breadcrumb button
     */
    public void addBreadCrumb(final String breadcrumbText) {
        final BreadCrumbButton breadCrumbItem = new BreadCrumbButton(breadcrumbText);
        setUpBreadCrumb(breadCrumbItem);
    }

    /**
     * @param breadcrumbText - text for Breadcrumb button
     * @param details        - to store extra trail info (date time etc)
     */
    public void addBreadCrumb(final String breadcrumbText, final BreadCrumbDetails details) {
        final BreadCrumbButton breadCrumbItem = new BreadCrumbButton(breadcrumbText, details);
        setUpBreadCrumb(breadCrumbItem);
    }

    public void clear() {
        breadCrumbHolderPanel.clear();
        breadCrumbButtonsList.clear();
        if (currentSelection != null) {
            currentSelection.clear();
        }
    }

    private void setUpBreadCrumb(final BreadCrumbButton breadcrumbItem) {
        breadCrumbHolderPanel.add(breadcrumbItem);
        breadCrumbButtonsList.add(breadcrumbItem);
        currentSelection = breadcrumbItem; //most recent added is selected
        breadcrumbItem.setSelected(true);

        checkForAndTurnOffOthers(); //turn rest off
    }

    private void removeCrumbTrailElements() {
        final int lastCrumb = breadCrumbButtonsList.indexOf(currentSelection);
        int i = breadCrumbButtonsList.size() - 1;
        while (i > lastCrumb) {
            breadCrumbButtonsList.remove(i);
            i--;
        }
        redrawBreadCrumb();
    }

    private void redrawBreadCrumb() {
        breadCrumbHolderPanel.clear();
        for (BreadCrumbButton b : breadCrumbButtonsList) {
            breadCrumbHolderPanel.add(b);
        }
    }

    private void checkForAndTurnOffOthers() {
        for (BreadCrumbButton b : breadCrumbButtonsList) {
            if (!b.equals(currentSelection)) {
                b.setSelected(false);
            }
        }
    }

    /**
     * @return Details of Current position in breadcrumb
     */
    public BreadCrumbDetails getCurrentBreadCrumbDetails() {
        return currentSelection.getBreadCrumbDetails();
    }

    /*Individual Bread Crumbs*/
    class BreadCrumbButton extends FocusPanel {

        HorizontalPanel breadcrumb = new HorizontalPanel();
        SimplePanel toggleButton = new SimplePanel();
        Label buttonLabel = new Label();
        SimplePanel buttonArrow = new SimplePanel();
        private BreadCrumbDetails breadCrumbDetails;

        private String title;

        private boolean isSelected; //should the button appear pressed

        BreadCrumbButton(final String title) {

            createButtonArrow();
            createButton(title);
            breadcrumb.add(toggleButton);
            breadcrumb.add(buttonArrow);
            addClickHandler(new BreadCrumbClickEvent());
            add(breadcrumb);
        }

        BreadCrumbButton(final String title, BreadCrumbDetails details) {
            this(title);
            this.breadCrumbDetails = details;
        }

        private void createButtonArrow() {
            buttonArrow.setStyleName("crumbArrow");
        }

        private void createButton(final String title) {
            buttonLabel.setText(title);
            toggleButton.add(buttonLabel);
            toggleButton.setStyleName("breadCrumb");
        }

        public String getTitle() {
            return title;
        }

        public boolean getSelected() {
            return this.isSelected;
        }

        public void setSelected(final boolean selected) {
            this.isSelected = selected;
            if (selected) {
                addSelectedOnStyle();
            } else {
                addSelectedOffStyle();
            }
        }

        private void addSelectedOffStyle() {
            toggleButton.removeStyleDependentName("down");
            buttonArrow.removeStyleDependentName("down");
            toggleButton.addStyleDependentName("up");
            buttonArrow.addStyleDependentName("up");
        }

        private void addSelectedOnStyle() {
            toggleButton.removeStyleDependentName("up");
            buttonArrow.removeStyleDependentName("up");
            toggleButton.addStyleDependentName("down");
            buttonArrow.addStyleDependentName("down");
        }

        public void setTitle(final String title) {
            this.title = title;
        }

        public BreadCrumbDetails getBreadCrumbDetails() {
            return breadCrumbDetails;
        }

        public void setBreadCrumbDetails(final BreadCrumbDetails breadCrumbDetails) {
            this.breadCrumbDetails = breadCrumbDetails;
        }

        class BreadCrumbClickEvent implements ClickHandler {
            public void onClick(final ClickEvent event) {
                setSelected(true);
                currentSelection = (BreadCrumbButton) event.getSource();
                removeCrumbTrailElements();
            }
        }
    }
}
