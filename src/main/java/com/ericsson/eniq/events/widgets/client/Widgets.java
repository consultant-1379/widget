package com.ericsson.eniq.events.widgets.client;

import com.ericsson.eniq.events.common.client.CommonMain;
import com.ericsson.eniq.events.common.client.gin.CommonInjector;
import com.ericsson.eniq.events.widgets.client.button.TextButton;
import com.ericsson.eniq.events.widgets.client.showcase.Holder;
import com.ericsson.eniq.events.widgets.client.togglebuttons.ToggleRail;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.EventBus;

public class Widgets implements EntryPoint, ClickHandler {

    private static CommonInjector commonInjector;

    private EventBus eventbus;

    private static String CONTENT = "se-content";

    private Label selected;

    private AbsolutePanel contentPanel;

    ToggleRail topNav = new ToggleRail("100%");

    final HorizontalPanel menu = new HorizontalPanel();

    final HorizontalPanel widgetSelector = new HorizontalPanel();

    final HorizontalPanel widgetList = createWidgetList();

    final VerticalPanel linksHolder = new VerticalPanel();

    FlowPanel widgetDetailsPanel = new FlowPanel();

    final Label spacer = new Label("\n\n");

    final TextButton Remove = new TextButton ("Close");

    public HorizontalPanel holder = new HorizontalPanel();

    public AbsolutePanel detailsHolder = new AbsolutePanel();

    AbsolutePanel widgetTypePointer = new AbsolutePanel();

    FlowPanel flowPanel = new FlowPanel();

    final Label buttonsLabel = new Label ("Buttons");

    final Label mapLabel = new Label ("Map");

    final Label toggleLabel = new Label ("Toggle");

    final Label popUpLabel = new Label ("Pop-Up");

    final Label textLabel = new Label ("Text");

    final Label miscLabel = new Label ("Misc");

    final Label betaLabel = new Label ("Beta");

    final Image shadow = new Image ("Images/Shadow-bar2.png");

    Image pointerButton = new Image("Images/Pointer.png");

    public Frame contentPanelFrame = new Frame ();

    public VerticalPanel buttonHolder = new VerticalPanel();

    @Override
    public void onModuleLoad() {
        buildWidgetSelector();            //Builds the Selector
        widgetSelectorActions();
        getStyle();
        commonInjector = CommonMain.getCommonInjector();
        eventbus = commonInjector.getEventBus();
        final RootPanel content = RootPanel.get(CONTENT);
        contentPanel = new AbsolutePanel();
        contentPanel.getElement().getStyle().setProperty("minHeight", "780px");
        content.add(contentPanel);
        contentPanel.add(widgetList);
        contentPanel.add(menu);
        contentPanel.addStyleName("Menu");
        widgetTypePointer.add(shadow, 0,-16);
        widgetTypePointer.add(widgetSelector,0,0);
        flowPanel.add(widgetTypePointer);
        contentPanel.add(flowPanel);
        buttons();                                //Starts the page on the Button Widgets
        topNav.addValueChangeHandler (new ValueChangeHandler() {
            @Override
            public void onValueChange(ValueChangeEvent event) {
                contentPanel.removeStyleName("Menu");
                holder.clear();
                contentPanel.clear();
                contentPanel.add(widgetList);
                contentPanel.add(menu);
                menu.clear();
                String Source = topNav.getValue().toString();
                topNav.remove("Widgets");             //Doing this removes the blue highlight bar.
                topNav.remove("Links");
                topNav.remove("How To");
                topNav.add("Widgets");
                topNav.add("Links");
                topNav.add("How To");
                if (Source.equals("Widgets")) {
                    holder.setSize("780px","130px");
                    contentPanel.addStyleName("Menu");
                    contentPanel.remove(widgetSelector);
                    toggleLabel.addStyleName("Normal");
                    buttonsLabel.addStyleName("Decor");
                    mapLabel.addStyleName("Normal");
                    popUpLabel.addStyleName("Normal");
                    textLabel.addStyleName("Normal");
                    miscLabel.addStyleName("Normal");
                    betaLabel.addStyleName("Normal");
                    widgetTypePointer.clear();
                    widgetSelector.getElement().getStyle().setZIndex(20);
                    widgetTypePointer.add(shadow, 0,-12);
                    widgetTypePointer.add(widgetSelector,0,0);
                    flowPanel.add(widgetTypePointer);
                    contentPanel.add(flowPanel);
                    buttons();
                }
                if(Source.equals("Links")) {
                    links();
                }
                if(Source.equals("How To")) {
                    howTo();
                }
            }
        });
        menu.setWidth("780px");
        RootPanel.get(CONTENT).add(contentPanel); //Adds the Widget List in under the Header
        colorCode();
    }

    @Override
    public void onClick(final ClickEvent event) {
        if (event.getSource() instanceof Label) {
            // First remove prev selection
            if (selected != null) {
                selected.removeStyleName("selected-item");
            }
            selected = (Label) event.getSource();
            selected.addStyleName("selected-item");

            final Widget widget = createWidget(selected.getElement().getId());
            if (widget != null) {
                contentPanel.clear();
                contentPanel.add(widget);
            }
            // Need to call this method to highlight example code
            colorCode();
        }}

    private HorizontalPanel createWidgetList() {
        topNav.add("Widgets");
        topNav.add("Links");
        topNav.add("How To");
        HorizontalPanel listContainer = new HorizontalPanel();
        listContainer.getElement().getStyle().setBackgroundColor("#E8E8E8");
        listContainer.add(topNav);
        return listContainer;
    }

    public Widget createWidget(final String id) {

        return null;
    }

    /**
     * Need to call this to color code
     */
    private native void colorCode() /*-{
        $wnd.prettyPrint();
    }-*/;

    public Widget buttons() {
        Holder buttonWidgets = new Holder();
        widgetDetailsPanel.clear();
        menu.clear();
        holder.clear();
        contentPanel.clear();
        contentPanel.add(topNav);
        widgetTypePointer.clear();
        widgetTypePointer.add(pointerButton,42,-2);
        widgetTypePointer.add(shadow, 0,-16);
        widgetTypePointer.add(widgetSelector,0,0);
        flowPanel.add(widgetTypePointer);
        contentPanel.add(widgetList);
        contentPanel.add(menu);
        contentPanel.add(flowPanel);
        holder.add(buttonWidgets.getWidgets("button"));
        menu.add(holder);
        widgetDetailsPanel.add(spacer);
        widgetDetailsPanel.add(buttonWidgets.widgetActions());
        contentPanel.add(widgetDetailsPanel);
        return contentPanel;
    }
    public Widget misc() {
        Holder miscWidgets = new Holder();
        menu.clear();
        widgetDetailsPanel.clear();
        holder.clear();
        contentPanel.clear();
        contentPanel.add(topNav);
        widgetTypePointer.clear();
        widgetTypePointer.add(pointerButton,602,-2);
        widgetTypePointer.add(shadow, 0,-16);
        widgetTypePointer.add(widgetSelector,0,0);
        flowPanel.add(widgetTypePointer);
        contentPanel.add(widgetList);
        contentPanel.add(menu);
        contentPanel.add(flowPanel);
        holder.add(miscWidgets.getWidgets("misc"));
        menu.add(holder);
        widgetDetailsPanel.add(spacer);
        widgetDetailsPanel.add(miscWidgets.widgetActions());
        contentPanel.add(widgetDetailsPanel);
        return contentPanel;
    }
    public Widget map() {
        Holder mapWidgets = new Holder();
        menu.clear();
        widgetDetailsPanel.clear();
        holder.clear();
        contentPanel.clear();
        contentPanel.add(topNav);
        widgetTypePointer.clear();
        widgetTypePointer.add(pointerButton,157,-2);
        widgetTypePointer.add(shadow, 0,-16);
        widgetTypePointer.add(widgetSelector,0,0);
        flowPanel.add(widgetTypePointer);
        contentPanel.add(widgetList);
        contentPanel.add(menu);
        contentPanel.add(flowPanel);
        holder.add(mapWidgets.getWidgets("map"));
        menu.add(holder);
        widgetDetailsPanel.add(spacer);
        widgetDetailsPanel.add(mapWidgets.widgetActions());
        contentPanel.add(widgetDetailsPanel);
        return contentPanel;
    }
    public Widget text() {
        Holder textWidgets = new Holder();
        menu.clear();
        widgetDetailsPanel.clear();
        holder.clear();
        contentPanel.clear();
        contentPanel.add(topNav);
        widgetTypePointer.clear();
        widgetTypePointer.add(pointerButton,493,-2);
        widgetTypePointer.add(shadow, 0,-16);
        widgetTypePointer.add(widgetSelector,0,0);
        flowPanel.add(widgetTypePointer);
        contentPanel.add(widgetList);
        contentPanel.add(menu);
        contentPanel.add(flowPanel);
        holder.add(textWidgets.getWidgets("text"));
        menu.add(holder);
        widgetDetailsPanel.add(spacer);
        widgetDetailsPanel.add(textWidgets.widgetActions());
        contentPanel.add(widgetDetailsPanel);
        return contentPanel;
    }
    public Widget popUp() {
        Holder popUpWidgets = new Holder();
        menu.clear();
        widgetDetailsPanel.clear();
        holder.clear();
        contentPanel.clear();
        contentPanel.add(topNav);
        widgetTypePointer.clear();
        widgetTypePointer.add(pointerButton, 378, -2);
        widgetTypePointer.add(shadow, 0,-16);
        widgetTypePointer.add(widgetSelector,0,0);
        flowPanel.add(widgetTypePointer);
        contentPanel.add(widgetList);
        contentPanel.add(menu);
        contentPanel.add(flowPanel);
        holder.add(popUpWidgets.getWidgets("popUp"));
        menu.add(holder);
        widgetDetailsPanel.add(spacer);
        widgetDetailsPanel.add(popUpWidgets.widgetActions());
        contentPanel.add(widgetDetailsPanel);
        return contentPanel;
    }
    public Widget beta() {
        Holder betaWidgets = new Holder();
        menu.clear();
        widgetDetailsPanel.clear();
        holder.clear();
        contentPanel.clear();
        contentPanel.add(topNav);
        widgetTypePointer.clear();
        widgetTypePointer.add(pointerButton,713,-2);
        widgetTypePointer.add(shadow, 0,-16);
        widgetTypePointer.add(widgetSelector,0,0);
        flowPanel.add(widgetTypePointer);
        contentPanel.add(widgetList);
        contentPanel.add(menu);
        contentPanel.add(flowPanel);
        holder.add(betaWidgets.getWidgets("beta"));
        menu.add(holder);
        widgetDetailsPanel.add(spacer);
        widgetDetailsPanel.add(betaWidgets.widgetActions());
        contentPanel.add(widgetDetailsPanel);
        return contentPanel;
    }
    public Widget toggle() {
        Holder toggleWidgets = new Holder();
        menu.clear();
        widgetDetailsPanel.clear();
        holder.clear();
        contentPanel.clear();
        contentPanel.add(topNav);
        widgetTypePointer.clear();
        widgetTypePointer.add(pointerButton,268,-2);
        widgetTypePointer.add(shadow, 0,-16);
        widgetTypePointer.add(widgetSelector,0,0);
        flowPanel.add(widgetTypePointer);
        contentPanel.add(widgetList);
        contentPanel.add(menu);
        contentPanel.add(flowPanel);
        holder.add(toggleWidgets.getWidgets("toggle"));
        menu.add(holder);
        widgetDetailsPanel.add(spacer);
        widgetDetailsPanel.add(toggleWidgets.widgetActions());
        contentPanel.add(widgetDetailsPanel);
        return contentPanel;
    }
    public Widget links() {
        contentPanel.clear();
        contentPanel.add(topNav);
        FlowPanel linkpanel = new FlowPanel();
        Holder linksholder = new Holder();
        linkpanel.add(linksholder.getLinks("links"));
        linkpanel.setStyleName("links");
        contentPanel.add(linkpanel);
        return contentPanel;
    }
    public Widget buildWidgetSelector() {
        widgetSelector.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        widgetSelector.setWidth("780px");
        widgetSelector.setHeight("30px");
        widgetSelector.addStyleName("LabelBar");
        buttonsLabel.addStyleName("Normal");
        mapLabel.addStyleName("Normal");
        toggleLabel.addStyleName("Normal");
        popUpLabel.addStyleName("Normal");
        textLabel.addStyleName("Normal");
        miscLabel.addStyleName("Normal");
        betaLabel.addStyleName("Normal");
        buttonsLabel.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        mapLabel.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        toggleLabel.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        popUpLabel.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        textLabel.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        miscLabel.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        betaLabel.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        widgetSelector.add(buttonsLabel);                    //If you need to add more toggle Buttons add them here
        widgetSelector.add(mapLabel);
        widgetSelector.add(toggleLabel);
        widgetSelector.add(popUpLabel);
        widgetSelector.add(textLabel);
        widgetSelector.add(miscLabel);
        widgetSelector.add(betaLabel);
        buttonsLabel.setWidth("50px");
        mapLabel.setWidth("50px");
        toggleLabel.setWidth("50px");
        popUpLabel.setWidth("50px");
        textLabel.setWidth("50px");
        miscLabel.setWidth("50px");
        betaLabel.setWidth("50px");
        return widgetSelector;
    }

    public void widgetSelectorActions() {
        Remove.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                linksHolder.remove(detailsHolder);
            }
        });

        buttonsLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                buttons();                   //Add your Button Widgets in the Buttons() Method. Step by Step Guide to adding Widgets to the Menu.
            }
        });

        mapLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                map();                   //Add your Map Widgets in the Map() Method
            }                           });

        textLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                text();                   //Add your Text Widgets in the Map() Method
            }
        });

        popUpLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                popUp();                   //Add your Pop Up Widgets in the Map() Method
            }
        });

        miscLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                misc();                   //Add your Misc Widgets in the Map() Method
            }
        });

        betaLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                beta();                   //Add your Beta Widgets in the Map() Method
            }
        });

        toggleLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                toggle();                   //Add your Toggle Widgets in the Map() Method
            }
        });
    }

    public void getStyle()  {
        holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        contentPanelFrame.setSize("600px","200px");
        Remove.setSize("80px","25px");
        buttonHolder.setSize("150px","100px");
        shadow.getElement().getStyle().setZIndex(22);
        shadow.setWidth("780px");
        spacer.getElement().getStyle().setProperty("whiteSpace", "pre");
        widgetTypePointer.setSize("780px", "30px");
        flowPanel.setSize("780px", "40px");
        holder.setSize("780px","130px");
        menu.setHeight("125px");
        menu.setWidth("780px");
        linksHolder.setWidth("780px");     //For more Links Increase size
        widgetTypePointer.addStyleName("Centre");
        menu.addStyleName("Border");
        linksHolder.addStyleName("Border");
        widgetDetailsPanel.addStyleName("PaddingLeft");
        pointerButton.getElement().getStyle().setZIndex(25);
        widgetSelector.getElement().getStyle().setZIndex(20);
    }

    public void howTo() {
        contentPanel.clear();
        contentPanel.add(topNav);
        contentPanel.addStyleName("Menu");
        Label Header = new Label ("Setting Up Git");
        HTML GITUI = new HTML (
                "<a href=\"href http://confluence-oss.lmera.ericsson.se/display/TEAMMIT/GIT\">GIT CheatSheet</a>");
        HTML GITSETUPHUB = new HTML (
                "<a href=\"http://confluence-oss.lmera.ericsson.se/display/TORCM/GIT+Setup\">Setting up GIT on the Hub</a>");
        Label Header2 = new Label (
                "ENIQ Events UI");
        HTML UIBasics = new HTML (
                "<a href=\"http://confluence-oss.lmera.ericsson.se/display/TEAMMIT/ENIQ+EVENT+UI\">Introduction to UI</a>");
        Header.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        Header.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
        GITUI.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        Header2.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        Header2.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
        UIBasics.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        GITSETUPHUB.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        Header.setStyleName("Headers");
        Header2.setStyleName("Headers");
        contentPanel.add(spacer);
        contentPanel.add(Header);
        contentPanel.add(GITUI);
        contentPanel.add(GITSETUPHUB);
        contentPanel.add(spacer);
        contentPanel.add(Header2);
        contentPanel.add(UIBasics);
    }
}